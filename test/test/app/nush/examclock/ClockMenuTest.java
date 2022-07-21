package test.app.nush.examclock;

import app.nush.examclock.components.ClockFace;
import app.nush.examclock.components.ClockMenu;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClockMenuTest {
    public static void main(String[] args) {
        FlatDarkLaf.setup();
        JFrame frame = new JFrame("Clock Face");
        ClockFace clockFace;
        frame.setJMenuBar(new ClockMenu(this));
        frame.setContentPane(clockFace = new ClockFace(this) {{
            setPreferredSize(new Dimension(800, 800));
        }});
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clockFace.setDark(!clockFace.isDark());
            }
        });
        new Timer(8, e -> {
            clockFace.repaint();
        }).start();
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
