package app.nush.examclock.components.inputs.menu;

import app.nush.examclock.model.Observable;

public class DoubleMenuItem extends InputMenuItem<Double> {
    public DoubleMenuItem(String title, Observable<Double> prop) {
        super(title, prop);
    }

    @Override
    protected Double parse(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return getProp().get();
        }
    }

    @Override
    protected String format(Double in) {
        return String.valueOf(in);
    }
}
