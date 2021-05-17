import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class GrayScale implements Runnable, Constants {

    private static final CyclicBarrier barrier = new CyclicBarrier(PARTS); // folosita pentru a sincroniza thread-urile 
    																	   // necesare procesarii
    private static final BufferedImage image = Consumer.finalImage; // o copie a imaginii obtinute dupa citire
    private static BufferedImage grayImage; // imaginea (resursa comuna) ca va rezulta in urma executiei
    private final int id; // identificatorul unic al fiecarui thread de procesare
    private String pathOut; // calea catre destinatie ouput

    public GrayScale(int id, String pathOutput) { // se atribuie ficarui thread un id corespunzator si calea 
    											  // catre output
        this.id = id;
        pathOut = pathOutput;
    }

    @Override
    public void run() { // se incepe procesarea imaginii
        if (id == 0) { // thread-ul 0 stabilieste o resursa comuna in care se vor aduce modificari imaginii
            grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = grayImage.createGraphics();
            g2d.drawImage(image, 0, 0, Color.WHITE, null);
        }

        try { // se asteapta ca toate thread-uril sa aiba resusa comuna vizibila
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        int start = id * (image.getHeight() / Constants.PARTS); // se imparte work-load-ul intre thread-uri
        int end = (id + 1) * (image.getHeight() / Constants.PARTS);
        end = Math.min(end, image.getHeight()); // daca thread-ul cu id-ul cel mai mare ajunge sa aiba un 
        										// end mai mare decat numarul maxim de linii al pozei
        										// atunci end-ul sau va fi inaltimea imaginii sursa

        // se realizeaza trecerea din RGB in Gray-scale prin metoda Average, la nivel de pixel
        for (int i = start; i < end; i++) {
            for (int j = 0; j < grayImage.getWidth(); j++) {
                Color c = new Color(grayImage.getRGB(j, i)); // se acceseaza fiecare pixel si codul RGB asociat
                int avg = (c.getRed() + c.getGreen() + c.getBlue()) / 3; // se face media intre R G si B
                Color newColor = new Color(avg, avg, avg); // se asociaza avg pentru R G si B
                grayImage.setRGB(j, i, newColor.getRGB()); // se scrie in imaginea rezultat (resusa comuna)
                										   // "culoare" pixelului obtinuta prin avg
            }
        }

        try { // se asteapta ca toate thread-urile sa isi termine de modificat partea asociata
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        if (id == 0) { // thread-ul 0 scrie imaginea modificata in fisier
            try {
                ImageIO.write(grayImage, "BMP", new File(pathOut));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
