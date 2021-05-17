import static java.lang.Thread.sleep;

public class Producer implements Runnable, Constants{
    private final Buffer buffer; // instanta comuna de Buffer cu cea din Consumer

    public Producer(Buffer buffer) {
        this.buffer = buffer; // se primeste instanta de buffer
    }

    @Override
    public void run() { // se executa thread-ul producer
        for (int i = 0; i < PARTS; i++) { // produce 4 bucati de imagine dupa cum specifica cerinta
            buffer.produce(i); // se apeleaza metoda de producere
            System.out.println("A fost produsa bucata " + (i + 1) + ".");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
