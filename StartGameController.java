package com.example.suleimanovfaiz_uur_sp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
public class StartGameController {
    private final MediaPlayer gamePlayer;
    private final MediaPlayer menuPlayer;
    public StartGameController(MediaPlayer gamePlayer, MediaPlayer menuPlayer) {
        this.gamePlayer = gamePlayer;
        this.menuPlayer = menuPlayer;
    }
    public void openSettingsWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/startgame.fxml"));
        StartGame game = new StartGame(gamePlayer, menuPlayer);
        loader.setController(game);
        Parent root = loader.load();
        Stage startGame2 = new Stage();
        startGame2.setTitle("Start Game");
        Scene scene = new Scene(root);
        startGame2.setScene(scene);
        startGame2.show();
        StartGame.startGameStage = startGame2;
        startGame2.setOnCloseRequest(e -> {
            gamePlayer.stop();
            menuPlayer.play();
        });
        startGame2.setResizable(false);
        startGame2.setOnCloseRequest(e -> Menu.mainStage.show());
    }
}
