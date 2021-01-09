package pl.library.components.book;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.parameters.P;
import pl.library.components.publisher.Publisher;
import pl.library.components.publisher.PublisherDto;
import pl.library.components.publisher.PublisherRepository;
import pl.library.components.publisher.exceptions.PublisherNotFoundException;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class BookMapperTest {

    @Mock
    private PublisherRepository publisherRepository;
    @InjectMocks
    private BookMapper bookMapper;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toDto_ShouldReturnBookDto() {
        //given
        Book book = createBook(1L,"W pustyni i w puszczy",20,"9876789273655");
        BookDto bookDto = toDto(book);
        //when
        //then
        BookDto resultBook = bookMapper.toDto(book);
        assertThat(resultBook).isEqualTo(bookDto);
    }

    @Test
    void toEntity_ShouldReturnBookEntity() {
        //given
        Book book = createBook(1L,"W pustyni i w puszczy",20,"9876789273655");
        BookDto bookDto = toDto(book);
        Publisher publisher = createPublisher(1L, "WSIP");
        //when
        when(publisherRepository.findByPublisherNameIgnoreCase(anyString())).thenReturn(Optional.of(publisher));
        //then
        Book resultBook = bookMapper.toEntity(bookDto);
        assertThat(resultBook).isEqualTo(book);
    }

    @Test
    void toEntity_ShouldThrowPublisherNotFoundException(){
        //given
        Book book = createBook(1L,"W pustyni i w puszczy",20,"9876789273655");
        BookDto bookDto = toDto(book);
        //when
        when(publisherRepository.findByPublisherNameIgnoreCase(anyString())).thenThrow(new PublisherNotFoundException());
        //then
        assertThatThrownBy(() -> bookMapper.toEntity(bookDto))
                .isInstanceOf(PublisherNotFoundException.class);
    }

    private Book createBook(Long id, String bookName, int quantity, String isbn){
        Book book = new Book();
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1L);
        publisher.setPublisherName("WSIP");
        book.setBookId(id);
        book.setBookName(bookName);
        book.setAvailableQuantity(quantity);
        book.setPublisher(publisher);
        book.setIsbn(isbn);
        book.setAuthorList(new ArrayList<>());
        return book;
    }

    private BookDto toDto (Book book){
        BookDto dto = new BookDto();
        if(book.getBookId() == null)
            dto.setBookid(null);
        else
            dto.setBookid(book.getBookId());
        dto.setBookid(book.getBookId());
        dto.setBookName(book.getBookName());
        dto.setIsbn(book.getIsbn());
        dto.setPublisher(book.getPublisher().getPublisherName());
        dto.setAvailableQuantity(book.getAvailableQuantity());
        return dto;
    }

    private Publisher createPublisher(Long id, String name){
        Publisher publisher = new Publisher();
        publisher.setPublisherId(id);
        publisher.setPublisherName(name);
        publisher.setBookList(new ArrayList<>());
        return publisher;
    }

}