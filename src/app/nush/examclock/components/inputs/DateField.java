package app.nush.examclock.components.inputs;

import app.nush.examclock.utils.Fonts;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class DateField extends PlaceholderTextField implements Changeable {
    private final DateTimeFormatter formatter;
    private LocalDate date;

    public DateField() {
        this(null);
    }

    private ChangeListener changeListener = e -> {
    };

    public DateField(LocalDate date) {
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        setFont(Fonts.opensans.deriveFont(12f));
        setPlaceholder("yyyy-MM-dd");
        setDate(date);

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                updateValue();
                setDate(DateField.this.date);
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
            setDate(LocalDate.from(formatter.parse(getText())));
        } catch (DateTimeParseException ignored) {
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        boolean fire = !Objects.equals(this.date, date);
        this.date = date;
        int start = getSelectionStart(), end = getSelectionEnd();
        setText(date == null ? "" : formatter.format(date));
        setSelectionStart(start);
        setSelectionEnd(end);
        if (fire) changeListener.stateChanged(new ChangeEvent(this));
    }

    @Override
    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }
}
