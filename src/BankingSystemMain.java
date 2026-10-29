
import banking.BankAccount;
import banking.SavingsAccount;
import banking.CurrentAccount;
import banking.Loanable;
import banking.TransactionLogger;

import java.util.ArrayList;
import java.util.Scanner;

// This class runs the banking system and displays the menu.
public class BankingSystemMain {

    // Stores all accounts while the program is running.
    private static final ArrayList<BankAccount> accounts =
            new ArrayList<>();

    // Reads information entered by the user.
    private static final Scanner scanner = new Scanner(System.in);

    // Starts the banking program.
    public static void main(String[] args) {

        // Creates two example accounts with four-digit PINs.
        accounts.add(new SavingsAccount(
                "900010", "Limakatso Mabula", 1000.00, "1234"));

        accounts.add(new CurrentAccount(
                "900011", "Moleboheng Setsoto", 2000.00, "5678"));

        int choice;

        // Repeats the menu until the user chooses Exit.
        do {
            displayMenu();
            choice = readInt("Choose an option: ");

            try {
                switch (choice) {
                    case 1:
                        displayAllAccounts();
                        break;

                    case 2:
                        depositMoney();
                        break;

                    case 3:
                        withdrawMoney();
                        break;

                    case 4:
                        transferMoney();
                        break;

                    case 5:
                        calculateInterest();
                        break;

                    case 6:
                        requestLoan();
                        break;

                    case 7:
                        displayAccountDetails();
                        break;

                    case 8:
                        createAccount();
                        break;

                    case 9:
                        System.out.println("Thank you for using the banking system.");
                        break;

                    default:
                        System.out.println("Invalid menu option. Try again.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (choice != 9);

        scanner.close();
    }

    // Displays the banking menu.
    private static void displayMenu() {
        System.out.println("\n===== BANKING SYSTEM =====");
        System.out.println("1. Display all accounts");
        System.out.println("2. Deposit money");
        System.out.println("3. Withdraw money");
        System.out.println("4. Transfer money");
        System.out.println("5. Calculate interest");
        System.out.println("6. Request a loan");
        System.out.println("7. Display account details");
        System.out.println("8. Create a new account");
        System.out.println("9. Exit");
    }

    // Reads a whole number and handles invalid input.
    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    // Reads a monetary amount and handles invalid input.
    private static double readAmount(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            try {
                double amount = Double.parseDouble(input);

                if (!Double.isFinite(amount)) {
                    System.out.println("Enter a valid amount.");
                    continue;
                }

                return amount;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Finds an account using its account number.
    private static BankAccount findAccount(String accountNumber) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }

        return null;
    }

    // Asks for an account number and verifies the PIN.
    private static BankAccount selectVerifiedAccount() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine().trim();

        BankAccount account = findAccount(accountNumber);

        if (account == null) {
            System.out.println("Account not found.");
            return null;
        }

        // Repeats PIN entry until the correct PIN is entered.
        while (true) {
            System.out.print("Enter PIN: ");
            String enteredPin = scanner.nextLine().trim();

            if (account.verifyPin(enteredPin)) {
                System.out.println("PIN verified successfully.");
                return account;
            }

            // Displays the requested message for an incorrect PIN.
            System.out.println("Incorrect PIN. Try again.");
        }
    }

    // Displays all accounts without showing their PINs.
    private static void displayAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts are available.");
            return;
        }

        for (BankAccount account : accounts) {
            account.displayDetails();
        }
    }

    // Deposits money after the PIN has been verified.
    private static void depositMoney() {
        BankAccount account = selectVerifiedAccount();

        if (account == null) {
            return;
        }

        double amount = readAmount("Enter deposit amount: ");

        try {
            account.deposit(amount);

            System.out.println("Deposit successful.");
            System.out.printf("New balance: M%.2f%n",
                    account.getBalance());

            TransactionLogger.log(
                    account.getAccountNumber(),
                    "DEPOSIT",
                    amount,
                    account.getBalance(),
                    "SUCCESS");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());

            TransactionLogger.log(
                    account.getAccountNumber(),
                    "DEPOSIT",
                    amount,
                    account.getBalance(),
                    "FAILED: " + e.getMessage());
        }
    }

