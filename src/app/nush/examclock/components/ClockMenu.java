package app.nush.examclock.components;

import javax.swing.*;

public class ClockMenu extends JMenuBar {
    public ClockMenu() {
        add(new JMenu("File") {{
            add(new JMenuItem("Open..."));
        }});
    }
}
