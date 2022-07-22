package app.nush.examclock.model;

import java.util.ArrayList;

public class Observable<T> {
    private T value;
    private final ArrayList<ChangeListener<T>> listeners = new ArrayList<>();

    public Observable() {
        this(null);
    }

    public Observable(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public T set(T value) {
        listeners.forEach(l -> l.changed(value, this.value));
        return this.value = value;
    }

    public Observable<T> listen(ChangeListener<T> listener) {
        listeners.add(listener);
        return this;
    }

    public Observable<T> stop(ChangeListener<T> listener) {
        listeners.remove(listener);
        return this;
    }
}
