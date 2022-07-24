package app.nush.examclock;

import app.nush.examclock.model.Observable;

public interface Context {
    Observable<Boolean> dark = new Observable<>(true);
    Observable<Boolean> debug = new Observable<>(true);

    default Observable<Boolean> dark() {
        return dark;
    }

    default Observable<Boolean> debug() {
        return debug;
    }

    Observable<Boolean> manToilet();

    Observable<Boolean> womanToilet();
}
