package pl.library.components.book;

import pl.library.components.author.Author;
import pl.library.components.loan.Loan;
import pl.library.components.publisher.Publisher;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;
    private String bookName;
    private String isbn;
    private Integer availableQuantity;

    @ManyToMany
    @JoinTable(name = "book_author",
            joinColumns = {@JoinColumn(name = "id_book", referencedColumnName = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "id_author", referencedColumnName = "author_id")}
            )
    private List<Author> authorList;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToOne
    @JoinColumn(name="loan_id")
    private Loan loan;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(bookId, book.bookId) &&
                Objects.equals(bookName, book.bookName) &&
                Objects.equals(isbn, book.isbn) &&
                Objects.equals(availableQuantity, book.availableQuantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, bookName, isbn, availableQuantity);
    }
}
