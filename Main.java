public class Main {
    public static String pathIn;
    public static String pathOut;
    private static long start;
    private static long end;

    public Main(String arg0, String arg1) {
        pathIn = arg0;
        pathOut = arg1;

        Buffer buff = new Buffer(pathIn);
        Thread []threads = new Thread[2]; // threadurile producer si consumer

        threads[0] = new Thread(new Producer(buff)); // se creeaza thread-ul producer
        threads[1] = new Thread(new Consumer(buff)); // se creeaza thread-ul consumer
        
        start = System.nanoTime();

        for (int i = 0; i < 2; i++) {  // se pornesc thread-urile de citire si consumare
            threads[i].start();
        }

        for (int i = 0; i < 2; i++) { // se asteapta thread-urile de producer si consumer
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        end = System.nanoTime();
        
        System.out.println("\nTimpul de executie al citirii este: " + ((end - start) / 1000000) + "ms." );
        System.out.println("Acest timp este puternic influentat de sleep!\n");
        
        Thread []processingThreads = new Thread[4]; // thread-uri procesare

        start = System.nanoTime();
        
        for (int i = 0; i < 4; i++) { // se pornesc thread-urile de procesare
            processingThreads[i] = new Thread(new GrayScale(i, pathOut));
            processingThreads[i].start();
        }

        for (int i = 0; i < 4; i++) { // se asteapta thread-urile de procesare
            try {
                processingThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        end = System.nanoTime();
        
        System.out.println("Timpul de executie al procesarii este: " + ((end - start) / 1000000) + "ms." );

    }

    public static void main(String[] args) {
    	try {
    		new Main(args[0], args[1]);
    	} catch (ArrayIndexOutOfBoundsException e) {
    		System.out.println("Numar insuficient de parametrii\nExecutia trebuie sa fie de forma \n"
    				+ "java Main.java sursa_input destinatie_output");
    	}
        
    }
}
