import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("==========================");
        System.out.println("Welcome to SmartSpend");
        System.out.println("Expense Tracker and Budget Planner");

        Scanner scanner = new Scanner(System.in);

        try {
            runMenu(scanner);
        } catch (NoSuchElementException exception) {
            System.out.println("Input ended. Goodbye!");
        } finally {
            scanner.close();
        }
    }

    private static void runMenu(Scanner scanner) {
        ExpenseManager expenseManager = new ExpenseManager();
        BudgetManager budgetManager = new BudgetManager();
        String choice;

        do {
            System.out.println();
            System.out.println("===== SmartSpend Main Menu =====");
            System.out.println("1. Add Expense");
            System.out.println("2. Display All Expenses");
            System.out.println("3. Search Expenses");
            System.out.println("4. Edit Expense");
            System.out.println("5. Delete Expense");
            System.out.println("6. Budget Management");
            System.out.println("7. Monthly Expense Summary");
            System.out.println("8. Expense Reports");
            System.out.println("9. Import / Export CSV");
            System.out.println("10. Exit");
            System.out.println("Enter your choice:");

            choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    expenseManager.addExpense(scanner);
                    break;
                case "2":
                    expenseManager.displayAllExpenses();
                    break;
                case "3":
                    runSearchMenu(scanner, expenseManager);
                    break;
                case "4":
                    expenseManager.editExpense(scanner);
                    break;
                case "5":
                    expenseManager.deleteExpense(scanner);
                    break;
                case "6":
                    runBudgetMenu(scanner, budgetManager, expenseManager);
                    break;
                case "7":
                    runMonthlySummaryMenu(scanner, expenseManager);
                    break;
                case "8":
                    runExpenseReportsMenu(scanner, expenseManager);
                    break;
                case "9":
                    runCsvMenu(scanner, expenseManager);
                    break;
                case "10":
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 10.");
            }
        } while (!choice.equals("10"));
    }

    private static void runSearchMenu(Scanner scanner, ExpenseManager expenseManager) {
        String choice;
        do {
            System.out.println();
            System.out.println("===== Search Expenses =====");
            System.out.println("1. Search by category");
            System.out.println("2. Search by month");
            System.out.println("3. Search by amount");
            System.out.println("4. Search by description");
            System.out.println("5. Search by date range");
            System.out.println("6. Filter by amount range");
            System.out.println("7. Back to Main Menu");
            System.out.println("Enter your choice:");

            choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    System.out.println("Enter category to search:");
                    expenseManager.searchByCategory(scanner.nextLine());
                    break;
                case "2":
                    System.out.println("Enter month to search (YYYY-MM):");
                    expenseManager.searchByMonth(scanner.nextLine());
                    break;
                case "3":
                    expenseManager.searchByAmount(scanner);
                    break;
                case "4":
                    expenseManager.searchByDescription(scanner);
                    break;
                case "5":
                    expenseManager.searchByDateRange(scanner);
                    break;
                case "6":
                    expenseManager.filterExpensesByAmountRange(scanner);
                    break;
                case "7":
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 7.");
            }
        } while (!choice.equals("7"));
    }

    private static void runBudgetMenu(
            Scanner scanner, BudgetManager budgetManager, ExpenseManager expenseManager) {
        String choice;
        do {
            System.out.println();
            System.out.println("===== Budget Management =====");
            System.out.println("1. Set Monthly Budget");
            System.out.println("2. Set Category Budget");
            System.out.println("3. Display Budgets");
            System.out.println("4. Calculate Remaining Budget");
            System.out.println("5. Check Budget Limits");
            System.out.println("6. Back to Main Menu");
            System.out.println("Enter your choice:");

            choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    budgetManager.setMonthlyBudget(scanner);
                    break;
                case "2":
                    budgetManager.setCategoryBudget(scanner);
                    break;
                case "3":
                    budgetManager.displayBudgets();
                    break;
                case "4": {
                    String month = promptForMonth(scanner, "Enter month (YYYY-MM):");
                    if (month != null) {
                        budgetManager.calculateRemainingBudget(month, expenseManager);
                    }
                    break;
                }
                case "5": {
                    String month = promptForMonth(scanner, "Enter month (YYYY-MM):");
                    if (month != null) {
                        budgetManager.checkBudgetLimit(month, expenseManager);
                    }
                    break;
                }
                case "6":
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 6.");
            }
        } while (!choice.equals("6"));
    }

    private static void runMonthlySummaryMenu(Scanner scanner, ExpenseManager expenseManager) {
        String choice;
        do {
            System.out.println();
            System.out.println("===== Monthly Expense Summary =====");
            System.out.println("1. View Monthly Expense Total");
            System.out.println("2. View Detailed Monthly Summary");
            System.out.println("3. Back to Main Menu");
            System.out.println("Enter your choice:");

            choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": {
                    String month = promptForMonth(scanner, "Enter month (YYYY-MM):");
                    if (month != null) {
                        double total = expenseManager.calculateMonthlyExpenses(month);
                        System.out.println("Total Expenses: Rs. " + total);
                    }
                    break;
                }
                case "2": {
                    String month = promptForMonth(scanner, "Enter month (YYYY-MM):");
                    if (month != null) {
                        expenseManager.displayMonthlyExpenseSummary(month);
                    }
                    break;
                }
                case "3":
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 3.");
            }
        } while (!choice.equals("3"));
    }

    private static void runExpenseReportsMenu(Scanner scanner, ExpenseManager expenseManager) {
        String choice;
        do {
            System.out.println();
            System.out.println("===== Expense Reports =====");
            System.out.println("1. Sort Expenses by Amount");
            System.out.println("2. Sort Expenses by Date");
            System.out.println("3. Category-wise Expense Summary");
            System.out.println("4. Compare Expenses Between Months");
            System.out.println("5. Average Monthly Expense");
            System.out.println("6. Expense Count by Category");
            System.out.println("7. Overall Expense Statistics");
            System.out.println("8. Back to Main Menu");
            System.out.println("Enter your choice:");

            choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    expenseManager.displayExpensesSortedByAmount(scanner);
                    break;
                case "2":
                    expenseManager.displayExpensesSortedByDate(scanner);
                    break;
                case "3":
                    expenseManager.displayCategoryExpenseSummary(scanner);
                    break;
                case "4": {
                    String firstMonth = promptForMonth(scanner, "Enter first month (YYYY-MM):");
                    if (firstMonth == null) {
                        break;
                    }
                    String secondMonth = promptForMonth(scanner, "Enter second month (YYYY-MM):");
                    if (secondMonth == null) {
                        break;
                    }
                    if (firstMonth.equals(secondMonth)) {
                        System.out.println("Choose two different months to compare.");
                        break;
                    }
                    expenseManager.displayMonthlyExpenseComparison(firstMonth, secondMonth);
                    break;
                }
                case "5": {
                    String month = promptForMonth(scanner, "Enter month (YYYY-MM):");
                    if (month != null) {
                        expenseManager.displayAverageMonthlyExpense(month);
                    }
                    break;
                }
                case "6":
                    expenseManager.displayExpenseCountByCategory();
                    break;
                case "7":
                    expenseManager.displayOverallExpenseStatistics();
                    break;
                case "8":
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 8.");
            }
        } while (!choice.equals("8"));
    }

    private static void runCsvMenu(Scanner scanner, ExpenseManager expenseManager) {
        String choice;
        do {
            System.out.println();
            System.out.println("===== Import / Export CSV =====");
            System.out.println("1. Import expenses from CSV");
            System.out.println("2. Export expenses to CSV");
            System.out.println("3. Back to Main Menu");
            System.out.println("Enter your choice:");

            choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    expenseManager.importExpensesFromCsv();
                    break;
                case "2":
                    expenseManager.exportExpensesToCsv();
                    break;
                case "3":
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 3.");
            }
        } while (!choice.equals("3"));
    }

    private static String promptForMonth(Scanner scanner, String prompt) {
        System.out.println(prompt);
        String month = scanner.nextLine().trim();
        if (!isValidMonth(month)) {
            System.out.println("Invalid month. Use YYYY-MM.");
            return null;
        }
        return month;
    }

    private static boolean isValidMonth(String month) {
        return month.matches("[0-9]{4}-(0[1-9]|1[0-2])")
                && !month.startsWith("0000");
    }
}
