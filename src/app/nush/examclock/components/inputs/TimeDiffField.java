package app.nush.examclock.components.inputs;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TimeDiffField extends TimeField implements ChangeListener {
    private final Supplier<LocalDateTime> startTimeSupplier;
    private final Supplier<LocalDateTime> endTimeSupplier;

    public TimeDiffField(Supplier<LocalDateTime> startTimeSupplier,
                         Supplier<LocalDateTime> endTimeSupplier,
                         Consumer<LocalDateTime> endTimeConsumer,
                         Changeable[] changeables) {
        this.startTimeSupplier = startTimeSupplier;
        this.endTimeSupplier = endTimeSupplier;

        for (Changeable changeable : changeables) changeable.setChangeListener(this);
        setPlaceholder("00:00:00");
        setChangeListener(e -> endTimeConsumer.accept(this.startTimeSupplier.get().plusSeconds(getTime().toSecondOfDay())));
        stateChanged(null);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        try {
            long seconds = startTimeSupplier.get().until(endTimeSupplier.get(), ChronoUnit.SECONDS);
            setTime(LocalTime.ofSecondOfDay(seconds));
        } catch (NullPointerException | DateTimeException ignored) {
        }
    }
}
