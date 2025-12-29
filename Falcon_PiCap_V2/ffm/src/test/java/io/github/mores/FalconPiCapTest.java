package io.github.mores;

import org.junit.Test;

public class FalconPiCapTest {
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FalconPiCapTest.class);

    // java.awt.Color not tuned for LEDs
    // using these until a good reference / library can be found
    public static final int AZURE = 0xF0FFFF;
    public static final int BLACK = 0x000000;
    public static final int BLUE = 0x0000FF;
    public static final int CHARTREUSE = 0xE9FF00;
    public static final int CYAN = 0x00CCCC;
    public static final int GREEN = 0x00AA00;
    public static final int MAGENTA = 0xFF00FF;
    public static final int ORANGE = 0xFF6600;
    public static final int PINK = 0xFF66CC;
    public static final int RED = 0xFF0000;
    public static final int SPRING_GREEN = 0x00FF7F;
    public static final int VIOLET = 0x7F00FF;
    public static final int YELLOW = 0xFFCC00;
    public static final int WHITE = 0xFFFFFF;

    @Test
    public void testOne() {
        log.info("testOne");

        try {

            try (java.lang.foreign.Arena libraryArena = java.lang.foreign.Arena.ofConfined()) {
                FalconPiCap piCap = new FalconPiCap(libraryArena);

                for (int port = 1; port <= 4; port++) {
                    piCap.setPixelDisplay(port, 0, RED);
                    Thread.sleep(500);

                    piCap.setPixelDisplay(port, 1, GREEN);
                    Thread.sleep(500);

                    piCap.setPixelDisplay(port, 2, BLUE);
                    Thread.sleep(500);

                    piCap.setPixelDisplay(port, 3, CYAN);
                    Thread.sleep(500);

                    piCap.setPixelDisplay(port, 4, MAGENTA);
                    Thread.sleep(500);

                    piCap.setPixelDisplay(port, 5, ORANGE);
                    Thread.sleep(500);

                    piCap.setPixelDisplay(port, 6, PINK);
                    Thread.sleep(500);

                    piCap.setPixelDisplay(port, 7, YELLOW);
                    Thread.sleep(500);

                    piCap.setPixelDisplay(port, 8, WHITE);
                    Thread.sleep(500);
                }

                for (int port = 1; port <= 4; port++) {
                    for (int pixel = 0; pixel <= 8; pixel++) {
                        piCap.setPixelDisplay(port, pixel, BLACK);
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

                piCap.setPixelDisplay(1, 0, RED);
                piCap.setPixelDisplay(2, 0, GREEN);
                piCap.setPixelDisplay(3, 0, BLUE);
                piCap.setPixelDisplay(4, 0, WHITE);

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

        java.util.List<Integer> colors = new java.util.ArrayList<>();
        colors.add(RED);
        colors.add(GREEN);
        colors.add(BLUE);
        colors.add(CYAN);
        colors.add(MAGENTA);
        colors.add(ORANGE);
        colors.add(PINK);
        colors.add(YELLOW);
        colors.add(WHITE);

        try {

            java.util.Random random = new java.util.Random();

            try (java.lang.foreign.Arena libraryArena = java.lang.foreign.Arena.ofConfined()) {
                FalconPiCap piCap = new FalconPiCap(libraryArena);

                for (int x = 0; x <= 10; x++) {
                    piCap.setPixelDisplay(1, x, RED);

                    int color = colors.get(random.nextInt(colors.size()));
                    piCap.setPixelDisplay(2, 0, color);
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
                java.util.List<Integer> gradient = getGradient(GREEN, BLUE, 255);

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
        log.info("testFive");

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

    @Test
    public void testSix() {
        log.info("testSix");

        try {

            try (java.lang.foreign.Arena libraryArena = java.lang.foreign.Arena.ofConfined()) {
                FalconPiCap piCap = new FalconPiCap(libraryArena);

                countdown(5);

                int begin = 0;
                int end = begin + 49;

                for (int i = begin; i <= end; i++) {
                    log.info("I: " + i);
                    piCap.setPixelDisplay(1, i, 0xff0000);

                    // 50 turn green
                    Thread.sleep(500);
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
