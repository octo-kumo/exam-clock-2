package app.nush.examclock.components.inputs.menu;

import app.nush.examclock.model.Observable;

import javax.swing.*;

public class CheckboxMenuItem extends JCheckBoxMenuItem {

    public CheckboxMenuItem(String text, Observable<Boolean> prop) {
        super(text, prop.get());

        addChangeListener(e -> prop.set(isSelected()));
        prop.listen((n, o) -> setSelected(n));
    }
}
