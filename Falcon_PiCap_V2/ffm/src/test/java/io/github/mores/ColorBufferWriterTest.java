package io.github.mores;

import org.junit.Test;

public class ColorBufferWriterTest {

    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ColorBufferWriterTest.class);

    private java.nio.ByteBuffer allOff;

    public ColorBufferWriterTest() {
        allOff = java.nio.ByteBuffer.allocate(216);
        fillBufferWithPattern(allOff, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00);
    }

    @Test
    public void testRedOne() {

        log.info("Off");
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(216);
        fillBufferWithPattern(buffer, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00);
        dumpBuffer(buffer);
        buffer.position(0);

        log.info("Port 1 - RED");
        ColorBufferWriter.writeRgb888(buffer, java.awt.Color.RED.getRGB(), 1);
        dumpBuffer(buffer);
        buffer.position(0);

        log.info("Port 1 - OFF");
        ColorBufferWriter.writeRgb888(buffer, java.awt.Color.BLACK.getRGB(), 1);
        dumpBuffer(buffer);
        buffer.position(0);

        org.junit.Assert.assertTrue(buffer.equals(allOff));
    }

    @Test
    public void testGreenTwo() {

        log.info("Off");
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(216);
        fillBufferWithPattern(buffer, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00);
        dumpBuffer(buffer);
        buffer.position(0);

        log.info("Port 2 - GREEN");
        ColorBufferWriter.writeRgb888(buffer, java.awt.Color.GREEN.getRGB(), 2);
        dumpBuffer(buffer);
        buffer.position(0);

        log.info("Port 2 - OFF");
        ColorBufferWriter.writeRgb888(buffer, java.awt.Color.BLACK.getRGB(), 2);
        dumpBuffer(buffer);
        buffer.position(0);

        org.junit.Assert.assertTrue(buffer.equals(allOff));
    }

    @Test
    public void testBlueThree() {

        log.info("Off");
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(216);
        fillBufferWithPattern(buffer, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00);
        dumpBuffer(buffer);
        buffer.position(0);

        log.info("Port 3 - BLUE");
        ColorBufferWriter.writeRgb888(buffer, java.awt.Color.BLUE.getRGB(), 3);
        dumpBuffer(buffer);
        buffer.position(0);

        log.info("Port 3 - OFF");
        ColorBufferWriter.writeRgb888(buffer, java.awt.Color.BLACK.getRGB(), 3);
        dumpBuffer(buffer);
        buffer.position(0);

        org.junit.Assert.assertTrue(buffer.equals(allOff));
    }

    @Test
    public void testWhiteFour() {

        log.info("Off");
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(216);
        fillBufferWithPattern(buffer, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00);
        dumpBuffer(buffer);
        buffer.position(0);

        log.info("Port 4 - WHITE");
        ColorBufferWriter.writeRgb888(buffer, java.awt.Color.WHITE.getRGB(), 4);
        dumpBuffer(buffer);
        buffer.position(0);

        log.info("Port 4 - RED");
        ColorBufferWriter.writeRgb888(buffer, java.awt.Color.RED.getRGB(), 4);
        dumpBuffer(buffer);
        buffer.position(0);

        log.info("Port 4 - OFF");
        ColorBufferWriter.writeRgb888(buffer, java.awt.Color.BLACK.getRGB(), 4);
        dumpBuffer(buffer);
        buffer.position(0);

        org.junit.Assert.assertTrue(buffer.equals(allOff));
    }

    @Test
    public void testFade() {

        log.info("Off");
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(216);
        fillBufferWithPattern(buffer, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00);
        dumpBuffer(buffer);
        buffer.position(0);

        log.info("Port 1 - RED");
        ColorBufferWriter.writeRgb888(buffer, 0xff0000, 1);
        dumpBuffer(buffer);
        buffer.position(0);

        log.info("Port 1 - LESS RED");
        ColorBufferWriter.writeRgb888(buffer, 0xfe0000, 1);
        dumpBuffer(buffer);
        buffer.position(0);

        log.info("Port 1 - LESS RED");
        ColorBufferWriter.writeRgb888(buffer, 0xfd0000, 1);
        dumpBuffer(buffer);
        buffer.position(0);

        log.info("Port 1 - LESS RED");
        ColorBufferWriter.writeRgb888(buffer, 0xfc0000, 1);
        dumpBuffer(buffer);
        buffer.position(0);

        log.info("Port 1 - OFF");
        ColorBufferWriter.writeRgb888(buffer, java.awt.Color.BLACK.getRGB(), 1);
        dumpBuffer(buffer);
        buffer.position(0);

        org.junit.Assert.assertTrue(buffer.equals(allOff));
    }

    private void dumpBuffer(java.nio.ByteBuffer buffer) {

        if (!log.isTraceEnabled()) {
            return;
        }

        int lastPosition = buffer.position();
        buffer.position(0);

        StringBuilder bitString = new StringBuilder();
        StringBuilder hexString = new StringBuilder();

        for (int x = 0; x < 216; x++) {

            if (x % 9 == 0) {
                System.out.println(hexString.toString() + "\t" + bitString.toString());
                bitString = new StringBuilder("");
                hexString = new StringBuilder("");
            }

            byte aByte = buffer.get();
            String hex = String.format("%02x ", Byte.toUnsignedInt(aByte));
            hexString.append(hex);

            String binaryString = Integer.toBinaryString(aByte & 0xFF);
            bitString.append(String.format("%8s", binaryString).replace(' ', '0'));
        }

        System.out.println(hexString.toString() + "\t" + bitString.toString());

        buffer.position(lastPosition);
    }

    private static void fillBufferWithPattern(java.nio.ByteBuffer targetBuffer, int b1, int b2, int b3, int b4, int b5,
            int b6, int b7, int b8, int b9) {
        // 1. Create a source array with the pattern
        byte[] patternBytes = new byte[] { (byte) b1, (byte) b2, (byte) b3, (byte) b4, (byte) b5, (byte) b6, (byte) b7,
                (byte) b8, (byte) b9 };

        // 2. Wrap the pattern in a source ByteBuffer
        java.nio.ByteBuffer sourcePattern = java.nio.ByteBuffer.wrap(patternBytes);

        // Ensure the target buffer starts at position 0 for filling
        targetBuffer.position(0);

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
