import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class HelloJNI {

    // Declare a native methods
    public native void displayIt();
    public native void sayGoodBye();
    public native void sayHello();

    public native java.nio.ByteBuffer getFrameBuffer();

    static {
        // The library name "hello" will resolve to "hello.dll" on Windows,
        // "libhello.so" on Linux, or "libhello.dylib" on macOS
        System.loadLibrary("hello");
    }

    private static java.nio.ByteBuffer buffer;

    public static void main(String[] args) {

        HelloJNI me = new HelloJNI();

        me.sayHello();

        buffer = me.getFrameBuffer();
        // init off - move to C ??
        fillBufferWithPattern(buffer, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x00);
        me.displayIt();

        ColorBufferWriter.writeRgb888( buffer, java.awt.Color.RED.getRGB() );
        ColorBufferWriter.writeRgb888( buffer, java.awt.Color.BLUE.getRGB() );
        ColorBufferWriter.writeRgb888( buffer, java.awt.Color.CYAN.getRGB() );
        ColorBufferWriter.writeRgb888( buffer, java.awt.Color.GREEN.getRGB() );
        ColorBufferWriter.writeRgb888( buffer, java.awt.Color.MAGENTA.getRGB() );
        ColorBufferWriter.writeRgb888( buffer, java.awt.Color.ORANGE.getRGB() );
        ColorBufferWriter.writeRgb888( buffer, java.awt.Color.PINK.getRGB() );
        ColorBufferWriter.writeRgb888( buffer, java.awt.Color.WHITE.getRGB() );
        ColorBufferWriter.writeRgb888( buffer, java.awt.Color.YELLOW.getRGB() );
        me.displayIt();

        buffer.position( 216 * 49 );
        ColorBufferWriter.writeRgb888( buffer, java.awt.Color.RED.getRGB() );
        me.displayIt();

        me.sayGoodBye();

    }

    private static void fillBufferWithPattern( ByteBuffer targetBuffer, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8, byte b9) {
        // 1. Create a source array with the pattern
        byte[] patternBytes = new byte[]{b1, b2, b3, b4, b5, b6, b7, b8, b9};

        // 2. Wrap the pattern in a source ByteBuffer
        ByteBuffer sourcePattern = ByteBuffer.wrap(patternBytes);

        // Ensure the target buffer starts at position 0 for filling
        targetBuffer.position( 0 );

        // 3. Loop to fill the target buffer efficiently
        while (targetBuffer.remaining() >= sourcePattern.capacity()) {
            // Reset the source buffer's position for each put operation
            sourcePattern.rewind();
            targetBuffer.put(sourcePattern);
        }

        // Handle any remaining bytes if the capacity is not a multiple of the pattern length (3 bytes)
        if (targetBuffer.hasRemaining()) {
            sourcePattern.rewind();
            targetBuffer.put(sourcePattern.array(), 0, targetBuffer.remaining());
        }

        // Optional: Rewind the buffer to prepare for reading
        targetBuffer.rewind();
    }
}
