# SmartSpend – Java Expense Tracker

SmartSpend is a Java console app for keeping track of everyday spending. Record expenses as you go, then look them up by category or month.

## What you can do

- Add an expense with an amount, category, date, and description.
- View all recorded expenses.
- Find expenses in a category such as Food or Travel.
- Find expenses from a specific month.

## How to run

Install a Java Development Kit (JDK) and open a terminal in the folder containing `Main.java`.

```sh
javac Main.java Expense.java ExpenseManager.java
java Main
```

Choose **1. Add expense** and try entering `250`, `Food`, `2026-11-21`, and `Lunch`. Choose **5. Search by month** and enter `2026-11` to find that expense again.

## Project structure

| File | What it does |
| --- | --- |
| `Main.java` | Starts the app, displays the menu, and handles your choices. |
| `Expense.java` | Stores the details of one expense. |
| `ExpenseManager.java` | Adds, displays, and searches expenses. |

## Input and storage

Enter a positive amount and choose Food, Travel, Shopping, Education, or Others. Use `YYYY-MM-DD` for expense dates and `YYYY-MM` for month searches.

The app stores up to 100 expenses in memory. Closing the app clears them.
Use **Import / Export CSV** from the main menu to save a copy of your expenses.
