package io.github.mores;

import org.junit.Test;

public class FalconPiCapTest {
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FalconPiCapTest.class);

    @Test
    public void testOne() {
        log.info("testOne");

        try {

            try (java.lang.foreign.Arena libraryArena = java.lang.foreign.Arena.ofConfined()) {
                FalconPiCap piCap = new FalconPiCap(libraryArena);

                for (int port = 1; port <= 4; port++) {
                    piCap.setPixelDisplay(port, 0, java.awt.Color.RED.getRGB());
                    Thread.sleep(500);

                    piCap.setPixelDisplay(port, 1, java.awt.Color.GREEN.getRGB());
                    Thread.sleep(500);

                    piCap.setPixelDisplay(port, 2, java.awt.Color.BLUE.getRGB());
                    Thread.sleep(500);

                    piCap.setPixelDisplay(port, 3, java.awt.Color.CYAN.getRGB());
                    Thread.sleep(500);

                    piCap.setPixelDisplay(port, 4, java.awt.Color.MAGENTA.getRGB());
                    Thread.sleep(500);

                    piCap.setPixelDisplay(port, 5, java.awt.Color.ORANGE.getRGB());
                    Thread.sleep(500);

                    piCap.setPixelDisplay(port, 6, java.awt.Color.PINK.getRGB());
                    Thread.sleep(500);

                    piCap.setPixelDisplay(port, 7, java.awt.Color.YELLOW.getRGB());
                    Thread.sleep(500);

                    piCap.setPixelDisplay(port, 8, java.awt.Color.WHITE.getRGB());
                    Thread.sleep(500);
                }

                for (int port = 1; port <= 4; port++) {
                    for (int pixel = 0; pixel <= 8; pixel++) {
                        piCap.setPixelDisplay(port, pixel, java.awt.Color.BLACK.getRGB());
                        Thread.sleep(500);
                    }

                }

                piCap.destroy();
            }

        } catch (Throwable e) {
            log.error("Error: ", e);
        }
    }

    @Test
    public void testTwo() {
        log.info("testTwo");

        try {

            try (java.lang.foreign.Arena libraryArena = java.lang.foreign.Arena.ofConfined()) {
                FalconPiCap piCap = new FalconPiCap(libraryArena);

                piCap.setPixelDisplay(1, 0, java.awt.Color.RED.getRGB());
                piCap.setPixelDisplay(2, 0, java.awt.Color.GREEN.getRGB());
                piCap.setPixelDisplay(3, 0, java.awt.Color.BLUE.getRGB());
                piCap.setPixelDisplay(4, 0, java.awt.Color.WHITE.getRGB());

                Thread.sleep(5000);

                piCap.destroy();
            }

        } catch (Throwable e) {
            log.error("Error: ", e);
        }
    }

    @Test
    public void testThree() {
        log.info("testThree");

        java.util.List<java.awt.Color> colors = new java.util.ArrayList<>();
        colors.add(java.awt.Color.RED);
        colors.add(java.awt.Color.GREEN);
        colors.add(java.awt.Color.BLUE);
        colors.add(java.awt.Color.CYAN);
        colors.add(java.awt.Color.MAGENTA);
        colors.add(java.awt.Color.ORANGE);
        colors.add(java.awt.Color.PINK);
        colors.add(java.awt.Color.YELLOW);
        colors.add(java.awt.Color.WHITE);

        try {

            java.util.Random random = new java.util.Random();

            try (java.lang.foreign.Arena libraryArena = java.lang.foreign.Arena.ofConfined()) {
                FalconPiCap piCap = new FalconPiCap(libraryArena);

                for (int x = 0; x <= 10; x++) {
                    piCap.setPixelDisplay(1, x, java.awt.Color.RED.getRGB());

                    java.awt.Color color = colors.get(random.nextInt(colors.size()));
                    piCap.setPixelDisplay(2, 0, color.getRGB());
                    Thread.sleep(1000);
                }

                piCap.destroy();
            }
        } catch (Throwable e) {
            log.error("Error: ", e);
        }
    }

    @Test
    public void testFour() {
        log.info("testFour");

        try {

            try (java.lang.foreign.Arena libraryArena = java.lang.foreign.Arena.ofConfined()) {
                FalconPiCap piCap = new FalconPiCap(libraryArena);

                countdown(5);
                java.util.List<Integer> gradient = getGradient(java.awt.Color.GREEN.getRGB(),
                        java.awt.Color.BLUE.getRGB(), 255);

                long lastTime = System.nanoTime();
                long targetFrameDuration = 50_000_000; // ~20 FPS
                // long targetFrameDuration = 25_000_000; // ~40 FPS
                // long targetFrameDuration = 16_000_000; // ~60 FPS

                log.info("Start");
                int count = 0;
                while (count < gradient.size()) {
                    long now = System.nanoTime();
                    long elapsed = now - lastTime;

                    long timeToWait = targetFrameDuration - elapsed;

                    if (timeToWait > 0) {
                        try {
                            Thread.sleep(timeToWait / 1_000_000, (int) (timeToWait % 1_000_000));
                        } catch (Exception e) {
                        }

                    }

                    piCap.setPixelDisplay(3, 0, gradient.get(count));
                    count = count + 1;

                    lastTime = System.nanoTime();
                }
                log.info("End");

                piCap.destroy();
            }
        } catch (Throwable e) {
            log.error("Error: ", e);
        }
    }

    @Test
    public void testFive() {
        log.info("testFour");

        try {

            try (java.lang.foreign.Arena libraryArena = java.lang.foreign.Arena.ofConfined()) {
                FalconPiCap piCap = new FalconPiCap(libraryArena);

                countdown(5);

                for (int i = 0; i <= 255; i++) {
                    log.info("I: " + i);
                    piCap.setPixelDisplay(3, 0, wheel(i));
                    Thread.sleep(50);
                }

                piCap.destroy();
            }
        } catch (Throwable e) {
            log.error("Error: ", e);
        }
    }

    private void countdown(int size) throws Exception {
        for (int x = size; x > 0; x--) {
            System.out.println(x);
            Thread.sleep(1000);
        }
    }

    private java.util.List<Integer> getGradient(int rgb888begin, int rgb888end, int steps) {
        java.util.List<Integer> gradient = new java.util.ArrayList<>(steps);

        if (steps <= 1) {
            gradient.add(rgb888begin);
            return gradient;
        }

        int r1 = (rgb888begin >> 16) & 0xFF;
        int g1 = (rgb888begin >> 8) & 0xFF;
        int b1 = rgb888begin & 0xFF;

        int r2 = (rgb888end >> 16) & 0xFF;
        int g2 = (rgb888end >> 8) & 0xFF;
        int b2 = rgb888end & 0xFF;

        for (int i = 0; i < steps; i++) {
            float t = i / (float) (steps - 1); // 0.0 → 1.0

            int r = (int) (r2 * t + r1 * (1 - t));
            int g = (int) (g2 * t + g1 * (1 - t));
            int b = (int) (b2 * t + b1 * (1 - t));

            gradient.add((r << 16) | (g << 8) | b);
        }

        return gradient;
    }

    private int wheel(int wheelPos) {
        int Color_COMPONENT_MAX = 0xFF;

        int max = Color_COMPONENT_MAX;
        int one_third = Color_COMPONENT_MAX / 3;
        int two_thirds = Color_COMPONENT_MAX * 2 / 3;

        int wheel_pos = max - wheelPos;
        if (wheel_pos < one_third) {
            return createColorRGB(max - wheel_pos * 3, 0, wheel_pos * 3);
        }
        if (wheel_pos < two_thirds) {
            wheel_pos -= one_third;
            return createColorRGB(0, wheel_pos * 3, max - wheel_pos * 3);
        }
        wheel_pos -= two_thirds;
        return createColorRGB(wheel_pos * 3, max - wheel_pos * 3, 0);
    }

    private int createColorRGB(int red, int green, int blue) {
        return (red << 16) | (green << 8) | blue;
    }

}
