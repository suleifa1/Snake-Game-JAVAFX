package com.example.suleimanovfaiz_uur_sp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
public class StartGame implements Initializable {
    public static Stage startGameStage;
    private final MediaPlayer gamePlayer;
    private final MediaPlayer menuPlayer;
    @FXML
    private Label chooseLB;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Button exit;
    @FXML
    private Button startButton;
    public StartGame(MediaPlayer gamePlayer, MediaPlayer menuPlayer) {
        this.gamePlayer = gamePlayer;
        this.menuPlayer = menuPlayer;
    }
    private void onClose(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        Menu.mainStage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Menu.mainStage.hide();
        startButton.setOnAction(event -> {
            try {
                Color selectedColor = colorPicker.getValue();
                SnakeGUIController snakeGUIController = new SnakeGUIController(gamePlayer, menuPlayer, selectedColor);
                snakeGUIController.openSettingsWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        exit.setOnAction(this::onClose);
    }
}
