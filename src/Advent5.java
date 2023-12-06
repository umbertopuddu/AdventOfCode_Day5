import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Advent5 {

    static long currMin;
    static long[] data;
    static String[][] table;
    static long[][] soil;
    static long[][] fertilizer;
    static long[][] water;
    static long[][] light;
    static long[][] temperature;
    static long[][] humidity;
    static long[][] location;

    static Object[][] all = { {"seed", "soil", "fertilizer", "water", "light", "temperature", "humidity"},
            {soil, fertilizer, water, light, temperature, humidity, location} };

    public static void main(String[] args) {
        try {
            table = convertFileToArrayOfArrays("src/table.txt");
            for (int i = 0; i < all[0].length; i++) {
                all[1][i] = initTable((String)all[0][i], table);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        data = new long[table[0].length-1];
        for (int i = 1; i < table[0].length; i++) {
            data[i-1] = Long.parseLong(table[0][i]);
        }

        long[] resultOne = new long[data.length];

        for(int i = 0; i < data.length; i++){
            resultOne[i] = position(data[i], all);
        }

        System.out.println("Part 1: " + minimum(resultOne));

        for (int i = 0; (i + 1) < data.length; i += 2) {
            for(long k = data[i]; k < data[i] + data[i+1]; k++){
                if((i == 0 && k == data[i]) || (position(k, all) < currMin)){
                    currMin = position(k, all);
                }
            }
            System.out.println("Part 2 - " + ((i/2) + 1) + "/" + data.length/2 + " step done!");
        }

        System.out.println("Part 2: " + currMin);
    }

    private static long position(long initial, Object[][] board){
        for(int j = 0; j < all[0].length; j++){
            initial = convert(initial, (long[][]) board[1][j]);
        }
        return initial;

    }

    private static long minimum(long[] arr){
        int min = 0;
        for(int i = 1; i < arr.length; i++){
            if(arr[i] < arr[min]){
                min = i;
            }
        }
        return arr[min];
    }

    private static String[][] convertFileToArrayOfArrays(String filePath) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(filePath)));
        String[] lines = text.split("\n");
        String[][] arrayOfArrays = new String[lines.length][];

        for (int i = 0; i < lines.length; i++) {
            // Split on one or more spaces
            arrayOfArrays[i] = lines[i].trim().split("\\s+");
        }

        return arrayOfArrays;
    }

    private static long[][] initTable(String to, String[][] table) {
        int line = -1;
        int limit = table.length;

        List<List<Long>> tempList = new ArrayList<>();

        // Find the start line of the relevant data
        for (int i = 0; i < table.length; i++) {
            for (int k = 0; k < table[i].length; k++) {
                if (table[i][k].contains(to + "-to")) {
                    line = i + 1;
                    break;
                }
            }
            if (line != -1) break;
        }

        // Find the limit for the relevant data
        for (int i = line; i < table.length && line != -1; i++) {
            if (table[i][0].contains("-to")) {
                limit = i - 1;
                break;
            }
        }

        // Populate the tempList with data from the table
        for (int i = 0; i < limit - line; i++) {
            List<Long> row = new ArrayList<>();
            for (int k = 0; k < table[line + i].length; k++) {
                row.add(Long.parseLong(table[line + i][k]));
            }
            tempList.add(row);
        }

        // Convert tempList to long[][]
        long[][] storage = new long[tempList.size()][tempList.get(0).size()];
        for(int i = 0; i < tempList.size(); i++) {
            for(int k = 0; k < tempList.get(i).size(); k++){
                storage[i][k] = tempList.get(i).get(k);
            }
        }

        return storage;
    }


    private static long convert(long current, long[][] grid){
        long result = current; // Initialize result to a default value, such as current

        for (long[] longs : grid) {
            if (current >= longs[1] && current < (longs[1] + longs[2])) {
                result = Math.abs(current - longs[1]) + longs[0];
                break;
            }
        }

        return result;
    }


}
