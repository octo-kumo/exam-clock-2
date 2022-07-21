package app.nush.examclock.components;

import app.nush.examclock.ExamClock;
import app.nush.examclock.model.Exam;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;

public class ExamList extends JScrollPane {
    private final Box list;
    private final ArrayList<Exam> exams;

    public ExamList(ExamClock examClock) {
        super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);
        setViewportView(list = new Box(BoxLayout.PAGE_AXIS));
        exams = new ArrayList<>();
        add(new Exam("CS6132", "Computer Science with alot of stuff and stuff awiidnwdnwoidnwoin wdwd", LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
    }

    public boolean add(Exam exam) {
        boolean success = exams.add(exam);
        if (success) list.add(new ExamHolder(exam));
        return success;
    }

    public boolean remove(Object o) {
        int i = exams.indexOf(o);
        boolean success = exams.remove(o);
        if (success) list.remove(i);
        return success;
    }

    public boolean addAll(Collection<? extends Exam> c) {
        for (Exam exam : c) list.add(new ExamHolder(exam));
        return exams.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends Exam> c) {
        int i = 0;
        for (Exam exam : c) list.add(new ExamHolder(exam), index + (i++));
        return exams.addAll(index, c);
    }

    public boolean removeAll(Collection<?> c) {
        c.forEach(e -> {
            int i = exams.indexOf(e);
            if (i == -1) return;
            list.remove(i);
            exams.remove(i);
        });
        return true;
    }

    public void sort(Comparator<? super Exam> c) {
        exams.sort(c);

        Component[] components = list.getComponents();
        list.removeAll();
        Arrays.sort(components, (a, b) -> c.compare(((ExamHolder) a).getExam(), ((ExamHolder) b).getExam()));
        for (Component component : components) list.add(component);
    }

    public void clear() {
        exams.clear();
        list.removeAll();
    }

    public Exam get(int index) {
        return exams.get(index);
    }

    public Exam set(int index, Exam element) {
        Objects.checkIndex(index, exams.size());
        return ((ExamHolder) list.getComponents()[index]).setExam(exams.set(index, element));
    }

    public void add(int index, Exam element) {
        exams.add(index, element);
        list.add(new ExamHolder(element), index);
    }

    public int indexOf(Object o) {
        return exams.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return exams.lastIndexOf(o);
    }
}
