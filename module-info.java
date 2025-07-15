module PomodoroPlant {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.pomodoro to javafx.fxml;
    exports com.pomodoro;
}
