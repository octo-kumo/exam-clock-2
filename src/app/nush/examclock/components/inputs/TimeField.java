package app.nush.examclock.components.inputs;

import app.nush.examclock.utils.Fonts;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class TimeField extends PlaceholderTextField implements Changeable {
    private final DateTimeFormatter formatter;
    private LocalTime time;

    public TimeField() {
        this(null);
    }

    private ChangeListener changeListener = e -> {
    };

    public TimeField(LocalTime time) {
        this.formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        setFont(Fonts.opensans.deriveFont(12f));
        setPlaceholder("HH:mm:ss");
        setTime(time == null ? null : time.withNano(0));

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                updateValue();
                setTime(TimeField.this.time);
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateValue();
            }
        });
    }

    public void updateValue() {
        try {
            setTime(LocalTime.from(formatter.parse(getText())));
        } catch (DateTimeParseException ignored) {
        }
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        boolean fire = !Objects.equals(this.time, time);
        this.time = time;
        int start = getSelectionStart(), end = getSelectionEnd();
        setText(time == null ? "" : formatter.format(time));
        setSelectionStart(start);
        setSelectionEnd(end);
        if (fire) changeListener.stateChanged(new ChangeEvent(this));
    }

    @Override
    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }
}
