package edu.curtin.saed.assignment1;


import javafx.scene.canvas.*;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A JavaFX GUI element that displays a grid on which you can draw images, text and lines.
 */
public class JFXArena extends Pane
{
    // Represents an image to draw, retrieved as a project resource.
    private static final String[] IMAGE_FILE = {"1554047213.png", "rg1024-isometric-tower.png", "181478.png", "181479.png"};
    private Image robot1, citadel1, wall1, wall2;

    // The following values are arbitrary, and you may need to modify them according to the
    // requirements of your application.
    private int gridWidth = 9;
    private int gridHeight = 9;
    private int robotCount = 0; //total robots in the screen
    private int wallCount = 0; //total walls in screen

    private double gridSquareSize; // Auto-calculated
    private Canvas canvas; // Used to provide a 'drawing surface'.
    private Citadel citadel;

    private List<ArenaListener> listeners = null;
    private BlockingQueue<Robot> robotQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<Wall> wallQueue = new ArrayBlockingQueue<>(10);
    //Initializing Grid Array
    private GridSquare[][] gridArray = new GridSquare[gridWidth][gridHeight];

    /**
     * Creates a new arena object, loading the robot image and initialising a drawing surface.
     */
    public JFXArena(Citadel citadel)
    {
        // Here's how (in JavaFX) you get an Image object from an image file that's part of the
        // project's "resources". If you need multiple different images, you can modify this code
        // accordingly.

        // (NOTE: _DO NOT_ use ordinary file-reading operations here, and in particular do not try
        // to specify the file's path/location. That will ruin things if you try to create a
        // distributable version of your code with './gradlew build'. The approach below is how a
        // project is supposed to read its own internal resources, and should work both for
        // './gradlew run' and './gradlew build'.)

        String s0 = IMAGE_FILE[0];
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(s0)) {
            if (is == null) {
                throw new AssertionError("Cannot find image file " + s0);
            }
            robot1 = new Image(is);

        } catch (IOException e) {
            throw new AssertionError("Cannot load image file " + s0, e);
        }

