import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String FILE_NAME = "expenses.csv";

    public static void saveToFile(List<Expense> expenses) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Expense e : expenses) {
                writer.println(e.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }

    public static List<Expense> loadFromFile() {
        List<Expense> expenses = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return expenses;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    expenses.add(new Expense(
                        Integer.parseInt(parts[0]),
                        Double.parseDouble(parts[1]),
                        parts[2],
                        parts[3],
                        parts[4]
                    ));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading file: " + e.getMessage());
        }
        return expenses;
    }
}