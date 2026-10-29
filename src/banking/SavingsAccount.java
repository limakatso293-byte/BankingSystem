
package banking;

// This class represents a savings account.
// It inherits from BankAccount and implements Loanable.
public class SavingsAccount extends BankAccount implements Loanable {

    // Sets the minimum balance that must remain in the account.
    private static final double MINIMUM_BALANCE = 100.00;

    // Sets the annual interest rate to 5%.
    private static final double INTEREST_RATE = 0.05;

    // Sets the maximum loan amount for a savings account.
    private static final double LOAN_LIMIT = 5000.00;

    // Creates a savings account using the details provided.
    public SavingsAccount(String accountNumber, String accountHolder,
                          double openingBalance, String pin) {

        // Calls the constructor of the parent class.
        super(accountNumber, accountHolder, openingBalance, pin);
    }

    // Adds money to the savings account.
    @Override
    public void deposit(double amount) {

        // Rejects deposits that are zero or negative.
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Deposit amount must be greater than zero.");
        }

        // Adds the deposit amount to the current balance.
        setBalance(getBalance() + amount);
    }

    // Withdraws money while keeping at least M100 in the account.
    @Override
    public void withdraw(double amount) {

        // Rejects withdrawals that are zero or negative.
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Withdrawal amount must be greater than zero.");
        }

        // Checks that the minimum balance rule is maintained.
        if (getBalance() - amount < MINIMUM_BALANCE) {
            throw new IllegalArgumentException(
                    "Savings account must keep at least M100.");
        }

        // Subtracts the withdrawal amount from the balance.
        setBalance(getBalance() - amount);
    }

    // Calculates 5% of the current balance as interest.
    @Override
    public double calculateInterest() {
        return getBalance() * INTEREST_RATE;
    }

    // Displays the savings account information.
    @Override
    public void displayDetails() {
        System.out.println("\n--- SAVINGS ACCOUNT ---");
        System.out.println("Account Number: " + getAccountNumber());
        System.out.println("Account Holder: " + getAccountHolder());
        System.out.printf("Balance: M%.2f%n", getBalance());
        System.out.printf("Annual Interest: M%.2f%n",
                calculateInterest());
    }

    // Approves a loan if it is within the allowed limit.
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
