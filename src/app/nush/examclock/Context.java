package app.nush.examclock;

import app.nush.examclock.model.Observable;

public interface Context {
    Observable<Boolean> dark = new Observable<>(true);
    Observable<Boolean> debug = new Observable<>(true);
    Observable<Boolean> quality = new Observable<>(true);

    Observable<Boolean> face_arcs = new Observable<>(false);
    Observable<Boolean> manToilet = new Observable<>(false);
    Observable<Boolean> womanToilet = new Observable<>(false);

    Observable<Boolean> optimizeFPS = new Observable<>(false);
    Observable<Integer> fps = new Observable<>(30);
}
