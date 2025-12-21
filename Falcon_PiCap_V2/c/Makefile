# apt install libbcm2835-dev

# Define variables for compiler and flags
CC = gcc
CFLAGS = -Wall -g -I/usr/include/libdrm
TARGET = pixel
OBJECTS = main.o drm.o

# Default target (run when 'make' is typed with no arguments)
all: $(TARGET)

# Rule to link object files into the final executable
$(TARGET): $(OBJECTS)
	$(CC) $(OBJECTS) -o $(TARGET) -ldrm -lbcm2835

# Pattern rule: how to build any .o file from its .c source
%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

# Phony target to clean up generated files
.PHONY: clean
clean:
	rm -f $(TARGET) $(OBJECTS)
