#ifndef DRM_H
#define DRM_H

#include <stdint.h>

uint64_t bufferSize();

void destroy();

int displayIt();

void disableDisplay();

void enableDisplay();

int getBufferPosition( int position );

uint8_t* init();

void off();

#endif // FUNCTIONS_H
