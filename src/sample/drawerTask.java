package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

public class drawerTask extends Task {
    GraphicsContext gc;
    int numDots;
    int min = -8, max = 8;
    Random randomG = new Random();

    public drawerTask(GraphicsContext gc, int numDots){
        this.gc = gc;
        this.numDots = numDots;
    }

    @Override
    protected Object call() throws Exception {
        while (true) {
            BufferedImage bi = new BufferedImage(600, 700, BufferedImage.TYPE_INT_RGB);
            int k = 0;

            for (int i = 0; i < numDots; i++) {
                if (isCancelled()) {
                    break;
                }

                double x = min + (max - min) * randomG.nextDouble();
                double y = min + (max - min) * randomG.nextDouble();

                double pixelX = ((gc.getCanvas().getWidth()) * (x - min) / (max - min));
                double pixelY = ((gc.getCanvas().getHeight()) * (y - min) / (max - min));

                if (Equation.solution(x, y)) {
                    k++;
                    bi.setRGB((int) pixelX, (int) (-pixelY + gc.getCanvas().getHeight()), java.awt.Color.YELLOW.getRGB());

                    if (i % 100 == 0) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                              gc.getPixelWriter().setColor((int) pixelX + 8,(int)gc.getCanvas().getHeight() -  (int) pixelY, Color.YELLOW);
                               //gc.drawImage(SwingFXUtils.toFXImage(bi, null), 0,0);
                            }
                        });
                    }
                } else {
                    bi.setRGB((int) pixelX, (int) (-pixelY + gc.getCanvas().getHeight()), java.awt.Color.BLUE.getRGB());

                    if (i % 100 == 0) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                             gc.getPixelWriter().setColor((int) pixelX + 8, (int)gc.getCanvas().getHeight() - (int) pixelY, Color.BLUE);
                             // gc.drawImage(SwingFXUtils.toFXImage(bi, null), 0,0);
                            }
                        });
                    }
                }
                updateProgress(i, numDots);
            }
            return (k * 256.0f) / numDots * 1.0f; //  P*k/n
        }
    }
}
