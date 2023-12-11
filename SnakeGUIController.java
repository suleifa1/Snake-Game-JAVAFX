package com.example.suleimanovfaiz_uur_sp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
public class SnakeGUIController {
    private final MediaPlayer gamePlayer;
    private final MediaPlayer menuPlayer;
    private final Color snakeColor;
    public SnakeGUIController(MediaPlayer gamePlayer, MediaPlayer menuPlayer, Color color) {
        this.gamePlayer = gamePlayer;
        this.menuPlayer = menuPlayer;
        this.snakeColor = color;
    }
    public void openSettingsWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/snake.fxml"));
        SnakeGUI snake = new SnakeGUI(gamePlayer, menuPlayer, snakeColor);
        loader.setController(snake);
        Parent root = loader.load();
        Stage snakeStage = new Stage();
        snakeStage.setTitle("Snake Game");
        Scene scene = new Scene(root, 800, 800);
        snakeStage.setScene(scene);
        snakeStage.show();
        snakeStage.setOnCloseRequest(e -> {
            gamePlayer.stop();
            if (!Menu.muted)
                menuPlayer.setMute(false);
            StartGame.startGameStage.show();
        });
        snakeStage.setResizable(false);
    }
}
