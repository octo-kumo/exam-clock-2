package app.nush.examclock.utils;

import app.nush.examclock.Context;
import app.nush.examclock.ExamClock;

import java.awt.*;

public class ApplicationLoop implements Runnable {
    private static final long DISPLAY_FPS = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDisplayMode().getRefreshRate();
    private static final long MINIMAL_DELAY = (long) (1e9 / 240);
    private final ExamClock examClock;
    private double fps = 30;
    private long period = (long) (1e9 / fps);
    private Thread thread;

    public ApplicationLoop(ExamClock examClock) {
        this.examClock = examClock;
    }

    private long lastTask = 0;
    private long counter = 0;

    @Override
    public void run() {
        while (true) {
            long s = System.nanoTime();
            Rectangle b1 = examClock.getFace().getBounds(), b2 = examClock.getList().getBounds();
            b1.setLocation(0, 0);
            b2.setLocation(0, 0);
            examClock.getFace().paintImmediately(b1);
            if (Context.quality.get() || (counter++) % ((int) fps / 2) == 0)
                examClock.getList().paintImmediately(b2);
            long e = System.nanoTime() - s;
            try {
                if (e > period * 0.9 && fps > 10) setFPS(fps - 1);
                if (e < period * 0.2 && fps < DISPLAY_FPS * 1000) setFPS(fps + 1);
                Thread.sleep(Math.abs((period - e) / 1_000_000));
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void start() {
        stop();
        lastTask = 0;
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        if (thread != null) thread.interrupt();
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public void setFPS(double fps) {
        this.fps = fps;
        setPeriod((long) (1e9 / fps));
    }

    public double getFPS() {
        return fps;
    }
}
