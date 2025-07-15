package com.pomodoro;

import javafx.scene.control.Label;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.media.AudioClip;

public class Main extends Application {
    private PomodoroTimer timer;
    private boolean onBreak = false;
    private boolean started = false;

    private Label timerLabel = new Label("25:00");
    private Label statusLabel = new Label("");
    private VirtualPlant virtualPlant = new VirtualPlant();

    private Button startButton = new Button("Start");
    private Button pauseButton = new Button("Pause");
    private Button resetButton = new Button("Reset");
    private Button breakButton = new Button("Break");

    private final int SESSION_SECONDS = 25 * 60;
    private final int BREAK_SECONDS = 5 * 60;

    private AudioClip startSound;
    private AudioClip endSound;
    private AudioClip breakSound;

    @Override
    public void start(Stage stage) {
        // Load sounds
        try {
            startSound = new AudioClip(getClass().getResource("/resources/start.mp3").toExternalForm());
            endSound = new AudioClip(getClass().getResource("/resources/end.mp3").toExternalForm());
            breakSound = new AudioClip(getClass().getResource("/resources/break.mp3").toExternalForm());
        } catch (Exception e) {
            System.out.println("Error loading audio: " + e.getMessage());
        }

        timerLabel.setStyle("-fx-font-size: 24;");
        statusLabel.setStyle("-fx-text-fill: green; -fx-font-size: 18;");

        VBox root = new VBox(15, timerLabel, statusLabel, virtualPlant, startButton, pauseButton, resetButton, breakButton);
        root.setAlignment(Pos.CENTER);

        setUpButtons();

        Scene scene = new Scene(root, 400, 500);
        stage.setScene(scene);
        stage.setTitle("Pomodoro Plant Timer");
        stage.show();

        startSession(); // preload the timer, but don't play any sound
    }

    private void startSession() {
        onBreak = false;
        statusLabel.setText("");
        timer = new PomodoroTimer(SESSION_SECONDS, this::updateUI, this::onSessionComplete);
        updateUI();
    }

    private void startBreak() {
        onBreak = true;
        statusLabel.setText("Break Time 🌿");
        timer = new PomodoroTimer(BREAK_SECONDS, this::updateUI, this::onBreakComplete);
        updateUI();
        timer.start();
    }

    private void updateUI() {
        int secondsLeft = timer.getTimeSeconds();
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));

        if (!onBreak) {
            int stage = 4 - (secondsLeft / 300);
            virtualPlant.setPlantStage(Math.max(0, Math.min(stage, 4)));
        }
    }

    private void onSessionComplete() {
        statusLabel.setText("✅ Session Complete!");
        if (endSound != null) endSound.play();
        startBreak();
    }

    private void onBreakComplete() {
        statusLabel.setText("Break over. Ready to start again!");
        virtualPlant.setPlantStage(0);
        timer = new PomodoroTimer(SESSION_SECONDS, this::updateUI, this::onSessionComplete);  // Reset to 25 min
        updateUI(); // Update the screen to show 25:00
    }


    private void setUpButtons() {
        startButton.setOnAction(e -> {
            if (!timer.isRunning()) {
                timer.start();
                if (!started) {
                    if (startSound != null) startSound.play();
                    started = true; // prevent sound on multiple clicks
                }
            }
        });

        pauseButton.setOnAction(e -> {
            if (timer.isRunning()) timer.pause();
        });

        resetButton.setOnAction(e -> {
            startSession(); // Always go back to full 25-minute Pomodoro
            started = false; // Allow start sound again
        });

        breakButton.setOnAction(e -> {
            timer.reset();
            startBreak();
            started = false; // allow sound again next session
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
