package app.nush.examclock;

import app.nush.examclock.utils.Fonts;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;

public class ExamClock extends JFrame {
    public ExamClock() {
        super("Exam Clock");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        Fonts.loadFonts();

        ExamClock examClock = new ExamClock();
        examClock.pack();
        examClock.setLocationRelativeTo(null);
        examClock.setVisible(true);
    }
}
