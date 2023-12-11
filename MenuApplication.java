package com.example.suleimanovfaiz_uur_sp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class MenuApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/menu.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Snake Game Menu");
        Scene scene = new Scene(root);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(440);
        primaryStage.setScene(scene);
        primaryStage.show();
        Menu.mainStage = primaryStage;
    }
    public static void main(String[] args) {
        launch(args);
    }
}

