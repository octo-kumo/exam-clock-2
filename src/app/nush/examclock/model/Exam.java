package app.nush.examclock.model;

import java.time.LocalDateTime;

public class Exam {
    public LocalDateTime startTime;
    public LocalDateTime endTime;

    public String name;
    public String desc;

    public Exam(String name, String desc, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.desc = desc;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
