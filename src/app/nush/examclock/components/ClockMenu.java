package app.nush.examclock.components;

import app.nush.examclock.Context;
import app.nush.examclock.ExamClock;
import app.nush.examclock.components.inputs.menu.CheckboxMenuItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ClockMenu extends JMenuBar {
    public ClockMenu(ExamClock examClock) {
        add(new JMenu("File") {{
            add(new JMenuItem("Open..."));
        }});
        add(new JMenu("Edit") {{
            add(new JMenuItem("Add...") {{
                setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | Event.SHIFT_MASK));
                addActionListener(e -> examClock.getList().add(e));
            }});
            add(new JMenuItem("Sort") {{
                setAccelerator(KeyStroke.getKeyStroke('F', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | Event.SHIFT_MASK));
                addActionListener(e -> examClock.getList().sort(e));
            }});
        }});
        add(new JMenu("View") {{
            add(new JMenu("Theme") {{
                ButtonGroup bg = new ButtonGroup();
                ActionListener listener = actionEvent -> examClock.dark().set("dark".equals(bg.getSelection().getActionCommand()));
                JRadioButtonMenuItem light = new JRadioButtonMenuItem("Light", !examClock.dark().get()), dark = new JRadioButtonMenuItem("Dark", examClock.dark().get());
                light.addActionListener(listener);
                dark.addActionListener(listener);
                light.setActionCommand("light");
                dark.setActionCommand("dark");
                bg.add(add(light));
                bg.add(add(dark));
            }});
            add(new CheckboxMenuItem("Debug", Context.debug));
        }});
    }
}
