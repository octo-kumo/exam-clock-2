package app.nush.examclock;

import app.nush.examclock.model.Observable;

public interface Config {
    Observable<Boolean> dark();

    Observable<Boolean> manToilet();

    Observable<Boolean> womanToilet();
}
