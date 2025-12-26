package io.github.mores;

import java.nio.ByteBuffer;

public class ColorBufferWriter {

    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ColorBufferWriter.class);

    private static final int UNIT_SIZE = 9;
    private static final int UNITS_PER_CHANNEL = 8;

    public static void writeRgb888(ByteBuffer buffer, int rgb888, int port) {

        int mask = 0X00FFFFFF;
        log.info("writeRgb888: {} {}", port, String.format("0x%06x", rgb888 & mask));

        if (buffer.remaining() < 216) {
            throw new IllegalArgumentException("Buffer must have at least 216 bytes remaining");
        }

        if (port < 1 || port > 4) {
            throw new IllegalArgumentException("Port must be 1,2,3, or 4");
        }

        int r = (rgb888 >> 16) & 0xFF;
        int g = (rgb888 >> 8) & 0xFF;
        int b = rgb888 & 0xFF;

        writeChannel(buffer, r, port);
        writeChannel(buffer, g, port);
        writeChannel(buffer, b, port);
    }

    private static void writeChannel(ByteBuffer buffer, int channelValue, int port) {

        int activeUnits = (channelValue * UNITS_PER_CHANNEL + 127) / 255;

        for (int unit = 0; unit < UNITS_PER_CHANNEL; unit++) {

            int base = buffer.position();

            // Bytes 0–2: always 0 (safe to overwrite)
            buffer.put((byte) 0x00);
            buffer.put((byte) 0x00);
            buffer.put((byte) 0x00);

            // Bytes 3–5: always FF (safe to overwrite)
            buffer.put((byte) 0xFF);
            buffer.put((byte) 0xFF);
            buffer.put((byte) 0xFF);

            byte bitPos1 = 0x00;
            byte bitPos2 = 0x00;
            byte bitPos3 = 0x00;

            switch (port) {
                case 1 -> bitPos1 = 0x02;
                case 2 -> bitPos1 = 0x04;
                case 3 -> bitPos2 = 0x02;
                case 4 -> bitPos3 = 0x40;
            }

            boolean active = unit >= (UNITS_PER_CHANNEL - activeUnits);

            // --- Byte 6 ---
            byte existing6 = buffer.get(base + 6);
            buffer.put(base + 6, active ? (byte) (existing6 | bitPos1) : existing6);

            // --- Byte 7 ---
            byte existing7 = buffer.get(base + 7);
            buffer.put(base + 7, active ? (byte) (existing7 | bitPos2) : existing7);

            // --- Byte 8 ---
            byte existing8 = buffer.get(base + 8);
            buffer.put(base + 8, active ? (byte) (existing8 | bitPos3) : existing8);

            // Move position to end of unit
            buffer.position(base + UNIT_SIZE);
        }
    }
}
