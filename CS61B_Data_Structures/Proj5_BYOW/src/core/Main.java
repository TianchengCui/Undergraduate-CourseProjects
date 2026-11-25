package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;

public class Main {
    private static final int WIDTH = 70;
    private static final int HEIGHT = 35;
    //private static final long SEED = 2873123L;
    private static final long SEED = 1881354388786084789L;

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.start();
    }
}
