package pl.library.components.loan;

import java.time.LocalDateTime;

public class LoanDto {

    private Long loanId;
    private LocalDateTime startLoanDate;
    private LocalDateTime returnLoanDate;
    private boolean isActive;
    private String bookName;
    private String customerName;
    private String customerSurname;
    private String librarianName;
    private String librarianSurname;

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

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSurname() {
        return customerSurname;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }

    public String getLibrarianName() {
        return librarianName;
    }

    public void setLibrarianName(String librarianName) {
        this.librarianName = librarianName;
    }

    public String getLibrarianSurname() {
        return librarianSurname;
    }

    public void setLibrarianSurname(String librarianSurname) {
        this.librarianSurname = librarianSurname;
    }
}
