// gcc -c main.c

#include "drm.h"
#include <stdint.h>
#include <stdio.h>
#include <sys/mman.h>
#include <unistd.h>

int main() {

  printf("Main\n");

  uint8_t *fb_ptr = init();

  int pos = getBufferPosition(0);
  // 1st pixel green
  for (int i = pos + 72; i < pos + 136; i += 9) {
    fb_ptr[i + 0] = 0x00;
    fb_ptr[i + 1] = 0x00;
    fb_ptr[i + 2] = 0x00;
    fb_ptr[i + 3] = 0xFF;
    fb_ptr[i + 4] = 0xFF;
    fb_ptr[i + 5] = 0xFF;
    fb_ptr[i + 6] = 0x06;
    fb_ptr[i + 7] = 0x02;
    fb_ptr[i + 8] = 0x40;
  }

  // 2nd pixel red
  pos = getBufferPosition(1);
  for (int i = 0 + pos; i < pos + 64; i += 9) {
    fb_ptr[i + 0] = 0x00;
    fb_ptr[i + 1] = 0x00;
    fb_ptr[i + 2] = 0x00;
    fb_ptr[i + 3] = 0xFF;
    fb_ptr[i + 4] = 0xFF;
    fb_ptr[i + 5] = 0xFF;
    fb_ptr[i + 6] = 0x06;
    fb_ptr[i + 7] = 0x02;
    fb_ptr[i + 8] = 0x40;
  }

  // 3rd pixel blue
  pos = getBufferPosition(2);
  for (int i = pos + 144; i < pos + 216; i += 9) {
    fb_ptr[i + 0] = 0x00;
    fb_ptr[i + 1] = 0x00;
    fb_ptr[i + 2] = 0x00;
    fb_ptr[i + 3] = 0xFF;
    fb_ptr[i + 4] = 0xFF;
    fb_ptr[i + 5] = 0xFF;
    fb_ptr[i + 6] = 0x06;
    fb_ptr[i + 7] = 0x02;
    fb_ptr[i + 8] = 0x40;
  }

  // 4th yellow
  pos = getBufferPosition(3);
  for (int i = pos; i < pos + 128; i += 9) {
    fb_ptr[i + 0] = 0x00;
    fb_ptr[i + 1] = 0x00;
    fb_ptr[i + 2] = 0x00;
    fb_ptr[i + 3] = 0xFF;
    fb_ptr[i + 4] = 0xFF;
    fb_ptr[i + 5] = 0xFF;
    fb_ptr[i + 6] = 0x06;
    fb_ptr[i + 7] = 0x02;
    fb_ptr[i + 8] = 0x40;
  }

  // 5th teal
  pos = getBufferPosition(4);
  for (int i = pos + 81; i < pos + 207; i += 9) {
    fb_ptr[i + 0] = 0x00;
    fb_ptr[i + 1] = 0x00;
    fb_ptr[i + 2] = 0x00;
    fb_ptr[i + 3] = 0xFF;
    fb_ptr[i + 4] = 0xFF;
    fb_ptr[i + 5] = 0xFF;
    fb_ptr[i + 6] = 0x06;
    fb_ptr[i + 7] = 0x02;
    fb_ptr[i + 8] = 0x40;
  }

  pos = getBufferPosition(49);
  // 49th pixel green
  for (int i = pos + 72; i < pos + 136; i += 9) {
    fb_ptr[i + 0] = 0x00;
    fb_ptr[i + 1] = 0x00;
    fb_ptr[i + 2] = 0x00;
    fb_ptr[i + 3] = 0xFF;
    fb_ptr[i + 4] = 0xFF;
    fb_ptr[i + 5] = 0xFF;
    fb_ptr[i + 6] = 0x06;
    fb_ptr[i + 7] = 0x02;
    fb_ptr[i + 8] = 0x40;
  }

  // 48th red
  pos = getBufferPosition(48);
  for (int i = 0 + pos; i < pos + 64; i += 9) {
    fb_ptr[i + 0] = 0x00;
    fb_ptr[i + 1] = 0x00;
    fb_ptr[i + 2] = 0x00;
    fb_ptr[i + 3] = 0xFF;
    fb_ptr[i + 4] = 0xFF;
    fb_ptr[i + 5] = 0xFF;
    fb_ptr[i + 6] = 0x06;
    fb_ptr[i + 7] = 0x02;
    fb_ptr[i + 8] = 0x40;
  }

  // 47th pixel blue
  pos = getBufferPosition(47);
  for (int i = pos + 144; i < pos + 216; i += 9) {
    fb_ptr[i + 0] = 0x00;
    fb_ptr[i + 1] = 0x00;
    fb_ptr[i + 2] = 0x00;
    fb_ptr[i + 3] = 0xFF;
    fb_ptr[i + 4] = 0xFF;
    fb_ptr[i + 5] = 0xFF;
    fb_ptr[i + 6] = 0x06;
    fb_ptr[i + 7] = 0x02;
    fb_ptr[i + 8] = 0x40;
  }

  for (size_t i = 0; i < 1080; i++) {
    printf("%02X ", fb_ptr[i]);
    if ((i + 1) % 9 == 0) {
      printf("\n");
    }

    if ((i + 1) % 216 == 0) {
      printf("- - %ld - \n", i);
    }
  }
  printf("\n");

  displayIt();
  sleep(10);

  destroy();

  return 0;
}
