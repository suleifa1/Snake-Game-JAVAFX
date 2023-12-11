package com.example.suleimanovfaiz_uur_sp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
public class ScoreBoard implements Initializable {
    @FXML
    private TableView<PlayerScore> tableView;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Menu.mainStage.hide();
        TableColumn<PlayerScore, String> nameColumn = new TableColumn<>("Nickname");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        TableColumn<PlayerScore, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        scoreColumn.setSortType(TableColumn.SortType.DESCENDING);
        tableView.getColumns().addAll(nameColumn, scoreColumn);
        ObservableList<PlayerScore> scores = FXCollections.observableArrayList();
        try {
            String pathToJar = MenuApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            String directoryPath = new File(pathToJar).getParent();
            File scoreFile = new File(directoryPath, "scores.txt");
            if (!scoreFile.exists()) {
                scoreFile.createNewFile();
            }
            BufferedReader reader = new BufferedReader(new FileReader(scoreFile));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains("-")) {
                    return;
                }
                String[] parts = line.split(" - ");
                String playerName = parts[0];
                int playerScore = Integer.parseInt(parts[1]);
                scores.add(new PlayerScore(playerName, playerScore));
            }
            reader.close();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        tableView.setItems(scores);
        tableView.getSortOrder().add(scoreColumn);
    }
}
