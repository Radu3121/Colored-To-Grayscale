import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Thread.sleep;

public class Consumer implements Runnable, Constants {

    private final Buffer buffer; // instanta comuna de buffer cu cea din Producer
    private BufferedImage produced = null; // variabila folosita pentru primirea rezultatului
    									   // in urma produce(); 
    public static BufferedImage finalImage = null; // imaginea reconstruita din bucatile venite
    											   // venite de la Producer

    public Consumer(Buffer buffer) {
        this.buffer = buffer; // se primeste instanta de buffer
    }

    @Override
    public void run() { // se executa thread-ul consumer
        int height = Buffer.getHeight(); // se memoreaza dimensiunile necesare pastraii imaginii sursa
        int width = Buffer.getWidth();

        finalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // folosit pentru aculuarea de bucati
                                                                                   // venite de la producer
        Graphics2D g2d = finalImage.createGraphics();
        for (int i = 0; i < PARTS; i++) {
            produced = buffer.consume();
            g2d.drawImage(produced, 0, i * (height / PARTS), null); // se concateneaza pe verticala partile
                                                                    // de imagine venite de la producer
                                                                    
            System.out.println("A fost consumata bucata " + (i + 1) + ".");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
