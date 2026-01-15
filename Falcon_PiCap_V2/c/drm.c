#include "drm.h"

#include <fcntl.h>
#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <sys/mman.h>
#include <unistd.h>

#include <xf86drm.h>
#include <xf86drmMode.h>

#include <bcm2835.h>

uint32_t connector_id;
uint32_t crtc_id;
uint32_t fb;
uint8_t* fb_ptr;
int fd;
drmModeModeInfo mode;
uint64_t size;

uint64_t bufferSize() {
    return size;
}

void destroy() {

    off();
    displayIt();

    // sleep time picked by trial and error
    usleep(30000);
    disableDisplay();

    if (fb_ptr != NULL && fb_ptr != MAP_FAILED) {
        munmap(fb_ptr, size);
        fb_ptr = NULL;
    }
    
    if (fb != 0) {
        drmModeRmFB(fd, fb);
        fb = 0;
    }

    if (fd >= 0) {
        close(fd);
        fd = -1;
    }

    bcm2835_close();
}

int displayIt() {

    if (drmModeSetCrtc(fd, crtc_id, fb, 0, 0, &connector_id, 1, &mode) != 0) {
        perror("drmModeSetCrtc failed");
        return 1;
    }

    return 0;
}

void disableDisplay() {

    // configure pins for input
    bcm2835_gpio_fsel(5, BCM2835_GPIO_FSEL_INPT);
    bcm2835_gpio_fsel(6, BCM2835_GPIO_FSEL_INPT);
    bcm2835_gpio_fsel(13, BCM2835_GPIO_FSEL_INPT);
    bcm2835_gpio_fsel(26, BCM2835_GPIO_FSEL_INPT);

}

void enableDisplay() {

    // configure pins for DPI                               Position in RGB888
    bcm2835_gpio_fsel(5, BCM2835_GPIO_FSEL_ALT2); // DPI_D1 0x020000
    bcm2835_gpio_fsel(6, BCM2835_GPIO_FSEL_ALT2); // DPI_D2 0x040000
    bcm2835_gpio_fsel(13, BCM2835_GPIO_FSEL_ALT2); // DPI_D9 0x000200
    bcm2835_gpio_fsel(26, BCM2835_GPIO_FSEL_ALT2); // DPI_D22 0x000040

}

uint8_t* init() {
    drmModeRes *res = NULL;
    drmModeConnector *conn = NULL;
    drmModeEncoder *enc = NULL;
    
    // Initialize globals
    fb_ptr = NULL;
    fb = 0;
    fd = -1;
    connector_id = 0;
    crtc_id = 0;

    if (!bcm2835_init()) {
        fprintf(stderr, "BCM2835 Library initialization failed!\n");
        return NULL;
    }

    fd = open("/dev/dri/card0", O_RDWR | O_CLOEXEC);
    if (fd < 0) {
        perror("open /dev/dri/card0");
        goto cleanup_fail;
    }

    res = drmModeGetResources(fd);
    if (!res) {
        perror("drmModeGetResources");
        goto cleanup_fail;
    }

    // Find first connected connector
    for (int i = 0; i < res->count_connectors; i++) {
        conn = drmModeGetConnector(fd, res->connectors[i]);
        if (!conn) {
            continue;
        }
        
        if (conn->connection == DRM_MODE_CONNECTED) {
            connector_id = conn->connector_id;
            break;
        }
        
        drmModeFreeConnector(conn);
        conn = NULL;
    }

    if (!conn || connector_id == 0) {
        fprintf(stderr, "No connected connector found.\n");
        goto cleanup_fail;
    }

    if (conn->count_modes == 0) {
        fprintf(stderr, "No display modes available.\n");
        goto cleanup_fail;
    }

    mode = conn->modes[0];  // use preferred mode
    printf("Mode: %dx%d\n", mode.hdisplay, mode.vdisplay);

    // Find a CRTC
    if (conn->encoder_id == 0) {
        fprintf(stderr, "Connector has no encoder.\n");
        goto cleanup_fail;
    }
    
    enc = drmModeGetEncoder(fd, conn->encoder_id);
    if (!enc) {
        perror("drmModeGetEncoder");
        goto cleanup_fail;
    }
    
    crtc_id = enc->crtc_id;
    drmModeFreeEncoder(enc);
    enc = NULL;

    // Create dumb buffer
    struct drm_mode_create_dumb create = {0};
    create.width  = mode.hdisplay;
    create.height = mode.vdisplay;
    create.bpp    = 24; // 3 bytes per pixel (RGB888)

    if (drmIoctl(fd, DRM_IOCTL_MODE_CREATE_DUMB, &create) < 0) {
        perror("DRM_IOCTL_MODE_CREATE_DUMB");
        goto cleanup_fail;
    }

    uint32_t handle = create.handle;
    uint32_t pitch  = create.pitch;
    size = create.size;

    printf("Buffer size: %ld\n", size);

    struct drm_mode_map_dumb map = {0};
    map.handle = handle;

    if (drmIoctl(fd, DRM_IOCTL_MODE_MAP_DUMB, &map) < 0) {
        perror("DRM_IOCTL_MODE_MAP_DUMB");
        goto cleanup_fail;
    }

    fb_ptr = mmap(0, size, PROT_READ | PROT_WRITE, MAP_SHARED, fd, map.offset);
    if (fb_ptr == MAP_FAILED) {
        perror("mmap");
        fb_ptr = NULL;
        goto cleanup_fail;
    }

    // Create framebuffer object
    if (drmModeAddFB(fd, mode.hdisplay, mode.vdisplay, 24, 24,
                     pitch, handle, &fb) != 0) {
        perror("drmModeAddFB");
        goto cleanup_fail;
    }

    // Clean up temporary resources
    if (conn) {
        drmModeFreeConnector(conn);
        conn = NULL;
    }
    if (res) {
        drmModeFreeResources(res);
        res = NULL;
    }

    off();
    displayIt();
    enableDisplay();

    return fb_ptr;

cleanup_fail:
    // Clean up on failure
    if (enc) {
        drmModeFreeEncoder(enc);
    }
    if (conn) {
        drmModeFreeConnector(conn);
    }
    if (res) {
        drmModeFreeResources(res);
    }
    if (fb_ptr && fb_ptr != MAP_FAILED) {
        munmap(fb_ptr, size);
    }
    if (fd >= 0) {
        close(fd);
    }
    
    return NULL;
}

int getBufferPosition(int index) {
    return index * 216 + (index / 5) * 6;
}

void off() {
    uint64_t i = 0;
    
    // Bounds check to prevent buffer overflow
    uint64_t max_position = 162 * 5 * 216 + (162 * 5 / 5) * 6;
    if (max_position > size) {
        fprintf(stderr, "Warning: off() may exceed buffer size\n");
    }
    
    for (int row = 0; row < 162; row++) {
        i = getBufferPosition(row * 5);

        for (int index = 0; index < 120; index++) {
            fb_ptr[i++] = 0x00;
            fb_ptr[i++] = 0x00;
            fb_ptr[i++] = 0x00;
            fb_ptr[i++] = 0xFF;
            fb_ptr[i++] = 0xFF;
            fb_ptr[i++] = 0xFF;
            fb_ptr[i++] = 0x00;
            fb_ptr[i++] = 0x00;
            fb_ptr[i++] = 0x00;
        }

        fb_ptr[i++] = 0x00;
        fb_ptr[i++] = 0x00;
        fb_ptr[i++] = 0x00;
        fb_ptr[i++] = 0x00;
        fb_ptr[i++] = 0x00;
        fb_ptr[i++] = 0x00;
    }
}
