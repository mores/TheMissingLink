package io.github.mores;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.foreign.FunctionDescriptor;
import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_LONG;

public class FalconPiCap {

    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FalconPiCap.class);

    private Linker linker;
    private SymbolLookup customLibrary;

    private java.nio.ByteBuffer buffer;

    public FalconPiCap(Arena libraryArena) throws Throwable {

        linker = Linker.nativeLinker();

        customLibrary = SymbolLookup.libraryLookup("libfalcon_picap.so", libraryArena);
        log.info("customLibrary: " + customLibrary);

        init(libraryArena);
    }

    public void destroy() throws Throwable {

        buffer = null;
        System.gc();

        MemorySegment destroySymbol = customLibrary.find("destroy")
                .orElseThrow(() -> new RuntimeException("Destroy not found"));
        FunctionDescriptor functionDesc = FunctionDescriptor.ofVoid();
        MethodHandle methodHandle = linker.downcallHandle(destroySymbol, functionDesc);
        methodHandle.invokeExact();
    }

    public void displayIt() throws Throwable {

        MemorySegment displayItSymbol = customLibrary.find("displayIt")
                .orElseThrow(() -> new RuntimeException("DisplayIt not found"));
        MethodHandle methodHandle = linker.downcallHandle(displayItSymbol, FunctionDescriptor.of(ValueLayout.JAVA_INT));
    }

    public void dumpBuffer() {

        int lastPosition = buffer.position();
        buffer.position(0);

        for (int x = 0; x < 1080; x++) {
            if (x % 9 == 0) {
                System.out.println();
            }

            if (x % 216 == 0) {
                System.out.println("- - - " + x + " - - - ");
            }

            String hex = String.format("%02x ", Byte.toUnsignedInt(buffer.get()));
            System.out.print(hex);

        }

        buffer.position(lastPosition);
    }

    private void init(Arena libraryArena) throws Throwable {

        MemorySegment initSymbol = customLibrary.find("init")
                .orElseThrow(() -> new RuntimeException("Cannot find init"));
        MethodHandle initHandle = linker.downcallHandle(initSymbol, FunctionDescriptor.of(ADDRESS));
        MemorySegment segment = (MemorySegment) initHandle.invokeExact();

        long size = getBufferSize();

        MemorySegment sizedSegment = segment.reinterpret(size, libraryArena, null);
        buffer = sizedSegment.asByteBuffer();
    }

    private long getBufferSize() throws Throwable {

        MethodHandle bufferSizeHandle = customLibrary.find("bufferSize")
                .map(symbol -> linker.downcallHandle(symbol, FunctionDescriptor.of(ValueLayout.JAVA_LONG) // Map
                                                                                                          // uint64_t to
                                                                                                          // Java long
                )).orElseThrow(() -> new NoSuchMethodException("Native function bufferSize not found"));
        long size = (long) bufferSizeHandle.invokeExact();
        System.out.println("size of buffer: " + size);

        return size;
    }

    public void setPixel(int port, int index, int rgb888) {

        int position = 216 * index;
        buffer.position(position);
        ColorBufferWriter.writeRgb888(buffer, rgb888, port);
    }

    public void setPixelDisplay(int port, int index, int rgb888) throws Throwable {
        setPixel(port, index, rgb888);
        displayIt();
    }
}