    // Withdraws money after the PIN has been verified.
    private static void withdrawMoney() {
        BankAccount account = selectVerifiedAccount();

        if (account == null) {
            return;
        }

        double amount = readAmount("Enter withdrawal amount: ");

        try {
            account.withdraw(amount);

            System.out.println("Withdrawal successful.");
            System.out.printf("Remaining balance: M%.2f%n",
                    account.getBalance());

            TransactionLogger.log(
                    account.getAccountNumber(),
                    "WITHDRAWAL",
                    amount,
                    account.getBalance(),
                    "SUCCESS");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());

            TransactionLogger.log(
                    account.getAccountNumber(),
                    "WITHDRAWAL",
                    amount,
                    account.getBalance(),
                    "FAILED: " + e.getMessage());
        }
    }

    // Transfers money between two accounts after verifying both PINs.
    private static void transferMoney() {
        System.out.println("Enter details for the sending account.");
        BankAccount sender = selectVerifiedAccount();

        if (sender == null) {
            return;
        }

        System.out.println("Enter details for the receiving account.");
        BankAccount receiver = selectVerifiedAccount();

        if (receiver == null) {
            return;
        }

        if (sender == receiver) {
            System.out.println("You cannot transfer money to the same account.");
            return;
        }

        double amount = readAmount("Enter transfer amount: ");

        try {
            // Checks and updates the sender's balance first.
            sender.withdraw(amount);

            // Adds the money to the receiving account.
            try {
                receiver.deposit(amount);
            } catch (IllegalArgumentException e) {
                // Restores the sender's money if the deposit fails.
                sender.deposit(amount);
                throw e;
            }

            System.out.println("Transfer successful.");

            TransactionLogger.log(
                    sender.getAccountNumber(),
                    "TRANSFER OUT",
                    amount,
                    sender.getBalance(),
                    "SUCCESS");

            TransactionLogger.log(
                    receiver.getAccountNumber(),
                    "TRANSFER IN",
                    amount,
                    receiver.getBalance(),
                    "SUCCESS");

        } catch (IllegalArgumentException e) {
            System.out.println("Transfer failed: " + e.getMessage());

            TransactionLogger.log(
                    sender.getAccountNumber(),
                    "TRANSFER",
                    amount,
                    sender.getBalance(),
                    "FAILED: " + e.getMessage());
        }
    }

    // Calculates interest after verifying the account PIN.
    private static void calculateInterest() {
        BankAccount account = selectVerifiedAccount();

        if (account == null) {
            return;
        }

        double interest = account.calculateInterest();

        System.out.printf("Calculated interest: M%.2f%n", interest);

        TransactionLogger.log(
                account.getAccountNumber(),
                "INTEREST CALCULATION",
                interest,
                account.getBalance(),
                "SUCCESS");
    }

    // Processes a loan request after verifying the PIN.
    private static void requestLoan() {
        BankAccount account = selectVerifiedAccount();

        if (account == null) {
            return;
        }

        double amount = readAmount("Enter loan amount: ");

        if (account instanceof Loanable) {
            Loanable loanableAccount = (Loanable) account;

            if (loanableAccount.requestLoan(amount)) {
                System.out.println("Loan request approved.");

                TransactionLogger.log(
                        account.getAccountNumber(),
                        "LOAN REQUEST",
                        amount,
                        account.getBalance(),
                        "APPROVED");
            } else {
                System.out.println(
                        "Loan request rejected. Check the amount and loan limit.");

                TransactionLogger.log(
                        account.getAccountNumber(),
                        "LOAN REQUEST",
                        amount,
                        account.getBalance(),
                        "REJECTED");
            }
        }
    }

    // Displays the selected account's details after PIN verification.
    private static void displayAccountDetails() {
        BankAccount account = selectVerifiedAccount();

        if (account != null) {
            account.displayDetails();
        }
    }

    // Creates a new savings or current account.
    private static void createAccount() {
        System.out.println("\n--- CREATE NEW ACCOUNT ---");
        System.out.println("1. Savings account");
        System.out.println("2. Current account");

        int type = readInt("Choose account type: ");

        if (type != 1 && type != 2) {
            System.out.println("Invalid account type.");
            return;
        }

        System.out.print("Enter a new account number: ");
        String number = scanner.nextLine().trim();

        if (number.isEmpty() || findAccount(number) != null) {
            System.out.println(
                    "Account number is empty or already exists.");
            return;
        }

        System.out.print("Enter account holder's name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Account holder's name cannot be empty.");
            return;
        }

        double openingBalance =
                readAmount("Enter opening balance: ");

        if (openingBalance < 0) {
            System.out.println(
                    "Opening balance cannot be negative.");
            return;
        }

        // Requires the user to create a four-digit PIN.
        String pin;

        while (true) {
            System.out.print("Create a four-digit PIN: ");
            pin = scanner.nextLine().trim();

            if (pin.matches("\\d{4}")) {
                break;
            }

            System.out.println(
                    "Invalid PIN. Enter exactly four digits.");
        }

        try {
            // Creates the account selected by the user.
            BankAccount newAccount;

            if (type == 1) {
                newAccount = new SavingsAccount(
                        number, name, openingBalance, pin);
            } else {
                newAccount = new CurrentAccount(
                        number, name, openingBalance, pin);
            }

            accounts.add(newAccount);

            System.out.println("Account created successfully.");

            TransactionLogger.log(
                    newAccount.getAccountNumber(),
                    "ACCOUNT CREATION",
                    openingBalance,
                    newAccount.getBalance(),
                    "SUCCESS");

        } catch (IllegalArgumentException e) {
            System.out.println("Account creation failed: "
                    + e.getMessage());
        }
    }
}
