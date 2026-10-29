
package banking;

// This interface is used by accounts that can request loans.
public interface Loanable {

    // Checks whether the requested loan can be approved.
    boolean requestLoan(double amount);
}
