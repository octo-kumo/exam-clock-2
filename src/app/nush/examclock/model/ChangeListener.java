package app.nush.examclock.model;

public interface ChangeListener<T> {
    void changed(T n, T o);
}
