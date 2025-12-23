# apt install libbcm2835-dev

CC = gcc

CFLAGS = -Wall -g -I/usr/include/libdrm
PICFLAGS = -fPIC

LIBS = -ldrm -lbcm2835

# Targets
APP = pixel
SHLIB = libfalcon_picap.so

# Objects
APP_OBJS = main.o
LIB_OBJS = drm.o

# Default target
all: $(SHLIB) $(APP)

# Build the shared library
$(SHLIB): $(LIB_OBJS)
	$(CC) -shared -o $@ $(LIB_OBJS) $(LIBS)

# Build the application, linking against the shared library
$(APP): $(APP_OBJS)
	$(CC) -o $@ $(APP_OBJS) -L. -lfalcon_picap -Wl,-rpath,'$$ORIGIN' $(LIBS)

# Compile drm.c as PIC for shared library
drm.o: drm.c
	$(CC) $(CFLAGS) $(PICFLAGS) -c $< -o $@

# Compile main.c normally
main.o: main.c
	$(CC) $(CFLAGS) -c $< -o $@

.PHONY: clean
clean:
	rm -f $(APP) $(SHLIB) *.o
