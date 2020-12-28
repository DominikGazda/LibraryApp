package pl.library.components.book;

public class BookMapper {

    public static BookDto toDto (Book book){
        BookDto dto = new BookDto();
        dto.setBookid(book.getBookId());
        dto.setBookName(book.getBookName());
        dto.setIsbn(book.getIsbn());
        dto.setPublisher(book.getPublisher().getPublisherName());
        dto.setAvailableQuantity(book.getAvailableQuantity());
        return dto;
    }
}
