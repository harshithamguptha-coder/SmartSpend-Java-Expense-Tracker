
import java.util.Scanner;

public class BudgetManager {

    private Budget[] budgets = new Budget[100];
    private int budgetCount = 0;

    // Set overall monthly budget
    public void setMonthlyBudget(Scanner scanner) {
        if (budgetCount == budgets.length) {
            System.out.println("Budget list is full.");
            return;
        }

        System.out.println("Enter month (YYYY-MM):");
        String month = scanner.nextLine().trim();

        if (!month.matches("[0-9]{4}-(0[1-9]|1[0-2])") || month.startsWith("0000")) {
            System.out.println("Invalid month. Use YYYY-MM.");
            return;
        }

        System.out.println("Enter monthly budget amount:");
        double amount;

        try {
            amount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException exception) {
            System.out.println("Please enter a numeric amount.");
            return;
        }

        if (!Double.isFinite(amount) || amount <= 0) {
            System.out.println("Budget amount must be positive.");
            return;
        }

        // Prevent duplicate overall budgets
        if (budgetExists(month, "Overall")) {
            System.out.println("Monthly budget already exists for " + month);
            return;
        }

        Budget budget = new Budget(month, "Overall", amount);
        budgets[budgetCount] = budget;
        budgetCount++;

        System.out.println("Monthly budget saved successfully!");
    }

    // Set budget for a particular category
    public void setCategoryBudget(Scanner scanner) {
        if (budgetCount == budgets.length) {
            System.out.println("Budget list is full.");
            return;
        }

        System.out.println("Enter month (YYYY-MM):");
        String month = scanner.nextLine().trim();

        if (!month.matches("[0-9]{4}-(0[1-9]|1[0-2])") || month.startsWith("0000")) {
            System.out.println("Invalid month. Use YYYY-MM.");
            return;
        }

        System.out.println("Enter category (Food, Travel, Shopping, Education, Others):");
        String input = scanner.nextLine().trim();

        String[] categories = {"Food", "Travel", "Shopping", "Education", "Others"};
        String category = null;

        for (String item : categories) {
            if (item.equalsIgnoreCase(input)) {
                category = item;
                break;
            }
        }

        if (category == null) {
            System.out.println("Invalid category.");
            return;
        }

        System.out.println("Enter category budget amount:");
        double amount;

        try {
            amount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException exception) {
            System.out.println("Please enter a numeric amount.");
            return;
        }

        if (!Double.isFinite(amount) || amount <= 0) {
            System.out.println("Budget amount must be positive.");
            return;
        }

        // Prevent duplicate category budgets
        if (budgetExists(month, category)) {
            System.out.println("Category budget already exists for this month.");
            return;
        }

        Budget budget = new Budget(month, category, amount);
        budgets[budgetCount] = budget;
        budgetCount++;

        System.out.println("Category budget saved successfully!");
    }

    // Display all saved budgets
    public void displayBudgets() {
        if (budgetCount == 0) {
            System.out.println("No budgets to display.");
            return;
        }

        System.out.println("===== Saved Budgets =====");

        for (int i = 0; i < budgetCount; i++) {
            Budget budget = budgets[i];

            System.out.println("Month: " + budget.getMonth());
            System.out.println("Category: " + budget.getCategory());
            System.out.println("Amount: Rs. " + budget.getAmount());
            System.out.println("--------------------");
        }
    }

    // Calculate remaining overall monthly budget
    public void calculateRemainingBudget(String month, ExpenseManager expenseManager) {
        for (int i = 0; i < budgetCount; i++) {
            Budget budget = budgets[i];

            if (budget.getMonth().equals(month)
                    && budget.getCategory().equals("Overall")) {

                double totalExpenses = expenseManager.calculateMonthlyExpenses(month);
                double remaining = budget.getAmount() - totalExpenses;

                System.out.println("Month: " + month);
                System.out.println("Monthly Budget: Rs. " + budget.getAmount());
                System.out.println("Total Expenses: Rs. " + totalExpenses);
                System.out.println("Remaining Budget: Rs. " + remaining);
                return;
            }
        }

        System.out.println("No monthly budget found for " + month);
    }

    // Check overall and category budget limits
    public void checkBudgetLimit(String month, ExpenseManager expenseManager) {
        boolean found = false;

        for (int i = 0; i < budgetCount; i++) {
            Budget budget = budgets[i];

            if (budget.getMonth().equals(month)) {
                found = true;

                double spent;

                if (budget.getCategory().equals("Overall")) {
                    spent = expenseManager.calculateMonthlyExpenses(month);
                } else {
                    spent = expenseManager.calculateCategoryExpenses(
                        month, budget.getCategory()
                    );
                }

                System.out.println("Category: " + budget.getCategory());
                System.out.println("Budget: Rs. " + budget.getAmount());
                System.out.println("Spent: Rs. " + spent);

                if (spent > budget.getAmount()) {
                    System.out.println("Warning! Budget limit exceeded.");
                } else {
                    System.out.println("You are within your budget.");
                }

                System.out.println("--------------------");
            }
        }

        if (!found) {
            System.out.println("No budgets found for " + month);
        }
    }

    // Check whether a budget already exists
    private boolean budgetExists(String month, String category) {
        for (int i = 0; i < budgetCount; i++) {
            Budget budget = budgets[i];

            if (budget.getMonth().equals(month)
                    && budget.getCategory().equals(category)) {
                return true;
            }
        }

        return false;
    }
}
