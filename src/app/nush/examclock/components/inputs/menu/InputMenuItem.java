package app.nush.examclock.components.inputs.menu;

import app.nush.examclock.model.Observable;

import javax.swing.*;

public abstract class InputMenuItem<T> extends JMenuItem {
    private final Observable<T> prop;

    public InputMenuItem(String title, Observable<T> prop) {
        this.prop = prop;
        setText(genText(title, prop.get()));
        prop.listen((n, o) -> setText(genText(title, n)));
        addActionListener(e -> {
            String s = JOptionPane.showInputDialog(this, title, prop.get());
            if (s == null) return;
            prop.set(parse(s));
        });
    }

    protected abstract T parse(String text);

    protected abstract String format(T in);

    private String genText(String title, T prop) {
        return String.format("<html>%s (<b>%s</b>)</html>", title, format(prop));
    }

    public Observable<T> getProp() {
        return prop;
    }
}
