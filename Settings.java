package com.example.suleimanovfaiz_uur_sp;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
public class Settings implements Initializable {
    private final MediaPlayer menuPlayer;
    private final MediaPlayer gamePlayer;
    private final ImageView volume;
    public Settings(MediaPlayer menuPlayer, MediaPlayer gamePlayer, ImageView volume) {
        this.menuPlayer = menuPlayer;
        this.gamePlayer = gamePlayer;
        this.volume = volume;
    }
    @FXML
    private Button backButton;
    @FXML
    private Button clearScoreboardButton;
    @FXML
    private Label gameSound;
    @FXML
    private Slider gameSoundSlider;
    @FXML
    private Label menuSound;
    @FXML
    private Slider menuSoundSlider;
    @FXML
    private Label cleared;
    @FXML
    private void onClose(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        Menu.mainStage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Menu.mainStage.hide();
        gameSoundSlider.setValue(gamePlayer.getVolume());
        gameSoundSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            gamePlayer.setVolume(newValue.doubleValue());
        });
        if (Menu.muted) {
            menuPlayer.setVolume(0);
        }
        menuSoundSlider.setValue(menuPlayer.getVolume());
        menuSoundSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            menuPlayer.setVolume(newValue.doubleValue());
            if (Double.parseDouble(String.valueOf(newValue)) == 0) {
                volume.setImage(new Image(getClass().getResource("images/muted.png").toString()));
                Menu.muted = true;
                if (!menuPlayer.isMute())
                    menuPlayer.setMute(true);
            } else {
                volume.setImage(new Image(getClass().getResource("images/no.png").toString()));
                Menu.muted = false;
                if (menuPlayer.isMute())
                    menuPlayer.setMute(false);
            }
        });
        backButton.setOnAction(this::onClose);
        clearScoreboardButton.setOnAction(event -> {
            try {
                String pathToJar = MenuApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                String directoryPath = new File(pathToJar).getParent();
                File scoreFile = new File(directoryPath, "scores.txt");
                FileWriter fileWriter = new FileWriter(scoreFile, false);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.flush();
                printWriter.close();
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.8), cleared);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
                fadeIn.setOnFinished((ActionEvent event1) -> {
                    FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), cleared);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.setDelay(Duration.seconds(2));
                    fadeOut.play();
                });
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

    }
}