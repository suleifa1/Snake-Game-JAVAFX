package com.example.suleimanovfaiz_uur_sp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
public class Menu implements Initializable {
    public static Stage mainStage;
    @FXML
    private Button exitButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button startButton;
    @FXML
    private ImageView volume;
    @FXML
    private Button showBoard;
    @FXML
    private void onExit() {
        menuPlayer.stop();
        System.exit(0);
    }
    public static boolean muted = false;
    private MediaPlayer menuPlayer;
    private double startVolume;
    @FXML
    private void setVolume() {
        if (!muted) {
            volume.setImage(new Image(getClass().getResource("images/muted.png").toString()));
            muted = true;
            menuPlayer.setMute(true);
        } else {
            volume.setImage(new Image(getClass().getResource("images/no.png").toString()));
            muted = false;
            menuPlayer.setMute(false);
            menuPlayer.setVolume(startVolume);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Media menuMedia = new Media(getClass().getResource("mp3/Snake Music.mp3").toString());
        menuPlayer = new MediaPlayer(menuMedia);
        menuPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        menuPlayer.play();
        startVolume = menuPlayer.getVolume();
        Media gameMedia = new Media(getClass().getResource("mp3/gameMusic.mp3").toString());
        MediaPlayer gamePlayer = new MediaPlayer(gameMedia);
        gamePlayer.setCycleCount(MediaPlayer.INDEFINITE);
        gamePlayer.setVolume(0.15);
        SettingsController settingsController = new SettingsController(menuPlayer, gamePlayer, volume);
        StartGameController startGameController = new StartGameController(gamePlayer, menuPlayer);
        ScoreBoardController scoreBoardController = new ScoreBoardController();
        settingsButton.setOnAction(event -> {
            try {
                settingsController.openSettingsWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        startButton.setOnAction(event -> {
            try {
                startGameController.openSettingsWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        showBoard.setOnAction(event -> {
            try {
                scoreBoardController.openSettingsWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}