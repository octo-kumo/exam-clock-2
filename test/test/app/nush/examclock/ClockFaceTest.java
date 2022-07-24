package test.app.nush.examclock;

import app.nush.examclock.Context;
import app.nush.examclock.components.ClockFace;
import app.nush.examclock.model.Observable;
import app.nush.examclock.utils.Fonts;

import javax.swing.*;
import java.awt.*;

public class ClockFaceTest {
    public static void main(String[] args) {
        Fonts.loadFonts();
        JFrame frame = new JFrame("Clock Face");
        ClockFace clockFace;
        frame.setContentPane(clockFace = new ClockFace(new Context() {
            @Override
            public Observable<Boolean> manToilet() {
                return new Observable<>(false);
            }

            @Override
            public Observable<Boolean> womanToilet() {
                return new Observable<>(false);
            }
        }) {{
            setPreferredSize(new Dimension(800, 800));
        }});
        new Timer(8, e -> {
            clockFace.repaint();
        }).start();
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
