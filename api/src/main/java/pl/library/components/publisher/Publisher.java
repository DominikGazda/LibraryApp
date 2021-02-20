package pl.library.components.publisher;

import pl.library.components.book.Book;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "publishers")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long publisherId;
    @NotEmpty(message = "{pl.library.components.publisher.Publisher.publisherName.NotEmpty}")
    private String publisherName;

    @OneToMany(mappedBy = "publisher")
    private List<Book> bookList;

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public void addBook(Book book){
        book.setPublisher(this);
        bookList.add(book);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publisher publisher = (Publisher) o;
        return Objects.equals(publisherId, publisher.publisherId) &&
                Objects.equals(publisherName, publisher.publisherName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publisherId, publisherName);
    }
}
