import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //Количество потоков равно количеству генерируемых маршрутов и равно 1000
        final int numberOfThreads = 1000;
        final ExecutorService threadPool = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            synchronized (sizeToFreq) {
                Callable<String> myCallable = () -> {
                    String valve = generateRoute("RLRFR", 100);
                    return valve;
                };

                final Future<String> task = threadPool.submit(myCallable);
                final String resultOfTask = task.get();

                int characterFrequency = resultOfTask.length() - resultOfTask.replace("R", "").length();
                int numberOfCharacters = 1;

                if (sizeToFreq.containsKey(characterFrequency)) {
                    sizeToFreq.put(characterFrequency, sizeToFreq.get(characterFrequency) + 1);
                } else {
                    sizeToFreq.put(characterFrequency, numberOfCharacters);
                }
            }
        }
        threadPool.shutdown();

        Map.Entry<Integer, Integer> maxEntry = null;
        List<Map.Entry<Integer, Integer>> entrys = new ArrayList<>(); //потратил ресурсы памяти на список,
                                                                      //но код стал читабельней
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }

            if (entry.getValue() != 1) {
                entrys.add(entry);
            }
        }

        System.out.println("Самое частое количество повторений = " + maxEntry.getKey()
                + " (встретилось " + maxEntry.getValue() + " раз)");

        System.out.println("Другие размеры:");
        for (int i = 0; i < entrys.size(); i++)
        System.out.println("- " + entrys.get(i).getKey() + " (" + entrys.get(i).getValue() + " раз)");
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}