package com.example.suleimanovfaiz_uur_sp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class ScoreBoardController {
    public void openSettingsWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/scoreboard.fxml"));
        ScoreBoard board = new ScoreBoard();
        loader.setController(board);
        Parent root = loader.load();
        Stage scoreStage = new Stage();
        scoreStage.setTitle("Snake Game Score Board");
        Scene scene = new Scene(root);
        scoreStage.setMinWidth(600);
        scoreStage.setMinHeight(400);
        scoreStage.setScene(scene);
        scoreStage.show();
        scoreStage.setOnCloseRequest(e -> Menu.mainStage.show());
    }
}