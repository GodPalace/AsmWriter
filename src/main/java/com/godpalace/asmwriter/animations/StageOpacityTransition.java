package com.godpalace.asmwriter.animations;

import javafx.animation.Transition;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StageOpacityTransition extends Transition {
    private final Stage stage;
    private final double startValue;
    private final double endValue;

    public StageOpacityTransition(Stage stage, double startValue, double endValue, Duration duration) {
        if (startValue < endValue || startValue > 1 || endValue < 0) {
            throw new IllegalArgumentException("Invalid start or end value");
        }

        this.stage = stage;
        this.startValue = startValue;
        this.endValue = endValue;

        setCycleDuration(duration);
    }

    @Override
    public void interpolate(double v) {
        stage.setOpacity(startValue - (startValue - endValue) * v);
    }
}