        String s1 = IMAGE_FILE[1];
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(s1)) {
            if (is == null) {
                throw new AssertionError("Cannot find image file " + s1);
            }
            citadel1 = new Image(is);

        } catch (IOException e) {
            throw new AssertionError("Cannot load image file " + s1, e);
        }

        String s2 = IMAGE_FILE[2];
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(s2)) {
            if (is == null) {
                throw new AssertionError("Cannot find image file " + s2);
            }
            wall1 = new Image(is);

        } catch (IOException e) {
            throw new AssertionError("Cannot load image file " + s2, e);
        }

        String s3 = IMAGE_FILE[3];
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(s3)) {
            if (is == null) {
                throw new AssertionError("Cannot find image file " + s3);
            }
            wall2 = new Image(is);

        } catch (IOException e) {
            throw new AssertionError("Cannot load image file " + s3, e);
        }


        // Fill the array with false values
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                gridArray[i][j] = new GridSquare(false);
            }
        }

        this.citadel = citadel;

        //this.wallSpawner = wallSpawner;
        canvas = new Canvas();
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        getChildren().add(canvas);
    }


    /**
     * Moves a robot image to a new grid position. This is highly rudimentary, as you will need
     * many different robots in practice. This method currently just serves as a demonstration.
     */
    public void setRobotPosition(double x, double y)
    {
        //robotX = x;
        //robotY = y;
        requestLayout();
    }

    /**
     * Adds a callback for when the user clicks on a grid square within the arena. The callback
     * (of type ArenaListener) receives the grid (x,y) coordinates as parameters to the
     * 'squareClicked()' method.
     */
    public void addListener(ArenaListener newListener)
    {
        if(listeners == null)
        {
            listeners = new LinkedList<>();
            setOnMouseClicked(event ->
            {
                int gridX = (int)(event.getX() / gridSquareSize);
                int gridY = (int)(event.getY() / gridSquareSize);

                if(gridX < gridWidth && gridY < gridHeight)
                {
                    for(ArenaListener listener : listeners)
                    {
                        listener.squareClicked(gridX, gridY);
                    }
                }
            });
        }
        listeners.add(newListener);
    }


    /**
     * This method is called in order to redraw the screen, either because the user is manipulating
     * the window, OR because you've called 'requestLayout()'.
     *
     * You will need to modify the last part of this method; specifically the sequence of calls to
     * the other 'draw...()' methods. You shouldn't need to modify anything else about it.
     */
    @Override
    public void layoutChildren()
    {
        super.layoutChildren();
        GraphicsContext gfx = canvas.getGraphicsContext2D();
        gfx.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());

        // First, calculate how big each grid cell should be, in pixels. (We do need to do this
        // every time we repaint the arena, because the size can change.)
        gridSquareSize = Math.min(
            getWidth() / (double) gridWidth,
            getHeight() / (double) gridHeight);

        double arenaPixelWidth = gridWidth * gridSquareSize;
        double arenaPixelHeight = gridHeight * gridSquareSize;


        // Draw the arena grid lines. This may help for debugging purposes, and just generally
        // to see what's going on.
        gfx.setStroke(Color.DARKGREY);
        gfx.strokeRect(0.0, 0.0, arenaPixelWidth - 1.0, arenaPixelHeight - 1.0); // Outer edge

        for(int gridX = 1; gridX < gridWidth; gridX++) // Internal vertical grid lines
        {
            double x = (double) gridX * gridSquareSize;
            gfx.strokeLine(x, 0.0, x, arenaPixelHeight);
        }

        for(int gridY = 1; gridY < gridHeight; gridY++) // Internal horizontal grid lines
        {
            double y = (double) gridY * gridSquareSize;
            gfx.strokeLine(0.0, y, arenaPixelWidth, y);
        }

        // Invoke helper methods to draw things at the current location.
        // ** You will need to adapt this to the requirements of your application. **
        for (int i = 0; i < getRobotCount(); i++) {
            try {
                Robot robot = robotQueue.take();
                if (! robot.isDestroyed()) {
                    drawImage(gfx, robot1, robot.getRobotX(), robot.getRobotY());
                    drawLabel(gfx, Integer.toString(robot.getId()), robot.getRobotX(), robot.getRobotY());
                    robotQueue.add(robot);
                }
            } catch (InterruptedException e) {
                System.out.println(e.toString());
            }
        }

        for (int i = 0; i < getWallCount(); i++) {
            try {
                Wall wall = wallQueue.take();
                if (! wall.isDestroyed()){
                    if (wall.getHealth() > 1){
                        drawImage(gfx, wall1, wall.getX(), wall.getY());
                    } else {
                        drawImage(gfx, wall2, wall.getX(), wall.getY());
                    }
                    wallQueue.add(wall);
                }

            } catch (InterruptedException e) {
                System.out.println(e.toString());
            }
        }


        drawImage(gfx, citadel1, citadel.getPosX(), citadel.getPosY());

    }

    /**
     * Draw an image in a specific grid location. *Only* call this from within layoutChildren().
     *
     * Note that the grid location can be fractional, so that (for instance), you can draw an image
     * at location (3.5,4), and it will appear on the boundary between grid cells (3,4) and (4,4).
     *
     * You shouldn't need to modify this method.
     */
    private void drawImage(GraphicsContext gfx, Image image, double gridX, double gridY)
    {
        // Get the pixel coordinates representing the centre of where the image is to be drawn.
        double x = (gridX + 0.5) * gridSquareSize;
        double y = (gridY + 0.5) * gridSquareSize;

        // We also need to know how "big" to make the image. The image file has a natural width
        // and height, but that's not necessarily the size we want to draw it on the screen. We
        // do, however, want to preserve its aspect ratio.
        double fullSizePixelWidth = robot1.getWidth();
        double fullSizePixelHeight = robot1.getHeight();

        double displayedPixelWidth, displayedPixelHeight;
        if(fullSizePixelWidth > fullSizePixelHeight)
        {
            // Here, the image is wider than it is high, so we'll display it such that it's as
            // wide as a full grid cell, and the height will be set to preserve the aspect
            // ratio.
            displayedPixelWidth = gridSquareSize;
            displayedPixelHeight = gridSquareSize * fullSizePixelHeight / fullSizePixelWidth;
        }
        else
        {
            // Otherwise, it's the other way around -- full height, and width is set to
            // preserve the aspect ratio.
            displayedPixelHeight = gridSquareSize;
            displayedPixelWidth = gridSquareSize * fullSizePixelWidth / fullSizePixelHeight;
        }

        // Actually put the image on the screen.
        gfx.drawImage(image,
            x - displayedPixelWidth / 2.0,  // Top-left pixel coordinates.
            y - displayedPixelHeight / 2.0,
            displayedPixelWidth,              // Size of displayed image.
            displayedPixelHeight);
    }


    /**
     * Displays a string of text underneath a specific grid location. *Only* call this from within
     * layoutChildren().
     *
     * You shouldn't need to modify this method.
     */
    private void drawLabel(GraphicsContext gfx, String label, double gridX, double gridY)
    {
        gfx.setTextAlign(TextAlignment.CENTER);
        gfx.setTextBaseline(VPos.TOP);
        gfx.setStroke(Color.BLUE);
        gfx.strokeText(label, (gridX + 0.5) * gridSquareSize, (gridY + 1.0) * gridSquareSize);
    }

    /**
     * Draws a (slightly clipped) line between two grid coordinates.
     *
     * You shouldn't need to modify this method.
     */
    /*
    private void drawLine(GraphicsContext gfx, double gridX1, double gridY1,
                                               double gridX2, double gridY2)
    {
        gfx.setStroke(Color.RED);

        // Recalculate the starting coordinate to be one unit closer to the destination, so that it
        // doesn't overlap with any image appearing in the starting grid cell.
        final double radius = 0.5;
        double angle = Math.atan2(gridY2 - gridY1, gridX2 - gridX1);
        double clippedGridX1 = gridX1 + Math.cos(angle) * radius;
        double clippedGridY1 = gridY1 + Math.sin(angle) * radius;

        gfx.strokeLine((clippedGridX1 + 0.5) * gridSquareSize,
                       (clippedGridY1 + 0.5) * gridSquareSize,
                       (gridX2 + 0.5) * gridSquareSize,
                       (gridY2 + 0.5) * gridSquareSize);
    }*/

    public int getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
    }

    public int getRobotCount(){
        return robotCount;
    }

    public int getWallCount(){
        return wallCount;
    }

    public void incrementRobotCount() {robotCount += 1;}

    public void addRobotToQueue(Robot robot) {robotQueue.add(robot);}

    public void addToWallQueue(Wall wall) {
        wallQueue.add(wall);
        wallCount += 1;
    }

    public GridSquare[][] getGridArray() {return gridArray;}
}
