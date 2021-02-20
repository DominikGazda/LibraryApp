package pl.library.components.book;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;

public class BookDto {

    private Long bookid;
    @NotBlank(message = "{pl.library.components.book.Book.bookName.NotBlank}")
    private String bookName;
    @Pattern(regexp = "^[0-9]{13}?$", message = "{pl.library.components.book.Book.isbn.Pattern}")
    private String isbn;
    private Integer availableQuantity;
    @NotBlank(message = "{pl.library.components.book.BookDto.publisher.NotBlank}")
    private String publisher;
    private String imageUrl;

    public Long getBookid() {
        return bookid;
    }

    public void setBookid(Long bookid) {
        this.bookid = bookid;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDto bookDto = (BookDto) o;
        return Objects.equals(bookid, bookDto.bookid) &&
                Objects.equals(bookName, bookDto.bookName) &&
                Objects.equals(isbn, bookDto.isbn) &&
                Objects.equals(availableQuantity, bookDto.availableQuantity) &&
                Objects.equals(publisher, bookDto.publisher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookid, bookName, isbn, availableQuantity, publisher);
    }
}
