public class Budget {
    private String month;
    private String category;
    private double amount;

    public Budget(String month, String category, double amount) {
        this.month = month;
        this.category = category;
        this.amount = amount;
    }
    public String getMonth() {
    return month;
}

public void setMonth(String month) {
    this.month = month;
}

public String getCategory() {
    return category;
}

public void setCategory(String category) {
    this.category = category;
}

public double getAmount() {
    return amount;
}

public void setAmount(double amount) {
    this.amount = amount;
}
}