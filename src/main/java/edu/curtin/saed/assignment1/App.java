package edu.curtin.saed.assignment1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App extends Application 
{
    Thread scoreThread;
    public static void main(String[] args) 
    {
        launch();        
    }
    
    @Override
    public void start(Stage stage) 
    {
        stage.setTitle("Example App (JavaFX)");
        JFXArena arena = new JFXArena();
        Citadel citadel = new Citadel(5,5);
        TextArea logger = new TextArea();
        ScoreController scoreController = new ScoreController(logger);
        RobotSpawner robotSpawner = new RobotSpawner(citadel,arena,logger);

        arena.addListener((x, y) ->
        {
            System.out.println("Arena click at (" + x + "," + y + ")");
        });
        
        ToolBar toolbar = new ToolBar();
//         Button btn1 = new Button("My Button 1");
//         Button btn2 = new Button("My Button 2");
        Label label = new Label("Score: ");
//         toolbar.getItems().addAll(btn1, btn2, label);
        toolbar.getItems().addAll(label);
        
//         btn1.setOnAction((event) ->
//         {
//             System.out.println("Button 1 pressed");
//         });
                    

        logger.appendText("Hello\n");
        logger.appendText("World\n");
        
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(arena, logger);
        arena.setMinWidth(300.0);
        
        BorderPane contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);
        
        Scene scene = new Scene(contentPane, 800, 800);
        stage.setScene(scene);
        stage.show();

        scoreThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Platform.runLater(() -> {
                        scoreController.addPoints(10);
                        label.setText("Score: " + scoreController.getScore());
                    });
                    Thread.sleep(1000);
                }
            } catch (InterruptedException i) {
                Thread.currentThread().interrupt();
            }
        });
        scoreThread.start();
        spawnRobots(robotSpawner);
    }

    @Override
    public void stop() throws IllegalStateException {
        scoreThread.interrupt();
    }

    private void spawnRobots(RobotSpawner robotSpawner) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable spawnTask = () -> {
            robotSpawner.spawn();
        };

        scheduler.scheduleAtFixedRate(spawnTask, 0, 1500, TimeUnit.MILLISECONDS);
    }
}
