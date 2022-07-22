package app.nush.examclock.components;

import app.nush.examclock.ExamClock;
import app.nush.examclock.model.Exam;
import app.nush.examclock.windows.ExamEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.*;

public class ExamList extends JScrollPane {
    private final Box list;
    private final ArrayList<Exam> exams;

    public ExamList(ExamClock examClock) {
        super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);
        setViewportView(list = new Box(BoxLayout.PAGE_AXIS));

        exams = new ArrayList<>();

        setComponentPopupMenu(new JPopupMenu() {{
            add(new JMenuItem("Add") {{
                addActionListener(ExamList.this::add);
            }});
            add(new JMenuItem("Sort") {{
                addActionListener(ExamList.this::sort);
            }});
        }});

        add(new Exam("MA2131", "Mathematics I", LocalDateTime.now(), LocalDateTime.now().plusMinutes(2)));
        add(new Exam("CS6132", "Computer Science with alot of stuff and stuff awiidnwdnwoidnwoin wdwd", LocalDateTime.now(), LocalDateTime.now().plusMinutes(1)));
    }

    protected void add(ActionEvent e) {
        Exam exam = new Exam("", "", LocalDateTime.now(), LocalDateTime.now());
        ExamEditor examEditor = new ExamEditor(exam, ExamEditor.EditDialogType.CREATE, SwingUtilities.getWindowAncestor(this));
        examEditor.setVisible(true);
        if (examEditor.getChoice() == ExamEditor.UserChoice.OK) add(exam);
    }

    protected void sort(ActionEvent e) {
        sort(Comparator.comparing(exam -> exam.startTime));
    }

    public boolean add(Exam exam) {
        boolean success = exams.add(exam);
        if (success) list.add(new ExamHolder(exam));
        list.revalidate();
        return success;
    }

    public void remove(Object o) {
        int i = exams.indexOf(o);
        boolean success = exams.remove(o);
        if (success) list.remove(i);
        list.revalidate();
    }

    public void addAll(Collection<? extends Exam> c) {
        for (Exam exam : c) add(exam);
    }

    public boolean addAll(int index, Collection<? extends Exam> c) {
        int i = 0;
        for (Exam exam : c) list.add(new ExamHolder(exam), index + (i++));
        return exams.addAll(index, c);
    }

    public void removeAll(Collection<?> c) {
        c.forEach(this::remove);
    }

    public void sort(Comparator<? super Exam> c) {
        exams.sort(c);
        Component[] components = list.getComponents();
        if (components.length != exams.size()) throw new RuntimeException("Mismatch!");
        for (int i = 0; i < components.length; i++) ((ExamHolder) components[i]).setExam(exams.get(i));
    }

    public void clear() {
        exams.clear();
        list.removeAll();
    }

    public Exam get(int index) {
        if (index < 0 || index >= exams.size()) throw new IndexOutOfBoundsException("Index out of range: " + index);
        return exams.get(index);
    }

    public Exam set(int index, Exam element) {
        if (index < 0 || index >= exams.size()) throw new IndexOutOfBoundsException("Index out of range: " + index);
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
