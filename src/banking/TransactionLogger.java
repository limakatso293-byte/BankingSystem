
package banking;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// This final class records transactions in a text file.
public final class TransactionLogger {

    // Prevents objects of this utility class from being created.
    private TransactionLogger() {
    }

    // Records successful and failed transactions.
    public static void log(String accountNumber, String type,
                           double amount, double balance,
                           String result) {

        try (FileWriter writer =
                     new FileWriter("transactions.txt", true)) {

            String dateTime = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            writer.write(
                    dateTime
                            + " | Account: " + accountNumber
                            + " | Type: " + type
                            + " | Amount: M" + String.format("%.2f", amount)
                            + " | Balance: M" + String.format("%.2f", balance)
                            + " | Result: " + result
                            + System.lineSeparator()
            );

        } catch (IOException e) {
            System.out.println(
                    "Could not save transaction: " + e.getMessage());
        }
    }
}
