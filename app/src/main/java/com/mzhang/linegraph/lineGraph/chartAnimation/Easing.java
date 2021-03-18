package com.mzhang.linegraph.lineGraph.chartAnimation;

import android.animation.TimeInterpolator;

import androidx.annotation.RequiresApi;

/**
 * Easing options.
 *
 */
@SuppressWarnings("WeakerAccess")
@RequiresApi(11)
public class Easing {

    public interface EasingFunction extends TimeInterpolator {
        @Override
        float getInterpolation(float input);
    }


    @SuppressWarnings("unused")
    public static final EasingFunction Linear = new EasingFunction() {
        public float getInterpolation(float input) {
            return input;
        }
    };

}
