package com.example.suleimanovfaiz_uur_sp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
public class SettingsController {
    private final MediaPlayer menuPlayer;
    private final MediaPlayer gamePlayer;
    private final ImageView volume;
    public SettingsController(MediaPlayer menuPlayer, MediaPlayer gamePlayer, ImageView volume) {
        this.menuPlayer = menuPlayer;
        this.gamePlayer = gamePlayer;
        this.volume = volume;
    }
    public void openSettingsWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/settings.fxml"));
        Settings settings = new Settings(menuPlayer, gamePlayer, volume);
        loader.setController(settings);
        Parent root = loader.load();
        Stage settingsStage = new Stage();
        settingsStage.setTitle("Snake Game Settings");
        Scene scene = new Scene(root, 500, 300);
        settingsStage.setScene(scene);
        settingsStage.show();
        settingsStage.setResizable(false);
        settingsStage.setOnCloseRequest(e -> Menu.mainStage.show());
    }
}
