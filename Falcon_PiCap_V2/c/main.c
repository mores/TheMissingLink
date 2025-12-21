// gcc -c main.c 

#include "drm.h"
#include <stdio.h>
#include <stdint.h>
#include <sys/mman.h>
#include <unistd.h>

int main() {

    printf("Main\n" );

	uint8_t* fb_ptr = init();

	int size = bufferSize();

	// Turn entire string off
    for (uint64_t i = 0; i < size; i += 9) {
        fb_ptr[i + 0] = 0x00;
        fb_ptr[i + 1] = 0x00;
        fb_ptr[i + 2] = 0x00;
        fb_ptr[i + 3] = 0xFF;
        fb_ptr[i + 4] = 0xFF;
        fb_ptr[i + 5] = 0xFF;
        fb_ptr[i + 6] = 0x00;
        fb_ptr[i + 7] = 0x00;
        fb_ptr[i + 8] = 0x00;
    }

	// 1st pixel green
    for (size_t i = 72; i < 136; i += 9) {
        fb_ptr[i + 0] = 0x00;
        fb_ptr[i + 1] = 0x00;
        fb_ptr[i + 2] = 0x00;
        fb_ptr[i + 3] = 0xFF;
        fb_ptr[i + 4] = 0xFF;
        fb_ptr[i + 5] = 0xFF;
        fb_ptr[i + 6] = 0x06;
        fb_ptr[i + 7] = 0x00;
        fb_ptr[i + 8] = 0x00;
    }

	// 2nd pixel red
    int pixelPos = 216;
    for (uint64_t i = 0 + pixelPos; i < 64 + pixelPos; i += 9) {
        fb_ptr[i + 0] = 0x00;
        fb_ptr[i + 1] = 0x00;
        fb_ptr[i + 2] = 0x00;
        fb_ptr[i + 3] = 0xFF;
        fb_ptr[i + 4] = 0xFF;
        fb_ptr[i + 5] = 0xFF;
        fb_ptr[i + 6] = 0x06;
        fb_ptr[i + 7] = 0x00;
        fb_ptr[i + 8] = 0x00;
    }

	// 3rd pixel blue
    int otherPos = 432;
    for (size_t i = 144 + otherPos; i < 216 + otherPos; i += 9) {
        fb_ptr[i + 0] = 0x00;
        fb_ptr[i + 1] = 0x00;
        fb_ptr[i + 2] = 0x00;
        fb_ptr[i + 3] = 0xFF;
        fb_ptr[i + 4] = 0xFF;
        fb_ptr[i + 5] = 0xFF;
        fb_ptr[i + 6] = 0x06;
        fb_ptr[i + 7] = 0x00;
        fb_ptr[i + 8] = 0x00;
    }

	/*
    // 4th yellow
    int fred = 621;
    for( size_t i = 0 + fred; i < 153 + fred; i += 9) {
        printf("%d\n", i);
        fb_ptr[i + 0] = 0x00;
        fb_ptr[i + 1] = 0x00;
        fb_ptr[i + 2] = 0x00;
        fb_ptr[i + 3] = 0xFF;
        fb_ptr[i + 4] = 0xFF;
        fb_ptr[i + 5] = 0xFF;
        fb_ptr[i + 6] = 0x06;
        fb_ptr[i + 7] = 0x00;
        fb_ptr[i + 8] = 0x00;
    }

	// 5th teal
    int larry = 828;
    for( size_t i = 81 + larry; i < 207 + larry; i += 9) {
        printf("%d\n", i);
        fb_ptr[i + 0] = 0x00;
        fb_ptr[i + 1] = 0x00;
        fb_ptr[i + 2] = 0x00;
        fb_ptr[i + 3] = 0xFF;
        fb_ptr[i + 4] = 0xFF;
        fb_ptr[i + 5] = 0xFF;
        fb_ptr[i + 6] = 0x06;
        fb_ptr[i + 7] = 0x00;
        fb_ptr[i + 8] = 0x00;
    }
    */

	for (size_t i = 0; i < 648; i++) {
        printf("%02X ", fb_ptr[i]);
        if ((i + 1) % 9 == 0)
        {
            printf("\n");
        }

        if ((i + 1) % 216 == 0)
        {
            printf("- - %ld - \n", i);
        }
    }
    printf("\n");

	displayIt();

	sleep(5);

	// Turn entire string off
    for (uint64_t i = 0; i < size; i += 9) {
        fb_ptr[i + 0] = 0x00;
        fb_ptr[i + 1] = 0x00;
        fb_ptr[i + 2] = 0x00;
        fb_ptr[i + 3] = 0xFF;
        fb_ptr[i + 4] = 0xFF;
        fb_ptr[i + 5] = 0xFF;
        fb_ptr[i + 6] = 0x00;
        fb_ptr[i + 7] = 0x00;
        fb_ptr[i + 8] = 0x00;
    }

    displayIt();

	destroy();

    return 0;
}

