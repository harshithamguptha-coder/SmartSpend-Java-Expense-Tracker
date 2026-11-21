import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class ExpenseManager {
    private static final String[] CATEGORIES = {"Food", "Travel", "Shopping", "Education", "Others"};
    private Expense[] expenses = new Expense[100];
    private int expenseCount = 0;

    public void addExpense(Scanner scanner) {
        if (expenseCount == expenses.length) {
            System.out.println("Expense list is full.");
            return;
        }

        System.out.println("Enter amount:");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException exception) {
            System.out.println("Enter a numeric amount, such as 125.50.");
            return;
        }
        if (!Double.isFinite(amount) || amount <= 0) {
            System.out.println("Amount must be a positive, finite number.");
            return;
        }

        System.out.println("Enter category (Food, Travel, Shopping, Education, or Others):");
        String category = findCategory(scanner.nextLine());
        if (category == null) {
            System.out.println("Choose Food, Travel, Shopping, Education, or Others.");
            return;
        }

        System.out.println("Enter date (YYYY-MM-DD):");
        String date = scanner.nextLine().trim();
        if (!isValidDate(date)) {
            System.out.println("Enter a valid date in YYYY-MM-DD format.");
            return;
        }

        System.out.println("Enter description:");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) {
            System.out.println("Description cannot be blank.");
            return;
        }

        Expense expense = new Expense(amount, category, date, description);
        expenses[expenseCount] = expense;
        expenseCount++;
        System.out.println("Expense stored: " + expense.getDescription());
    }

    public void displayAllExpenses() {
        if (expenseCount == 0) {
            System.out.println("No expenses to display.");
            return;
        }

        for (int i = 0; i < expenseCount; i++) {
            printExpense(expenses[i]);
        }
    }

    public void deleteExpense(Scanner scanner) {
        if (expenseCount == 0) {
            System.out.println("No expenses to delete.");
            return;
        }

        System.out.println("===== Expenses =====");
        for (int i = 0; i < expenseCount; i++) {
            System.out.println("Expense " + (i + 1) + ":");
            printExpense(expenses[i]);
        }

        System.out.println("Enter the expense number or exact description to delete:");
        String selection = scanner.nextLine().trim();
        int selectedIndex;

        try {
            selectedIndex = Integer.parseInt(selection) - 1;
            if (selectedIndex < 0 || selectedIndex >= expenseCount) {
                System.out.println("Invalid expense number.");
                return;
            }
        } catch (NumberFormatException exception) {
            int[] matchingIndexes = new int[expenseCount];
            int matchCount = 0;

            for (int i = 0; i < expenseCount; i++) {
                if (expenses[i].getDescription().equalsIgnoreCase(selection)) {
                    matchingIndexes[matchCount] = i;
                    matchCount++;
                }
            }

            if (matchCount == 0) {
                System.out.println("No expense found with that description.");
                return;
            }

            if (matchCount == 1) {
                selectedIndex = matchingIndexes[0];
            } else {
                System.out.println("Multiple expenses have that description:");
                for (int i = 0; i < matchCount; i++) {
                    int index = matchingIndexes[i];
                    System.out.println("Expense " + (index + 1) + ":");
                    printExpense(expenses[index]);
                }

                System.out.println("Enter the expense number to delete:");
                String matchingSelection = scanner.nextLine().trim();
                try {
                    selectedIndex = Integer.parseInt(matchingSelection) - 1;
                } catch (NumberFormatException invalidSelection) {
                    System.out.println("Invalid expense number.");
                    return;
                }

                boolean isMatchingExpense = false;
                for (int i = 0; i < matchCount; i++) {
                    if (matchingIndexes[i] == selectedIndex) {
                        isMatchingExpense = true;
                        break;
                    }
                }
                if (!isMatchingExpense) {
                    System.out.println("Invalid expense number.");
                    return;
                }
            }
        }

        System.out.println("Expense selected for deletion:");
        printExpense(expenses[selectedIndex]);
        System.out.println("Are you sure you want to delete this expense? (y/n):");
        String confirmation = scanner.nextLine().trim();
        if (!confirmation.equalsIgnoreCase("y") && !confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Expense deletion cancelled.");
            return;
        }

        for (int i = selectedIndex; i < expenseCount - 1; i++) {
            expenses[i] = expenses[i + 1];
        }
        expenses[expenseCount - 1] = null;
        expenseCount--;
        System.out.println("Expense deleted successfully.");
    }

    public void editExpense(Scanner scanner) {
        if (expenseCount == 0) {
            System.out.println("No expenses to edit.");
            return;
        }

        System.out.println("===== Expenses =====");
        for (int i = 0; i < expenseCount; i++) {
            System.out.println("Expense " + (i + 1) + ":");
            printExpense(expenses[i]);
        }

        System.out.println("Enter the expense number or exact description to edit:");
        String selection = scanner.nextLine().trim();
        int selectedIndex;

        try {
            selectedIndex = Integer.parseInt(selection) - 1;
            if (selectedIndex < 0 || selectedIndex >= expenseCount) {
                System.out.println("Invalid expense number.");
                return;
            }
        } catch (NumberFormatException exception) {
            int[] matchingIndexes = new int[expenseCount];
            int matchCount = 0;

            for (int i = 0; i < expenseCount; i++) {
                if (expenses[i].getDescription().equalsIgnoreCase(selection)) {
                    matchingIndexes[matchCount] = i;
                    matchCount++;
                }
            }

            if (matchCount == 0) {
                System.out.println("No expense found with that description.");
                return;
            }

            if (matchCount == 1) {
                selectedIndex = matchingIndexes[0];
            } else {
                System.out.println("Multiple expenses have that description:");
                for (int i = 0; i < matchCount; i++) {
                    int index = matchingIndexes[i];
                    System.out.println("Expense " + (index + 1) + ":");
                    printExpense(expenses[index]);
                }

                System.out.println("Enter the expense number to edit:");
                String matchingSelection = scanner.nextLine().trim();
                try {
                    selectedIndex = Integer.parseInt(matchingSelection) - 1;
                } catch (NumberFormatException invalidSelection) {
                    System.out.println("Invalid expense number.");
                    return;
                }

                boolean isMatchingExpense = false;
                for (int i = 0; i < matchCount; i++) {
                    if (matchingIndexes[i] == selectedIndex) {
                        isMatchingExpense = true;
                        break;
                    }
                }
                if (!isMatchingExpense) {
                    System.out.println("Invalid expense number.");
                    return;
                }
            }
        }

        Expense expense = expenses[selectedIndex];
        System.out.println("Current expense details:");
        printExpense(expense);
        System.out.println("Enter new amount, or press Enter to keep " + expense.getAmount() + ":");
        String amountInput = scanner.nextLine().trim();
        if (amountInput.equalsIgnoreCase("cancel")) {
            System.out.println("Expense editing cancelled.");
            return;
        }

        double amount = expense.getAmount();
        if (!amountInput.isEmpty()) {
            try {
                amount = Double.parseDouble(amountInput);
            } catch (NumberFormatException exception) {
                System.out.println("Enter a numeric amount, such as 125.50. Expense unchanged.");
                return;
            }
            if (!Double.isFinite(amount) || amount <= 0) {
                System.out.println("Amount must be a positive, finite number. Expense unchanged.");
                return;
            }
        }

        System.out.println("Enter new category, or press Enter to keep " + expense.getCategory() + ":");
        String categoryInput = scanner.nextLine().trim();
        if (categoryInput.equalsIgnoreCase("cancel")) {
            System.out.println("Expense editing cancelled.");
            return;
        }
        String category = expense.getCategory();
        if (!categoryInput.isEmpty()) {
            category = findCategory(categoryInput);
            if (category == null) {
                System.out.println("Choose Food, Travel, Shopping, Education, or Others. Expense unchanged.");
                return;
            }
        }

        System.out.println("Enter new date (YYYY-MM-DD), or press Enter to keep " + expense.getDate() + ":");
        String dateInput = scanner.nextLine().trim();
        if (dateInput.equalsIgnoreCase("cancel")) {
            System.out.println("Expense editing cancelled.");
            return;
        }
        String date = expense.getDate();
        if (!dateInput.isEmpty()) {
            if (!isValidDate(dateInput)) {
                System.out.println("Enter a valid date in YYYY-MM-DD format. Expense unchanged.");
                return;
            }
            date = dateInput;
        }

        expense.setAmount(amount);
        expense.setCategory(category);
        expense.setDate(date);
        System.out.println("Expense updated successfully:");
        printExpense(expense);
    }

    public void searchByCategory(String category) {
        String matchingCategory = findCategory(category);
        if (matchingCategory == null) {
            System.out.println("Choose Food, Travel, Shopping, Education, or Others.");
            return;
        }
        boolean found = false;

        for (int i = 0; i < expenseCount; i++) {
            Expense expense = expenses[i];
            if (expense.getCategory().equals(matchingCategory)) {
                printExpense(expense);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No expenses found in that category.");
        }
    }

    public void searchByMonth(String month) {
        month = month.trim();
        if (!isValidDate(month + "-01")) {
            System.out.println("Enter a valid month in YYYY-MM format.");
            return;
        }
        boolean found = false;
        String datePrefix = month + "-";

        for (int i = 0; i < expenseCount; i++) {
            Expense expense = expenses[i];
            if (expense.getDate().startsWith(datePrefix)) {
                printExpense(expense);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No expenses found in that month.");
        }
    }

    public void searchByDateRange(Scanner scanner) {
        System.out.println("Enter start date (YYYY-MM-DD):");
        String startDate = scanner.nextLine().trim();
        if (!isValidDate(startDate)) {
            System.out.println("Enter a valid start date in YYYY-MM-DD format.");
            return;
        }

        System.out.println("Enter end date (YYYY-MM-DD):");
        String endDate = scanner.nextLine().trim();
        if (!isValidDate(endDate)) {
            System.out.println("Enter a valid end date in YYYY-MM-DD format.");
            return;
        }

        if (startDate.compareTo(endDate) > 0) {
            System.out.println("Start date cannot be after the end date.");
            return;
        }

        boolean found = false;
        for (int i = 0; i < expenseCount; i++) {
            String expenseDate = expenses[i].getDate();
            if (expenseDate.compareTo(startDate) >= 0
                    && expenseDate.compareTo(endDate) <= 0) {
                printExpense(expenses[i]);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No expenses found in the selected date range.");
        }
    }

    public void searchByAmount(Scanner scanner) {
        System.out.println("Enter expense amount:");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException exception) {
            System.out.println("Enter a numeric amount, such as 125.50.");
            return;
        }

        if (!Double.isFinite(amount) || amount <= 0) {
            System.out.println("Amount must be a positive, finite number.");
            return;
        }

        boolean found = false;
        for (int i = 0; i < expenseCount; i++) {
            if (Double.compare(expenses[i].getAmount(), amount) == 0) {
                printExpense(expenses[i]);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No expenses found with that amount.");
        }
    }

    public void filterExpensesByAmountRange(Scanner scanner) {
        System.out.println("Enter minimum amount:");
        double minimumAmount;
        try {
            minimumAmount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException exception) {
            System.out.println("Enter a numeric minimum amount.");
            return;
        }
        if (!Double.isFinite(minimumAmount) || minimumAmount < 0) {
            System.out.println("Minimum amount must be a non-negative, finite number.");
            return;
        }

        System.out.println("Enter maximum amount:");
        double maximumAmount;
        try {
            maximumAmount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException exception) {
            System.out.println("Enter a numeric maximum amount.");
            return;
        }
        if (!Double.isFinite(maximumAmount) || maximumAmount < 0) {
            System.out.println("Maximum amount must be a non-negative, finite number.");
            return;
        }

        if (minimumAmount > maximumAmount) {
            System.out.println("Minimum amount cannot be greater than maximum amount.");
            return;
        }

        boolean found = false;
        for (int i = 0; i < expenseCount; i++) {
            double expenseAmount = expenses[i].getAmount();
            if (expenseAmount >= minimumAmount && expenseAmount <= maximumAmount) {
                printExpense(expenses[i]);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No expenses found in that amount range.");
        }
    }

    public void searchByDescription(Scanner scanner) {
        System.out.println("Enter a word or phrase to search in expense descriptions:");
        String keyword = scanner.nextLine().trim();
        if (keyword.isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }

        String searchText = keyword.toLowerCase(Locale.ROOT);
        boolean found = false;
        for (int i = 0; i < expenseCount; i++) {
            if (expenses[i].getDescription().toLowerCase(Locale.ROOT).contains(searchText)) {
                printExpense(expenses[i]);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No expenses found with that description keyword.");
        }
    }

    public void displayExpensesSortedByAmount(Scanner scanner) {
        if (expenseCount == 0) {
            System.out.println("No expenses to sort.");
            return;
        }

        System.out.println("Choose sort order:");
        System.out.println("1. Ascending (lowest to highest)");
        System.out.println("2. Descending (highest to lowest)");
        String order = scanner.nextLine().trim();
        if (!order.equals("1") && !order.equals("2")) {
            System.out.println("Invalid choice. Enter 1 for ascending or 2 for descending.");
            return;
        }

        Expense[] sortedExpenses = new Expense[expenseCount];
        System.arraycopy(expenses, 0, sortedExpenses, 0, expenseCount);

        for (int i = 0; i < sortedExpenses.length - 1; i++) {
            for (int j = 0; j < sortedExpenses.length - i - 1; j++) {
                boolean outOfOrder;
                if (order.equals("1")) {
                    outOfOrder = sortedExpenses[j].getAmount() > sortedExpenses[j + 1].getAmount();
                } else {
                    outOfOrder = sortedExpenses[j].getAmount() < sortedExpenses[j + 1].getAmount();
                }

                if (outOfOrder) {
                    Expense temp = sortedExpenses[j];
                    sortedExpenses[j] = sortedExpenses[j + 1];
                    sortedExpenses[j + 1] = temp;
                }
            }
        }

        System.out.println("===== Expenses Sorted by Amount =====");
        for (Expense expense : sortedExpenses) {
            printExpense(expense);
        }
    }

    public void displayExpensesSortedByDate(Scanner scanner) {
        if (expenseCount == 0) {
            System.out.println("No expenses to sort.");
            return;
        }

        System.out.println("Choose sort order:");
        System.out.println("1. Oldest to newest");
        System.out.println("2. Newest to oldest");
        String order = scanner.nextLine().trim();
        if (!order.equals("1") && !order.equals("2")) {
            System.out.println("Invalid choice. Enter 1 for oldest to newest or 2 for newest to oldest.");
            return;
        }

        Expense[] sortedExpenses = new Expense[expenseCount];
        System.arraycopy(expenses, 0, sortedExpenses, 0, expenseCount);

        for (int i = 0; i < sortedExpenses.length - 1; i++) {
            for (int j = 0; j < sortedExpenses.length - i - 1; j++) {
                int comparison = sortedExpenses[j].getDate().compareTo(
                    sortedExpenses[j + 1].getDate()
                );
                boolean outOfOrder = order.equals("1")
                    ? comparison > 0
                    : comparison < 0;

                if (outOfOrder) {
                    Expense temp = sortedExpenses[j];
                    sortedExpenses[j] = sortedExpenses[j + 1];
                    sortedExpenses[j + 1] = temp;
                }
            }
        }

        System.out.println("===== Expenses Sorted by Date =====");
        for (Expense expense : sortedExpenses) {
            printExpense(expense);
        }
    }

    private String findCategory(String input) {
        String trimmedInput = input.trim();
        for (String category : CATEGORIES) {
            if (category.equalsIgnoreCase(trimmedInput)) {
                return category;
            }
        }
        return null;
    }

    private boolean isValidDate(String date) {
        if (!date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}") || date.startsWith("0000")) {
            return false;
        }
        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeParseException exception) {
            return false;
        }
    }

    private void printExpense(Expense expense) {
        System.out.println("Amount: " + expense.getAmount());
        System.out.println("Category: " + expense.getCategory());
        System.out.println("Date: " + expense.getDate());
        System.out.println("Description: " + expense.getDescription());
        System.out.println();
    }
    public double calculateMonthlyExpenses(String month) {
    double total = 0;

    for (int i = 0; i < expenseCount; i++) {
        Expense expense = expenses[i];

        if (expense.getDate().startsWith(month + "-")) {
            total = total + expense.getAmount();
        }
    }

    return total;
}

public int calculateMonthlyExpenseCount(String month) {
    int count = 0;

    for (int i = 0; i < expenseCount; i++) {
        if (expenses[i].getDate().startsWith(month + "-")) {
            count++;
        }
    }

    return count;
}

public void displayAverageMonthlyExpense(String month) {
    double total = calculateMonthlyExpenses(month);
    int count = calculateMonthlyExpenseCount(month);

    System.out.println("===== Average Monthly Expense =====");
    System.out.println("Month: " + month);
    System.out.println("Total Expenses: Rs. " + total);
    System.out.println("Number of Transactions: " + count);

    if (count == 0) {
        System.out.println("No expenses found in that month; average cannot be calculated.");
        return;
    }

    System.out.println("Average per Transaction: Rs. " + (total / count));
}

public void displayMonthlyExpenseComparison(String firstMonth, String secondMonth) {
    double firstTotal = calculateMonthlyExpenses(firstMonth);
    double secondTotal = calculateMonthlyExpenses(secondMonth);

    System.out.println("===== Monthly Expense Comparison =====");
    System.out.println(firstMonth + " Total Expenses: Rs. " + firstTotal);
    if (firstTotal == 0) {
        System.out.println("No expenses recorded for " + firstMonth + ".");
    }
    System.out.println(secondMonth + " Total Expenses: Rs. " + secondTotal);
    if (secondTotal == 0) {
        System.out.println("No expenses recorded for " + secondMonth + ".");
    }

    double difference = Math.abs(secondTotal - firstTotal);
    System.out.println("Spending Difference: Rs. " + difference);
    if (firstTotal > secondTotal) {
        System.out.println(firstMonth + " had higher expenses.");
    } else if (secondTotal > firstTotal) {
        System.out.println(secondMonth + " had higher expenses.");
    } else {
        System.out.println("Both months had the same total expenses.");
    }

    if (firstTotal == 0) {
        System.out.println("Percentage change: Not available because the first month has zero spending.");
    } else {
        double percentageChange = ((secondTotal - firstTotal) / firstTotal) * 100;
        if (percentageChange > 0) {
            System.out.println("Increase: " + percentageChange + "%");
        } else if (percentageChange < 0) {
            System.out.println("Decrease: " + Math.abs(percentageChange) + "%");
        } else {
            System.out.println("Percentage change: 0.0%");
        }
    }
}

public void displayMonthlyExpenseSummary(String month) {
    double total = 0;
    int count = 0;
    Expense highestExpense = null;
    Expense lowestExpense = null;

    for (int i = 0; i < expenseCount; i++) {
        Expense expense = expenses[i];

        if (expense.getDate().startsWith(month + "-")) {
            total = total + expense.getAmount();
            count++;

            if (highestExpense == null || expense.getAmount() > highestExpense.getAmount()) {
                highestExpense = expense;
            }

            if (lowestExpense == null || expense.getAmount() < lowestExpense.getAmount()) {
                lowestExpense = expense;
            }
        }
    }

    System.out.println("===== Monthly Expense Summary =====");
    System.out.println("Month: " + month);
    System.out.println("Total Expenses: Rs. " + total);
    System.out.println("Number of Expenses: " + count);

    if (count == 0) {
        System.out.println("No expenses found in that month.");
        return;
    }

    System.out.println("Highest Expense: Rs. " + highestExpense.getAmount());
    System.out.println("Highest Expense Category: " + highestExpense.getCategory());
    System.out.println("Lowest Expense: Rs. " + lowestExpense.getAmount());
    System.out.println("Lowest Expense Category: " + lowestExpense.getCategory());
}

public void displayCategoryExpenseSummary(Scanner scanner) {
    if (expenseCount == 0) {
        System.out.println("No expenses recorded.");
        return;
    }

    System.out.println("Enter month (YYYY-MM), or press Enter for all-time summary:");
    String month = scanner.nextLine().trim();
    if (!month.isEmpty() && !isValidDate(month + "-01")) {
        System.out.println("Invalid month. Use YYYY-MM.");
        return;
    }

    double overallTotal = 0;
    double[] categoryTotals = new double[CATEGORIES.length];
    int[] categoryCounts = calculateCategoryExpenseCounts(month);

    for (int i = 0; i < expenseCount; i++) {
        Expense expense = expenses[i];
        if (!month.isEmpty() && !expense.getDate().startsWith(month + "-")) {
            continue;
        }

        for (int j = 0; j < CATEGORIES.length; j++) {
            if (expense.getCategory().equalsIgnoreCase(CATEGORIES[j])) {
                categoryTotals[j] = categoryTotals[j] + expense.getAmount();
                overallTotal = overallTotal + expense.getAmount();
                break;
            }
        }
    }

    int overallCount = 0;
    for (int categoryCount : categoryCounts) {
        overallCount = overallCount + categoryCount;
    }

    if (overallCount == 0) {
        if (month.isEmpty()) {
            System.out.println("No expenses recorded.");
        } else {
            System.out.println("No expenses found for " + month + ".");
        }
        return;
    }

    System.out.println("===== Category-wise Expense Summary =====");
    System.out.println(month.isEmpty() ? "Period: All time" : "Month: " + month);
    for (int i = 0; i < CATEGORIES.length; i++) {
        System.out.println("Category: " + CATEGORIES[i]);
        System.out.println("Total: Rs. " + categoryTotals[i]);
        System.out.println("Number of Expenses: " + categoryCounts[i]);
        System.out.println("--------------------");
    }
    System.out.println("Overall Total: Rs. " + overallTotal);
    System.out.println("Overall Number of Expenses: " + overallCount);
}

public void displayExpenseCountByCategory() {
    if (expenseCount == 0) {
        System.out.println("No expenses recorded.");
        return;
    }

    int[] categoryCounts = calculateCategoryExpenseCounts("");
    System.out.println("===== Expense Count by Category =====");
    for (int i = 0; i < CATEGORIES.length; i++) {
        System.out.println(CATEGORIES[i] + ": " + categoryCounts[i]);
    }
    System.out.println("Total Recorded Expenses: " + expenseCount);
}

public void displayOverallExpenseStatistics() {
    if (expenseCount == 0) {
        System.out.println("No expenses recorded. Overall statistics are unavailable.");
        return;
    }

    double total = 0;
    Expense highestExpense = expenses[0];
    Expense lowestExpense = expenses[0];

    for (int i = 0; i < expenseCount; i++) {
        Expense expense = expenses[i];
        total = total + expense.getAmount();

        if (expense.getAmount() > highestExpense.getAmount()) {
            highestExpense = expense;
        }
        if (expense.getAmount() < lowestExpense.getAmount()) {
            lowestExpense = expense;
        }
    }

    System.out.println("===== Overall Expense Statistics =====");
    System.out.println("Total Number of Expenses: " + expenseCount);
    System.out.println("Total Amount Spent: Rs. " + total);
    System.out.println("Highest Expense: Rs. " + highestExpense.getAmount());
    System.out.println("Highest Expense Category: " + highestExpense.getCategory());
    System.out.println("Lowest Expense: Rs. " + lowestExpense.getAmount());
    System.out.println("Lowest Expense Category: " + lowestExpense.getCategory());
    System.out.println("Average Amount per Expense: Rs. " + (total / expenseCount));
}

public void exportExpensesToCsv() {
    if (expenseCount == 0) {
        System.out.println("No expenses to export.");
        return;
    }

    String fileName = "expenses.csv";
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
        writer.write("amount,category,date,description");
        writer.newLine();

        for (int i = 0; i < expenseCount; i++) {
            Expense expense = expenses[i];
            writer.write(expense.getAmount() + ","
                    + toCsvField(expense.getCategory()) + ","
                    + toCsvField(expense.getDate()) + ","
                    + toCsvField(expense.getDescription()));
            writer.newLine();
        }

    } catch (IOException exception) {
        System.out.println("Unable to export expenses to " + fileName + ": "
                + exception.getMessage());
        return;
    }
    System.out.println("Expenses exported successfully to " + fileName + ".");
}

private String toCsvField(String value) {
    return "\"" + value.replace("\"", "\"\"") + "\"";
}

public void importExpensesFromCsv() {
    int importedCount = 0;
    int skippedCount = 0;
    String fileName = "expenses.csv";

    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
        String header = reader.readLine();
        if (header == null) {
            System.out.println("The CSV file is empty; no expenses were imported.");
            return;
        }

        String line;
        while ((line = reader.readLine()) != null) {
            String[] fields = parseCsvRow(line);
            if (fields == null || fields.length != 4) {
                skippedCount++;
                continue;
            }

            double amount;
            try {
                amount = Double.parseDouble(fields[0].trim());
            } catch (NumberFormatException exception) {
                skippedCount++;
                continue;
            }

            String category = findCategory(fields[1]);
            String date = fields[2].trim();
            String description = fields[3].trim();
            if (!Double.isFinite(amount) || amount <= 0
                    || category == null || !isValidDate(date) || description.isEmpty()) {
                skippedCount++;
                continue;
            }

            if (expenseCount == expenses.length) {
                skippedCount++;
                continue;
            }

            expenses[expenseCount] = new Expense(amount, category, date, description);
            expenseCount++;
            importedCount++;
        }
    } catch (FileNotFoundException exception) {
        System.out.println("CSV file not found: " + fileName);
        return;
    } catch (IOException exception) {
        System.out.println("Unable to read " + fileName + ": " + exception.getMessage());
        System.out.println("Expenses imported before the error: " + importedCount);
        System.out.println("Rows skipped: " + skippedCount);
        return;
    }

    System.out.println("Expenses successfully imported: " + importedCount);
    System.out.println("Rows skipped: " + skippedCount);
}

private String[] parseCsvRow(String line) {
    ArrayList<String> fields = new ArrayList<>();
    StringBuilder field = new StringBuilder();
    boolean inQuotes = false;
    boolean quoteClosed = false;

    for (int i = 0; i < line.length(); i++) {
        char character = line.charAt(i);
        if (inQuotes) {
            if (character == '"') {
                if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    field.append('"');
                    i++;
                } else {
                    inQuotes = false;
                    quoteClosed = true;
                }
            } else {
                field.append(character);
            }
        } else if (character == ',') {
            fields.add(field.toString());
            field.setLength(0);
            quoteClosed = false;
        } else if (character == '"' && field.length() == 0 && !quoteClosed) {
            inQuotes = true;
        } else if (quoteClosed) {
            return null;
        } else {
            field.append(character);
        }
    }

    if (inQuotes) {
        return null;
    }
    fields.add(field.toString());
    return fields.toArray(new String[0]);
}

private int[] calculateCategoryExpenseCounts(String month) {
    int[] categoryCounts = new int[CATEGORIES.length];

    for (int i = 0; i < expenseCount; i++) {
        Expense expense = expenses[i];
        if (!month.isEmpty() && !expense.getDate().startsWith(month + "-")) {
            continue;
        }

        for (int j = 0; j < CATEGORIES.length; j++) {
            if (expense.getCategory().equalsIgnoreCase(CATEGORIES[j])) {
                categoryCounts[j]++;
                break;
            }
        }
    }

    return categoryCounts;
}

public double calculateCategoryExpenses(String month, String category) {
    double total = 0;

    for (int i = 0; i < expenseCount; i++) {
        Expense expense = expenses[i];

        if (expense.getDate().startsWith(month + "-")
                && expense.getCategory().equalsIgnoreCase(category)) {

            total = total + expense.getAmount();
        }
    }

    return total;
}
}
