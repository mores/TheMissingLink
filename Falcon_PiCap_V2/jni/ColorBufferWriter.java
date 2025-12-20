import java.nio.ByteBuffer;

public class ColorBufferWriter {

    private static final int UNIT_SIZE = 9;
    private static final int UNITS_PER_CHANNEL = 8;

    public static void writeRgb888(ByteBuffer buffer, int rgb888) {
        if (buffer.remaining() < 216) {
            throw new IllegalArgumentException("Buffer must have at least 216 bytes remaining");
        }

        int r = (rgb888 >> 16) & 0xFF;
        int g = (rgb888 >> 8) & 0xFF;
        int b = rgb888 & 0xFF;

        writeChannel(buffer, r);
        writeChannel(buffer, g);
        writeChannel(buffer, b);
    }

    private static void writeChannel(ByteBuffer buffer, int channelValue) {
        int activeUnits = (channelValue * UNITS_PER_CHANNEL + 127) / 255;

        for (int unit = 0; unit < UNITS_PER_CHANNEL; unit++) {

            // Bytes 0–2: always 0
            buffer.put((byte) 0x00);
            buffer.put((byte) 0x00);
            buffer.put((byte) 0x00);

            // Bytes 3–5: always FF
            buffer.put((byte) 0xFF);
            buffer.put((byte) 0xFF);
            buffer.put((byte) 0xFF);

            // Byte 6: bottom-up ordering
            buffer.put(unit >= (UNITS_PER_CHANNEL - activeUnits)
                    ? (byte) 0x02
                    : (byte) 0x00);

            // Bytes 7–8: always 0
            buffer.put((byte) 0x00);
            buffer.put((byte) 0x00);
        }
    }
}
