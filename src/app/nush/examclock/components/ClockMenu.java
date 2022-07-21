package app.nush.examclock.components;

import app.nush.examclock.ExamClock;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ClockMenu extends JMenuBar {
    public ClockMenu(ExamClock examClock) {
        add(new JMenu("File") {{
            add(new JMenuItem("Open..."));
        }});
        add(new JMenu("View") {{
            add(new JMenu("Theme") {{
                ButtonGroup bg = new ButtonGroup();
                ActionListener listener = actionEvent -> {
                    if (examClock.setDark("dark".equals(bg.getSelection().getActionCommand()))) FlatDarkLaf.setup();
                    else FlatLightLaf.setup();
                    FlatLaf.updateUI();
                };
                JRadioButtonMenuItem light = new JRadioButtonMenuItem("Light");
                JRadioButtonMenuItem dark = new JRadioButtonMenuItem("Dark");
                light.addActionListener(listener);
                dark.addActionListener(listener);
                light.setActionCommand("light");
                dark.setActionCommand("dark");
                bg.add(add(light));
                bg.add(add(dark));
                (examClock.isDark() ? dark : light).setSelected(true);
            }});
        }});
    }
}
