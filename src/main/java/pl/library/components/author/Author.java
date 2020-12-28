package pl.library.components.author;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import pl.library.components.book.Book;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Long authorId;
    private String authorName;
    private String authorSurname;

    @ManyToMany(mappedBy = "authorList", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Book> bookList;

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorSurname() {
        return authorSurname;
    }

    public void setAuthorSurname(String authorSurname) {
        this.authorSurname = authorSurname;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(authorId, author.authorId) &&
                Objects.equals(authorName, author.authorName) &&
                Objects.equals(authorSurname, author.authorSurname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId, authorName, authorSurname);
    }
}
