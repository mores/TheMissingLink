#include <jni.h>
#include <stdio.h>
#include "HelloJNI.h"

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

JNIEXPORT jobject JNICALL Java_HelloJNI_getFrameBuffer(JNIEnv* env, jobject obj) {

    return (*env)->NewDirectByteBuffer(env, fb_ptr, size);
}

JNIEXPORT void JNICALL Java_HelloJNI_displayIt(JNIEnv *env, jobject thisObject) {

    printf("displayIt!\n");

    if (drmModeSetCrtc(fd, crtc_id, fb, 0, 0, &connector_id, 1, &mode) != 0) {
        perror("drmModeSetCrtc failed");
        return;
    }

}

JNIEXPORT void JNICALL Java_HelloJNI_sayGoodBye(JNIEnv *env, jobject thisObject) {

    printf("GoodBye from C!\n");
    munmap(fb_ptr, size);
    drmModeRmFB(fd, fb);

    close(fd);
}


// Implementation of the native method
JNIEXPORT void JNICALL Java_HelloJNI_sayHello(JNIEnv *env, jobject thisObject) {
    printf("Hello from C!\n");

    if (bcm2835_init()) {
        // configure pins for DPI                               Position in RGB888
        bcm2835_gpio_fsel(5, BCM2835_GPIO_FSEL_ALT2); // DPI_D1 0x020000
        bcm2835_gpio_fsel(6, BCM2835_GPIO_FSEL_ALT2); // DPI_D2 0x040000
        bcm2835_gpio_fsel(13, BCM2835_GPIO_FSEL_ALT2); // DPI_D9 0x000200
        bcm2835_gpio_fsel(26, BCM2835_GPIO_FSEL_ALT2); // DPI_D22 0x000040

        bcm2835_close();
    }
    else {
        printf("BCM2835 Library initialization failed!\n");
    }

    fd = open("/dev/dri/card0", O_RDWR | O_CLOEXEC);
    if (fd < 0) {
        perror("open");
        return;
    }

	drmModeRes *res = drmModeGetResources(fd);
    if (!res) {
        perror("drmModeGetResources");
        return;
    }

    drmModeConnector *conn = NULL;
    connector_id = 0;

    // Find first connected connector
    for (int i = 0; i < res->count_connectors; i++) {
        conn = drmModeGetConnector(fd, res->connectors[i]);
        if (conn->connection == DRM_MODE_CONNECTED) {
            connector_id = conn->connector_id;
            break;
        }
        drmModeFreeConnector(conn);
        conn = NULL;
    }

    if (!conn) {
        fprintf(stderr, "No connected connector found.\n");
        return;
    }

    mode = conn->modes[0];  // use preferred mode
    printf("Mode: %dx%d\n", mode.hdisplay, mode.vdisplay);

    // Find a CRTC
    drmModeEncoder *enc = drmModeGetEncoder(fd, conn->encoder_id);
    crtc_id = enc->crtc_id;
    drmModeFreeEncoder(enc);

    // Create dumb buffer
    struct drm_mode_create_dumb create = {0};
    create.width  = mode.hdisplay;
    create.height = mode.vdisplay;
    create.bpp    = 24; // 4 bytes/pixel

    if (drmIoctl(fd, DRM_IOCTL_MODE_CREATE_DUMB, &create) < 0) {
        perror("CREATE_DUMB");
        return;
    }

    uint32_t handle = create.handle;
    uint32_t pitch  = create.pitch;
    size   = create.size;

	struct drm_mode_map_dumb map = {0};
    map.handle = handle;

    if (drmIoctl(fd, DRM_IOCTL_MODE_MAP_DUMB, &map) < 0) {
        perror("MAP_DUMB");
        return;
    }

    fb_ptr = mmap(0, size, PROT_READ | PROT_WRITE, MAP_SHARED, fd, map.offset);
    if (fb_ptr == MAP_FAILED) {
        perror("mmap");
        return;
    }

    // Create framebuffer object
    if (drmModeAddFB(fd, mode.hdisplay, mode.vdisplay, 24, 24,
                     pitch, handle, &fb) != 0) {
        perror("drmModeAddFB");
        return;
    }
}
