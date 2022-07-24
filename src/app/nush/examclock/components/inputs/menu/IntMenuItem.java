package app.nush.examclock.components.inputs.menu;

import app.nush.examclock.model.Observable;

public class IntMenuItem extends InputMenuItem<Integer> {
    public IntMenuItem(String title, Observable<Integer> prop) {
        super(title, prop);
    }

    @Override
    protected Integer parse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return getProp().get();
        }
    }

    @Override
    protected String format(Integer in) {
        return String.valueOf(in);
    }
}
