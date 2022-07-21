package app.nush.examclock;

import app.nush.examclock.components.ClockFace;
import app.nush.examclock.components.ClockMenu;
import app.nush.examclock.components.ExamList;
import app.nush.examclock.utils.ApplicationLoop;
import app.nush.examclock.utils.Fonts;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.awt.*;

public class ExamClock extends JFrame implements Config {
    private final ExamList list;
    private final ClockMenu menu;
    private final ClockFace face;
    private boolean dark = true;

    public ExamClock() {
        super("Exam Clock");
        setPreferredSize(new Dimension(1280, 720));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setJMenuBar(menu = new ClockMenu(this));
        add(face = new ClockFace(this), BorderLayout.CENTER);
        add(list = new ExamList(this), BorderLayout.EAST);
    }

    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        Fonts.loadFonts();

        ExamClock examClock = new ExamClock();
        ApplicationLoop loop = new ApplicationLoop(examClock);
        examClock.pack();
        examClock.setLocationRelativeTo(null);
        examClock.setVisible(true);
        loop.start();
    }

    public ExamList getList() {
        return list;
    }

    public ClockMenu getMenu() {
        return menu;
    }

    public ClockFace getFace() {
        return face;
    }

    @Override
    public boolean isDark() {
        return dark;
    }

    @Override
    public boolean setDark(boolean value) {
        return dark = value;
    }
}
