package pl.library.components.book;

import org.springframework.stereotype.Service;
import pl.library.components.publisher.Publisher;
import pl.library.components.publisher.PublisherRepository;
import pl.library.components.publisher.exceptions.PublisherNotFoundException;

@Service
public class BookMapper {

    private PublisherRepository publisherRepository;

    public BookMapper(PublisherRepository publisherRepository){
        this.publisherRepository = publisherRepository;
    }

    public  BookDto toDto (Book book){
        BookDto dto = new BookDto();
        dto.setBookid(book.getBookId());
        dto.setBookName(book.getBookName());
        dto.setIsbn(book.getIsbn());
        dto.setPublisher(book.getPublisher().getPublisherName());
        dto.setAvailableQuantity(book.getAvailableQuantity());
        return dto;
    }

    public  Book toEntity (BookDto dto){
        Book book = new Book();
        Publisher publisher = publisherRepository.findByPublisherNameIgnoreCase(dto.getPublisher())
                .orElseThrow(PublisherNotFoundException::new);

        book.setBookName(dto.getBookName());
        book.setBookId(dto.getBookid());
        book.setIsbn(dto.getIsbn());
        book.setAvailableQuantity(dto.getAvailableQuantity());
        book.setPublisher(publisher);
        return book;
    }
}
