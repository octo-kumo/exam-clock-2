package app.nush.examclock.utils;

import app.nush.examclock.ExamClock;

import java.util.Timer;
import java.util.TimerTask;

public class ApplicationLoop implements Runnable {
    private final Timer timer = new Timer();
    private final ExamClock examClock;

    private long period = 16;
    private TimerTask task;

    public ApplicationLoop(ExamClock examClock) {
        this.examClock = examClock;
    }


    @Override
    public void run() {
        examClock.getFace().repaint();
    }

    public void start() {
        stop();
        timer.scheduleAtFixedRate(task = getTask(), 0, period);
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
    }
}
