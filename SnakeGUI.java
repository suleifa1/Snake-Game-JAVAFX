package com.example.suleimanovfaiz_uur_sp;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Point;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;
public class SnakeGUI implements Initializable {
    private final MediaPlayer gamePlayer;
    private final MediaPlayer menuPlayer;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static final int ROWS = 20;
    private static final int COLUMNS = ROWS;
    private static final int SQUARE_SIZE = WIDTH / ROWS;
    private static String[] FOODS_IMAGE;
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private final Color snakeColor;
    private GraphicsContext gc;
    private final List<Point> snakeBody = new ArrayList<>();
    private Point snakeHead;
    private Image foodImage;
    private int foodX;
    private int foodY;
    private boolean gameOver;
    private int currentDirection;
    private int score = 0;
    private int frameCounter = 0;
    private Timeline timeline;
    private boolean isCountdownActive = false;
    private final String apple = Objects.requireNonNull(getClass().getResource("images/ic_apple.png")).toString();
    private final String berry = Objects.requireNonNull(getClass().getResource("images/ic_berry.png")).toString();
    private final String cherry = Objects.requireNonNull(getClass().getResource("images/ic_cherry.png")).toString();
    private final String coconut = Objects.requireNonNull(getClass().getResource("images/ic_coconut_.png")).toString();
    private final String orange = Objects.requireNonNull(getClass().getResource("images/ic_orange.png")).toString();
    private final String peach = Objects.requireNonNull(getClass().getResource("images/ic_peach.png")).toString();
    private final String pomegranate = Objects.requireNonNull(getClass().getResource("images/ic_pomegranate.png")).toString();
    private final String tomato = Objects.requireNonNull(getClass().getResource("images/ic_tomato.png")).toString();
    private final String watermelon = Objects.requireNonNull(getClass().getResource("images/ic_watermelon.png")).toString();
    @FXML
    Canvas gameCanvas;
    @FXML
    private AnchorPane group;
    @FXML
    private AnchorPane gameOverPane;
    @FXML
    private AnchorPane saveOverPane;
    @FXML
    private Button exitButton;
    @FXML
    private Text countdownText;
    @FXML
    private TextField nicknameTF;
    @FXML
    private Button saveScore;
    @FXML
    private CheckBox playedCB;
    @FXML
    private ComboBox<String> playersCB;
    public SnakeGUI(MediaPlayer gamePlayer, MediaPlayer menuPlayer, Color color) {
        this.gamePlayer = gamePlayer;
        this.menuPlayer = menuPlayer;
        this.snakeColor = color;
    }
    @FXML
    private void goToSave() {
        nicknameTF.clear();
        saveScore.setDisable(true);
        fillComboBox();
        setupControls();
        nicknameTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                if (!saveScore.isDisabled())
                    saveScore.setDisable(true);
            } else {
                if (saveScore.isDisabled())
                    saveScore.setDisable(false);
            }
        });
        gameOverPane.setVisible(false);
        saveOverPane.setVisible(true);
    }
    @FXML
    private void backToGO() {
        saveOverPane.setVisible(false);
        gameOverPane.setVisible(true);
    }
    @FXML
    public void restartGame() {
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        snakeBody.clear();
        for (int i = 0; i < 3; i++) {
            snakeBody.add(new Point(5, ROWS / 2));
        }
        snakeHead = snakeBody.get(0);
        generateFood();
        gameOver = false;
        currentDirection = RIGHT;
        score = 0;
        gameOverPane.setVisible(false);
        group.requestFocus();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FOODS_IMAGE = new String[]{apple, berry, cherry, coconut, orange, peach, pomegranate, tomato, watermelon};
        StartGame.startGameStage.hide();
        gc = gameCanvas.getGraphicsContext2D();
        group.setFocusTraversable(true);
        Platform.runLater(() -> group.requestFocus());
        setUpActions();
        setUpSnake();
        timeline = new Timeline(new KeyFrame(Duration.millis(35), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        menuPlayer.setMute(true);
        gamePlayer.play();
    }
    private void setUpActions(){
        group.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.RIGHT || code == KeyCode.D) {
                if (currentDirection != LEFT) {
                    currentDirection = RIGHT;
                }
            } else if (code == KeyCode.LEFT || code == KeyCode.A) {
                if (currentDirection != RIGHT) {
                    currentDirection = LEFT;
                }
            } else if (code == KeyCode.UP || code == KeyCode.W) {
                if (currentDirection != DOWN) {
                    currentDirection = UP;
                }
            } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                if (currentDirection != UP) {
                    currentDirection = DOWN;
                }
            } else if (code == KeyCode.ESCAPE && !isCountdownActive && !gameOver) {
                if (timeline.getStatus() == Animation.Status.RUNNING) {
                    timeline.pause();
                    countdownText.setText("Paused");
                    countdownText.setFont(new Font("Comic Sans MS", 60));
                    countdownText.setFill(Color.rgb(128, 128, 128));
                    countdownText.setLayoutX(311);
                    countdownText.setLayoutY(394);
                } else if (timeline.getStatus() == Animation.Status.PAUSED) {
                    isCountdownActive = true;
                    Timer timer = new Timer();
                    countdownText.setFont(new Font("Comic Sans MS", 60));
                    countdownText.setFill(Color.rgb(0, 255, 33));
                    countdownText.setLayoutX(390);
                    countdownText.setLayoutY(399);
                    TimerTask task = new TimerTask() {
                        int seconds = 6;
                        public void run() {
                            Platform.runLater(() -> {
                                countdownText.setText(String.valueOf(seconds));
                                ScaleTransition scaleTransition = new ScaleTransition();
                                scaleTransition.setDuration(Duration.millis(500));
                                scaleTransition.setNode(countdownText);
                                scaleTransition.setByY(1.5);
                                scaleTransition.setByX(1.5);
                                scaleTransition.setAutoReverse(true);
                                scaleTransition.setCycleCount(2);
                                scaleTransition.play();
                                Color color = Color.rgb(0, 255, 33);
                                Color interColor = color.interpolate(Color.RED, (double) (6 - seconds) / 5);
                                countdownText.setFill(interColor);
                            });
                            seconds--;
                            if (seconds < 1) {
                                timer.cancel();
                                Platform.runLater(() -> {
                                    timeline.play();
                                    countdownText.setText("");
                                    isCountdownActive = false;
                                });
                            }
                        }
                    };
                    timer.scheduleAtFixedRate(task, 0, 1000);
                }
            }
        });
        exitButton.setOnAction(this::onClose);
        nicknameTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 10) {
                nicknameTF.setText(oldValue);
            } else {
                if (!newValue.matches("[a-zA-ZěščřžýáíéĚŠČŘŽÝÁÍÉ0-9]*")) {
                    nicknameTF.setText(oldValue);
                }
            }
        });

        saveScore.setOnAction(e -> {
            saveResultToFile(nicknameTF.getText(), score);
            backToGO();
        });

    }
    private void setUpSnake(){
        for (int i = 0; i < 3; i++) {
            snakeBody.add(new Point(5, ROWS / 2));
        }
        snakeHead = snakeBody.get(0);
        generateFood();
    }
    private void onClose(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        gamePlayer.stop();
        gamePlayer.stop();
        if (!Menu.muted)
            menuPlayer.setMute(false);
        stage.close();
        StartGame.startGameStage.show();
    }
    private void saveResultToFile(String playerName, int score) {
        try {
            String pathToJar = MenuApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            String directoryPath = new File(pathToJar).getParent();
            File scoreFile = new File(directoryPath, "scores.txt");
            HashMap<String, Integer> scores = new HashMap<>();
            if (scoreFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(scoreFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" - ");
                    scores.put(parts[0], Integer.parseInt(parts[1]));
                }
                reader.close();
            }
            if (scores.containsKey(playerName)) {
                int existingScore = scores.get(playerName);
                if (score > existingScore) {
                    scores.put(playerName, score);
                }
            } else {
                scores.put(playerName, score);
            }
            FileWriter fileWriter = new FileWriter(scoreFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                bufferedWriter.write(entry.getKey() + " - " + entry.getValue() + "\n");
            }
            bufferedWriter.close();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    private void fillComboBox() {
        playersCB.getItems().clear();
        try {
            String pathToJar = MenuApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            String directoryPath = new File(pathToJar).getParent();
            File scoreFile = new File(directoryPath, "scores.txt");
            if (scoreFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(scoreFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" - ");
                    playersCB.getItems().add(parts[0]);
                }
                reader.close();
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    private void setupControls() {
        playersCB.setDisable(true);
        playedCB.setSelected(false);
        playedCB.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> playersCB.setDisable(!isNowSelected));
        playersCB.valueProperty().addListener((obs, oldVal, newVal) -> nicknameTF.setText(newVal));
    }
    private void run(GraphicsContext gc) {
        if (gameOver) {
            return;
        }
        drawBackground(gc);
        drawFood(gc);
        drawSnake(gc);
        drawScore();
        frameCounter++;
        if (frameCounter % 3 != 0) {
            return;
        }
        for (int i = snakeBody.size() - 1; i >= 1; i--) {
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            snakeBody.get(i).y = snakeBody.get(i - 1).y;
        }
        switch (currentDirection) {
            case RIGHT -> moveRight();
            case LEFT -> moveLeft();
            case UP -> moveUp();
            case DOWN -> moveDown();
        }
        gameOver();
        eatFood();
    }
    private void drawBackground(GraphicsContext gc) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("AAD751"));
                } else {
                    gc.setFill(Color.web("A2D149"));
                }
                gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }
    private void generateFood() {
        start:
        while (true) {
            foodX = (int) (Math.random() * ROWS);
            foodY = (int) (Math.random() * COLUMNS);
            for (Point snake : snakeBody) {
                if (snake.getX() == foodX && snake.getY() == foodY) {
                    continue start;
                }
            }
            foodImage = new Image(FOODS_IMAGE[(int) (Math.random() * FOODS_IMAGE.length)]);
            break;
        }
    }
    private void drawFood(GraphicsContext gc) {
        gc.drawImage(foodImage, foodX * SQUARE_SIZE, foodY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }
    private void drawSnake(GraphicsContext gc) {
        for (int i = 0; i < snakeBody.size() - 1; i++) {
            Point currentSegment = snakeBody.get(i);
            Point nextSegment = snakeBody.get(i + 1);
            double position = (double) i / (snakeBody.size() - 1);
            Color endColor = Color.web("FFFFFF");
            Color currentColor = snakeColor.interpolate(endColor, position);
            gc.setStroke(currentColor);
            gc.setLineWidth(SQUARE_SIZE);
            gc.setLineCap(StrokeLineCap.ROUND);
            gc.strokeLine(currentSegment.getX() * SQUARE_SIZE + SQUARE_SIZE / 2,
                    currentSegment.getY() * SQUARE_SIZE + SQUARE_SIZE / 2,
                    nextSegment.getX() * SQUARE_SIZE + SQUARE_SIZE / 2,
                    nextSegment.getY() * SQUARE_SIZE + SQUARE_SIZE / 2);
        }
    }
    private void moveRight() {
        snakeHead.x++;
    }
    private void moveLeft() {
        snakeHead.x--;
    }
    private void moveUp() {
        snakeHead.y--;
    }
    private void moveDown() {
        snakeHead.y++;
    }
    public void gameOver() {
        if (snakeHead.x < 0 || snakeHead.y < 0 || snakeHead.x * SQUARE_SIZE >= WIDTH || snakeHead.y * SQUARE_SIZE >= HEIGHT) {
            gameOver = true;
            gameOverPane.setVisible(true);
            return;
        }
        for (int i = 1; i < snakeBody.size(); i++) {
            if (snakeHead.x == snakeBody.get(i).getX() && snakeHead.getY() == snakeBody.get(i).getY()) {
                gameOver = true;
                gameOverPane.setVisible(true);
                return;
            }
        }
    }
    private void eatFood() {
        if (snakeHead.getX() == foodX && snakeHead.getY() == foodY) {
            snakeBody.add(new Point(snakeBody.get(snakeBody.size() - 1)));
            generateFood();
            score += 5;
        }
    }
    private void drawScore() {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("Score: " + score, 10, 35);
    }
}
