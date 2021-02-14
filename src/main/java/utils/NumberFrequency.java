package utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author namvdo
 */
public class NumberFrequency {
    private static final Logger logger = Logger.getLogger(NumberFrequency.class.getName());

    /**
     * Output number of occurrence of each unique letter in the given letters sequence input.
     *
     * @param letters the letters want to find the frequency of their characters
     */
    public static void outputFrequency(String letters) {
        String[] numbers = letters.split("-");
        Map<String, Integer> fre = new HashMap<>(5);
        for (String number : numbers) {
            fre.putIfAbsent(number, 1);
            fre.computeIfPresent(number, (k, n) -> n + 1);
        }
        logger.log(Level.INFO, "\n{0}", toString(fre, numbers.length));
    }

    private static String toString(Map<String, Integer> map, int size) {
        StringBuilder str = new StringBuilder();
        Map<String, Integer> newMap = new LinkedHashMap<>(5);
        map.entrySet()
                .stream().sorted(Map.Entry.comparingByValue())
                .forEach(consumer -> newMap.put(consumer.getKey(), consumer.getValue()));
        for (Map.Entry<String, Integer> entry : newMap.entrySet()) {
            String percent = String.format("%.2f", entry.getValue() / (float) size) + "%";
            str.append(entry.getKey()).append(" : ").append(entry.getValue()).append(" : ").append(percent).append("\n");
        }
        return str.toString();
    }

    /**
     * Shift and then write characters to file.
     * @param input characters to perform shifting
     * @param shift the shift number
     * @param fileName the file name
     * @throws FileNotFoundException in case the given fileName cannot be found.
     */
    public static void shiftAndWriteToFile(String input, int shift, String fileName) throws FileNotFoundException {
        int[] numbers = Arrays.stream(input.split("-")).mapToInt(Integer::parseInt).toArray();
        AtomicInteger atomic = new AtomicInteger(0);
        Arrays.stream(numbers).forEach(num -> {
            numbers[atomic.get()] = (num + shift) % 26;
            atomic.incrementAndGet();
        });
        Path path = Paths.get(fileName);
        try (PrintWriter printWriter = new PrintWriter(path.toFile())) {
            for (int num : numbers) {
                printWriter.write(String.valueOf(num));
                printWriter.write("-");
            }
            printWriter.flush();
        }
    }
}
