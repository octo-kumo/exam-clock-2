package app.nush.examclock.utils;

import app.nush.examclock.Context;
import app.nush.examclock.ExamClock;

import java.awt.*;

public class ApplicationLoop implements Runnable {
    private static final long DISPLAY_FPS = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDisplayMode().getRefreshRate();
    private final ExamClock examClock;
    private long period = (long) (1e9 / Context.fps.get());
    private Thread thread;

    public ApplicationLoop(ExamClock examClock) {
        this.examClock = examClock;
        Context.fps.listen((n, o) -> period = (long) (1e9 / n));
    }

    private long counter = 0;

    @Override
    public void run() {
        while (true) {
            examClock.getFace().repaint();
            if (Context.quality.get() || (counter++) % 10 == 0) examClock.getList().repaint();
            long e = examClock.getFace().getRenderTime();
            try {
                if (Context.optimizeFPS.get()) {
                    int fps = getFPS();
                    if (e > period * 0.9 && fps > 10) setFPS(fps - 1);
                    if (e < period * 0.2 && fps < DISPLAY_FPS * 1000) setFPS(fps + 1);
                }
                Thread.sleep(Math.abs((period - e) / 1_000_000));
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void start() {
        stop();
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        if (thread != null) thread.interrupt();
    }

    public void setFPS(int fps) {
        Context.fps.set(fps);
    }

    public int getFPS() {
        return Context.fps.get();
    }
}
