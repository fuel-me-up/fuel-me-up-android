package de.fuelmeup.observable;

import android.widget.SeekBar;

import de.fuelmeup.ui.component.custom.LabelledSeekBar;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by jonas on 12.10.14.
 */
public class SeekBarObservable {

    public static Observable<Integer> startTrackingTouch(LabelledSeekBar seekBar) {
        return Observable.create((Subscriber<? super Integer> subscriber) -> {
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (subscriber.isUnsubscribed()) {
                        return;
                    }
                    subscriber.onNext(seekBar.getProgress());
                }
            });
        });
    }
}
