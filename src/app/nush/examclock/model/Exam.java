package app.nush.examclock.model;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;

public class Exam {
    @Expose
    public LocalDateTime startTime;
    @Expose
    public LocalDateTime endTime;

    @Expose
    public String name;
    @Expose
    public String desc;

    public Exam(String name, String desc, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.desc = desc;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
