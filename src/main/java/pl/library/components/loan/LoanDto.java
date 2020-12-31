package pl.library.components.loan;

import pl.library.components.customer.CustomerDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

public class LoanDto {

    private Long loanId;
    @Null(message = "{pl.library.components.loan.Loan.startLoanDate.NotEmpty}")
    private LocalDateTime startLoanDate;
    private LocalDateTime returnLoanDate;
    private boolean isActive;
    @NotNull(message = "{pl.library.components.loan.Loan.book.NotNull}")
    private Long bookId;
    @NotNull(message = "{pl.library.components.loan.Loan.customer.NotNull}")
    private Long customerId;
    @NotNull(message = "{pl.library.components.loan.Loan.librarianId.NotNull}")
    private Long librarianId;

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public LocalDateTime getStartLoanDate() {
        return startLoanDate;
    }

    public void setStartLoanDate(LocalDateTime startLoanDate) {
        this.startLoanDate = startLoanDate;
    }

    public LocalDateTime getReturnLoanDate() {
        return returnLoanDate;
    }

    public void setReturnLoanDate(LocalDateTime returnLoanDate) {
        this.returnLoanDate = returnLoanDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getLibrarianId() {
        return librarianId;
    }

    public void setLibrarianId(Long librarianId) {
        this.librarianId = librarianId;
    }
}
