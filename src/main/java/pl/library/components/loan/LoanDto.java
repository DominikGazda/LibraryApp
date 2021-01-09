package pl.library.components.loan;

import pl.library.components.customer.CustomerDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.Objects;

public class LoanDto {

    private Long loanId;
    @Null(message = "{pl.library.components.loan.Loan.startLoanDate.Null}")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanDto loanDto = (LoanDto) o;
        return isActive == loanDto.isActive &&
                Objects.equals(loanId, loanDto.loanId) &&
                Objects.equals(startLoanDate, loanDto.startLoanDate) &&
                Objects.equals(returnLoanDate, loanDto.returnLoanDate) &&
                Objects.equals(bookId, loanDto.bookId) &&
                Objects.equals(customerId, loanDto.customerId) &&
                Objects.equals(librarianId, loanDto.librarianId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId, startLoanDate, returnLoanDate, isActive, bookId, customerId, librarianId);
    }

    @Override
    public String toString() {
        return "LoanDto{" +
                "loanId=" + loanId +
                ", startLoanDate=" + startLoanDate +
                ", returnLoanDate=" + returnLoanDate +
                ", isActive=" + isActive +
                ", bookId=" + bookId +
                ", customerId=" + customerId +
                ", librarianId=" + librarianId +
                '}';
    }
}
