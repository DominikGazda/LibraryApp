package pl.library.components.loan;

import pl.library.components.book.BookDto;
import pl.library.components.customer.CustomerDto;
import pl.library.components.librarian.LibrarianDto;

import java.time.LocalDateTime;
import java.util.List;

public class LoanDto {

    private Long loanId;
    private LocalDateTime startLoanDate;
    private LocalDateTime returnLoanDate;
    private boolean isActive;
    private List<BookDto> bookList;
    private CustomerDto customer;
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

    public List<BookDto> getBookList() {
        return bookList;
    }

    public void setBookList(List<BookDto> bookList) {
        this.bookList = bookList;
    }

    public CustomerDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    public Long getLibrarianId() {
        return librarianId;
    }

    public void setLibrarianId(Long librarianId) {
        this.librarianId = librarianId;
    }
}
