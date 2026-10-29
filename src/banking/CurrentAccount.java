
package banking;

// A current account can use an overdraft up to M2,000.
public class CurrentAccount extends BankAccount implements Loanable {

    // Sets the overdraft and loan limits.
    private static final double OVERDRAFT_LIMIT = 2000.00;
    private static final double LOAN_LIMIT = 10000.00;

    // Creates a current account using the details provided.
    public CurrentAccount(String accountNumber, String accountHolder,
                          double openingBalance, String pin) {

        // Calls the constructor of the parent class.
        super(accountNumber, accountHolder, openingBalance, pin);
    }

    // This adds money to the current account.
    @Override
    public void deposit(double amount) {

        // Rejects zero or negative deposits.
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Deposit amount must be greater than zero.");
        }

        // Adds money to the balance.
        setBalance(getBalance() + amount);
    }

    // The balance can go negative, but not below -M2,000.
    @Override
    public void withdraw(double amount) {

        // Rejects zero or negative withdrawals.
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Withdrawal amount must be greater than zero.");
        }

        // Checks that the overdraft limit is not exceeded.
        if (getBalance() - amount < -OVERDRAFT_LIMIT) {
            throw new IllegalArgumentException(
                    "Overdraft limit of M2,000 exceeded.");
        }

        // Subtracts the withdrawal amount.
        setBalance(getBalance() - amount);
    }

    // Current accounts do not earn interest in this system.
    @Override
    public double calculateInterest() {
        return 0.00;
    }

    // This displays the current account information.
    @Override
    public void displayDetails() {
        System.out.println("\n--- CURRENT ACCOUNT ---");
        System.out.println("Account Number: " + getAccountNumber());
        System.out.println("Account Holder: " + getAccountHolder());
        System.out.printf("Balance: M%.2f%n", getBalance());
        System.out.println("Interest: M0.00");
        System.out.printf("Overdraft Limit: M%.2f%n", OVERDRAFT_LIMIT);
    }

    // This checks whether the requested loan is allowed.
    @Override
    public boolean requestLoan(double amount) {

        // Rejects zero, negative, or excessive loan amounts.
        if (amount <= 0 || amount > LOAN_LIMIT) {
            return false;
        }

        // Adds the approved loan to the account balance.
        setBalance(getBalance() + amount);

        return true;
    }
}
