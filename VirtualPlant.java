package com.pomodoro;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class VirtualPlant extends VBox {
    private ImageView plantView;

    public VirtualPlant() {
        plantView = new ImageView();
        plantView.setFitWidth(200);
        plantView.setPreserveRatio(true);
        setPlantStage(0);

        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.getChildren().add(plantView);
    }

    public void setPlantStage(int stage) {
        String path = "/resources/plant_stage" + stage + ".png";
        Image image = new Image(getClass().getResource(path).toExternalForm());
        plantView.setImage(image);
    }
}
