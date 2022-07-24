package app.nush.examclock.components;

import app.nush.examclock.Context;
import app.nush.examclock.ExamClock;
import app.nush.examclock.components.inputs.menu.BooleanMenuItem;
import app.nush.examclock.components.inputs.menu.IntMenuItem;
import app.nush.examclock.i18n;
import app.nush.examclock.model.Exam;
import app.nush.examclock.utils.io.FileIO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;

import static app.nush.examclock.i18n.B;

public class ClockMenu extends JMenuBar {
    public ClockMenu(ExamClock examClock) {
        add(new JMenu("File") {{
            add(new JMenuItem("Open...") {{
                addActionListener(e -> {
                    if (!examClock.getList().isEmpty() && JOptionPane.showConfirmDialog(null, "Load from file even though you have exams already loaded?") != JOptionPane.OK_OPTION)
                        return;
                    Exam[] exams = FileIO.load();
                    if (exams == null) return;
                    examClock.getList().clear();
                    examClock.getList().addAll(Arrays.asList(exams));
                });
            }});
            add(new JMenuItem("Save...") {{
                addActionListener(e -> FileIO.save(examClock.getList().toArray()));
            }});
        }});
        add(new JMenu("Edit") {{
            add(new JMenuItem(B.getString(i18n.menu_add_exam)) {{
                setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | Event.SHIFT_MASK));
                addActionListener(e -> examClock.getList().add(e));
            }});
            add(new JMenuItem(B.getString(i18n.menu_sort_exam)) {{
                setAccelerator(KeyStroke.getKeyStroke('F', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | Event.SHIFT_MASK));
                addActionListener(e -> examClock.getList().sort(e));
            }});
        }});
        add(new JMenu("View") {{
            add(new JMenu("Theme") {{
                ButtonGroup bg = new ButtonGroup();
                ActionListener listener = actionEvent -> Context.dark.set("dark".equals(bg.getSelection().getActionCommand()));
                JRadioButtonMenuItem light = new JRadioButtonMenuItem("Light", !Context.dark.get()), dark = new JRadioButtonMenuItem("Dark", Context.dark.get());
                light.addActionListener(listener);
                dark.addActionListener(listener);
                light.setActionCommand("light");
                dark.setActionCommand("dark");
                bg.add(add(light));
                bg.add(add(dark));
            }});
            add(new BooleanMenuItem("Debug", Context.debug));
            add(new BooleanMenuItem("Quality Render", Context.quality));
            add(new JSeparator());
            add(new JMenu("Clock") {{
                add(new BooleanMenuItem("Circular exam progress", Context.face_arcs));
            }});
            add(new IntMenuItem("FPS", Context.fps));
            add(new BooleanMenuItem("Auto Optimize FPS", Context.optimizeFPS));
        }});
    }
}
