package sample;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class Controller implements Initializable  {

    @FXML
    private Canvas canvas;

    @FXML
    private TextField numDots;

    @FXML
    private Button startButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button stopButton;

    @FXML
    private Label results;


    drawerTask task;
    Random randomG = new Random();

    GraphicsContext gc;

    @FXML
    void handleStart(ActionEvent event) {
        //GraphicsContext gc = canvas.getGraphicsContext2D();
        drawShapes(gc);
        task = new drawerTask(gc, parseInt(numDots.getText()));
        task.setOnSucceeded(event2 ->{
                Float var = (Float) task.getValue();
                results.setText("Wynik całki = " + Float.toString(var));

        });
        progressBar.progressProperty().bind(task.progressProperty());

        new Thread(task).start();
    }

    @FXML
    void handleStop(ActionEvent event) {
        if (task != null) {
            task.cancel();
        }
    }

    private void drawShapes(GraphicsContext gc)
    { gc.setFill(Color.BLACK);
        gc.fillRect(gc.getCanvas().getLayoutX() + 8,
                gc.getCanvas().getLayoutY(),
                gc.getCanvas().getWidth(),
                gc.getCanvas().getHeight());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = canvas.getGraphicsContext2D();
        drawShapes(gc);
    }
}
