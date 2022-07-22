package app.nush.examclock.utils;

import app.nush.examclock.ExamClock;

import java.util.Timer;
import java.util.TimerTask;

public class ApplicationLoop implements Runnable {
    private static final long MINIMAL_DELAY = (long) (1e9 / 240);
    private final Timer timer = new Timer();
    private final ExamClock examClock;

    private long period = 16;
    private TimerTask task;

    public ApplicationLoop(ExamClock examClock) {
        this.examClock = examClock;
    }

    private long lastTask = 0;

    @Override
    public void run() {
        if (-(lastTask - (lastTask = System.nanoTime())) < MINIMAL_DELAY) return;
        examClock.getFace().repaint();
        examClock.getList().repaint();
    }

    public void start() {
        stop();
        timer.scheduleAtFixedRate(task = getTask(), period, period);
    }

    public void stop() {
        if (task != null) task.cancel();
    }

    public TimerTask getTask() {
        return new TimerTask() {
            @Override
            public void run() {
                ApplicationLoop.this.run();
            }
        };
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
        start();
    }
}
