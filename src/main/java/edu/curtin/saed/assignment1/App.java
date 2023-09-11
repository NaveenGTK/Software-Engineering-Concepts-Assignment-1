package edu.curtin.saed.assignment1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.concurrent.*;

public class App extends Application
{
    private Thread scoreThread, wallSpawnerThread;
    private RobotSpawner robotSpawner;
    private WallSpawner wallSpawner;
    private ExecutorService executorService;
    private ScoreController scoreController;
    private Stage gameStage;

    public static void main(String[] args)
    {
        launch();
    }

    @Override
    public void start(Stage stage)
    {
        stage.setTitle("Example App (JavaFX)");
        this.gameStage = stage;
        Citadel citadel = new Citadel(4.0,4.0);
        JFXArena arena = new JFXArena(citadel);
        TextArea logger = new TextArea();
        executorService = Executors.newCachedThreadPool();

        ToolBar toolbar = new ToolBar();
//         Button btn1 = new Button("My Button 1");
//         Button btn2 = new Button("My Button 2");
        Label label = new Label("Score: ");
        Label labelWall = new Label();
//         toolbar.getItems().addAll(btn1, btn2, label);
        toolbar.getItems().addAll(label, labelWall);

        scoreController = new ScoreController(label);
        robotSpawner = new RobotSpawner(arena, logger, citadel, scoreController, this);
        GridSquare[][] gridArray = arena.getGridArray();
        wallSpawner = new WallSpawner(labelWall, 10, logger, arena, gridArray);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(arena, logger);
        arena.setMinWidth(300.0);

        BorderPane contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);

        Scene scene = new Scene(contentPane, 800, 800);
        stage.setScene(scene);
        stage.show();

        scoreThread = new Thread(scoreController);
        wallSpawnerThread = new Thread(wallSpawner);

        scoreThread.start();
        wallSpawnerThread.start();
        robotSpawner.spawn(executorService);
    }

    @Override
    public void stop() throws IllegalStateException {
        System.out.println("FINAL SCORE: " + scoreController.getScore());
        Platform.runLater(() -> {
            Stage stage = (Stage) gameStage.getScene().getWindow();
            stage.close();
            scoreThread.interrupt();
            wallSpawner.stop();
            robotSpawner.stop();
            wallSpawnerThread.interrupt();
            executorService.shutdownNow();
        });
    }


}
