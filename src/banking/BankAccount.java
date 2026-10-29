
package banking;

// This abstract class is the parent of all bank account types.
public abstract class BankAccount {

    // Private variables demonstrate encapsulation.
    private String accountNumber;
    private String accountHolder;
    private double balance;
    private String pin;

    // Creates a bank account with its details and PIN.
    public BankAccount(String accountNumber, String accountHolder,
                       double openingBalance, String pin) {

        if (openingBalance < 0) {
            throw new IllegalArgumentException(
                    "Opening balance cannot be negative.");
        }

        if (pin == null || !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException(
                    "PIN must contain exactly four digits.");
        }

        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = openingBalance;
        this.pin = pin;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    // Checks whether the entered PIN matches the account PIN.
    public boolean verifyPin(String enteredPin) {
        return pin.equals(enteredPin);
    }

    // Allows child classes to update the balance.
    protected void setBalance(double balance) {
        this.balance = balance;
    }

    // Each account type implements these methods.
    public abstract void deposit(double amount);

    public abstract void withdraw(double amount);

    public abstract double calculateInterest();

    public abstract void displayDetails();
}
