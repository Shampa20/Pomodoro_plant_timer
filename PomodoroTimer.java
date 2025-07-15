package com.pomodoro;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class PomodoroTimer {
    private Timeline timeline;
    private int timeSeconds;
    private Runnable onTick;
    private Runnable onFinish;

    public PomodoroTimer(int durationSeconds, Runnable onTick, Runnable onFinish) {
        this.timeSeconds = durationSeconds;
        this.onTick = onTick;
        this.onFinish = onFinish;

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeSeconds--;
            onTick.run();
            if (timeSeconds <= 0) {
                timeline.stop();
                onFinish.run();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void start() {
        timeline.play();
    }

    public void pause() {
        timeline.pause();
    }

    public void reset() {
        timeline.stop();
    }

    public boolean isRunning() {
        return timeline.getStatus() == Timeline.Status.RUNNING;
    }

    public int getTimeSeconds() {
        return timeSeconds;
    }
}
