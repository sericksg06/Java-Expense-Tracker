import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpenseManager {
    private List<Expense> expenses;
    private int nextId;

    public ExpenseManager() {
        this.expenses = new ArrayList<>();
        this.nextId = 1;
    }

    public void addExpense(double amount, String category, String date, String description) {
        expenses.add(new Expense(nextId++, amount, category, date, description));
    }

    public void updateExpense(int id, double amount, String category, String date, String description) {
        for (Expense e : expenses) {
            if (e.getId() == id) {
                e.setAmount(amount);
                e.setCategory(category);
                e.setDate(date);
                e.setDescription(description);
                break;
            }
        }
    }

    public void deleteExpense(int id) {
        expenses.removeIf(e -> e.getId() == id);
    }

    public List<Expense> getAllExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        this.nextId = expenses.stream().mapToInt(Expense::getId).max().orElse(0) + 1;
    }

    public double getTotalExpenses() {
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    public Map<String, Double> getCategorySummary() {
        return expenses.stream().collect(
            Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(Expense::getAmount))
        );
    }

    public List<Expense> search(String query) {
        return expenses.stream()
            .filter(e -> e.getDescription().toLowerCase().contains(query.toLowerCase()) || 
                         e.getCategory().toLowerCase().contains(query.toLowerCase()))
            .collect(Collectors.toList());
    }

    public String getTopCategory() {
        return getCategorySummary().entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("N/A");
    }
}