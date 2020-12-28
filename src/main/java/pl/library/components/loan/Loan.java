package pl.library.components.loan;

import pl.library.components.book.Book;
import pl.library.components.customer.Customer;
import pl.library.components.librarian.Librarian;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;
    private LocalDateTime startLoanDate;
    private LocalDateTime returnLoanDate;
    private boolean isActive;

    @OneToMany(mappedBy = "loan")
    private List <Book> bookList;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

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

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
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

    public void addBook(Book book){
        book.setLoan(this);
        this.bookList.add(book);
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
                Objects.equals(bookList, loan.bookList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId, startLoanDate, returnLoanDate, isActive, bookList);
    }
}
