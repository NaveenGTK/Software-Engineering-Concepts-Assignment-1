package edu.curtin.saed.assignment1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App extends Application 
{
    private Thread scoreThread;
    public static void main(String[] args) 
    {
        launch();        
    }
    
    @Override
    public void start(Stage stage) 
    {
        stage.setTitle("Example App (JavaFX)");
        JFXArena arena = new JFXArena();
        //Citadel citadel = new Citadel(5,5);
        TextArea logger = new TextArea();
        ScoreController scoreController = new ScoreController();
        ExecutorService executorService = Executors.newFixedThreadPool(5); // Adjust thread pool size as needed
        RobotSpawner robotSpawner = new RobotSpawner(arena, logger);

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
        robotSpawner.spawn(executorService);
    }

    @Override
    public void stop() throws IllegalStateException {
        scoreThread.interrupt();
    }


}
