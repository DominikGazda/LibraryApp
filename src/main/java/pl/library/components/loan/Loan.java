package pl.library.components.loan;

import pl.library.components.book.Book;
import pl.library.components.customer.Customer;
import pl.library.components.librarian.Librarian;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;
    @NotNull(message = "{pl.library.components.loan.Loan.startLoanDate.NotEmpty}")
    private LocalDateTime startLoanDate;
    private LocalDateTime returnLoanDate;
    private boolean isActive;

    @NotNull(message = "{pl.library.components.loan.Loan.book.NotEmpty}")
    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;

    @NotNull(message = "{pl.library.components.loan.Loan.customer.NotNull}")
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NotNull(message = "{pl.library.components.loan.Loan.librarian.NotNull}")
    @ManyToOne
    @JoinColumn(name = "librarian_id")
    private Librarian librarian;

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

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Librarian getLibrarian() {
        return librarian;
    }

    public void setLibrarian(Librarian librarian) {
        this.librarian = librarian;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return isActive == loan.isActive &&
                Objects.equals(loanId, loan.loanId) &&
                Objects.equals(startLoanDate, loan.startLoanDate) &&
                Objects.equals(returnLoanDate, loan.returnLoanDate) &&
                Objects.equals(book, loan.book) &&
                Objects.equals(customer, loan.customer) &&
                Objects.equals(librarian, loan.librarian);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId, startLoanDate, returnLoanDate, isActive, book, customer, librarian);
    }
}
