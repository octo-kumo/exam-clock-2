package app.nush.examclock;

import app.nush.examclock.components.ClockFace;
import app.nush.examclock.components.ClockMenu;
import app.nush.examclock.components.ExamList;
import app.nush.examclock.utils.ApplicationLoop;
import app.nush.examclock.utils.Fonts;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class ExamClock extends JFrame implements Context {
    public static ApplicationLoop loop;
    private final ExamList list;
    private final ClockMenu menu;
    private final ClockFace face;

    public ExamClock() {
        super("Exam Clock");
        setPreferredSize(new Dimension(1280, 720));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setJMenuBar(menu = new ClockMenu(this));
        add(face = new ClockFace(this), BorderLayout.CENTER);
        add(list = new ExamList(this), BorderLayout.EAST);

        try {
            setIconImage(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/icons/appventure_logo_nobg.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Context.quality.listen((n, o) -> {
            SwingUtilities.updateComponentTreeUI(this);
            revalidate();
            repaint();
        });
        Context.dark.listen((n, o) -> {
            if (n) FlatDarkLaf.setup();
            else FlatLightLaf.setup();
            FlatLaf.updateUI();
        });
    }

    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        Fonts.loadFonts();
//        Toolkit.getDefaultToolkit().setDynamicLayout(false);

        ExamClock examClock = new ExamClock();
        loop = new ApplicationLoop(examClock);
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
}
