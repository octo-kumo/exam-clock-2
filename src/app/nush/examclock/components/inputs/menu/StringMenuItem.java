package app.nush.examclock.components.inputs.menu;

import app.nush.examclock.model.Observable;

public class StringMenuItem extends InputMenuItem<String> {
    public StringMenuItem(String title, Observable<String> prop) {
        super(title, prop);
    }

    @Override
    protected String parse(String text) {
        return text;
    }

    @Override
    protected String format(String in) {
        return in;
    }
}
