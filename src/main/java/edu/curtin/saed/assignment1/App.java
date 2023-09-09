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
    private Thread scoreThread, wallBuilderThread;
    private BlockingQueue<Wall> wallQueue;
    private int maxWalls;
    private int curWalls;
    public static void main(String[] args) 
    {
        launch();        
    }
    
    @Override
    public void start(Stage stage) 
    {
        stage.setTitle("Example App (JavaFX)");
        Citadel citadel = new Citadel(4.0,4.0);
        JFXArena arena = new JFXArena(citadel);
        TextArea logger = new TextArea();
        ScoreController scoreController = new ScoreController();
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        RobotSpawner robotSpawner = new RobotSpawner(arena, logger, citadel);

        wallQueue = arena.getWallQueue();
        maxWalls = 10;
        curWalls = 0;
        GridSquare[][] gridArray = arena.getGridArray();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        arena.addListener((x, y) ->
        {
            System.out.println("Arena click at (" + x + "," + y + ")");
            if (curWalls <= maxWalls) {
                Wall wall = new Wall(x, y);
                executor.submit(() -> {
                    try {
                        Thread.sleep(2000);
                        boolean r = wallQueue.offer(wall);
                        if(r){
                            curWalls += 1;
                            logger.appendText("Curwall count: " + curWalls);
                        }
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                });
            }
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


        wallBuilderThread = new Thread(() -> {
            while (curWalls <= maxWalls) {
                Wall wall = wallQueue.poll();
                if (wall != null) {
                    logger.appendText("\nPolled a wall!");
                    gridArray[(int) wall.getX()][(int) wall.getY()].setWall(wall);
                    logger.appendText("Added wall to "+ wall.getX() + " " + wall.getY());
                    arena.addWallToQueue(wall);
                    arena.incrementWallCount();

                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });

        scoreThread.start();
        wallBuilderThread.start();
        robotSpawner.spawn(executorService);
    }

    @Override
    public void stop() throws IllegalStateException {
        scoreThread.interrupt();
    }


}
