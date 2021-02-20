package pl.library.components.publisher;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.book.Book;
import pl.library.components.book.BookDto;
import pl.library.components.book.BookMapper;
import pl.library.components.publisher.exceptions.PublisherNotFoundException;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PublisherServiceTest {

    @Mock
    private PublisherRepository publisherRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private PublisherService publisherService;
    private MockedStatic<PublisherMapper> publisherMapper;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        publisherMapper = Mockito.mockStatic(PublisherMapper.class);
    }

    @AfterEach
    void end(){
        publisherMapper.close();
    }

    @Test
    void getAllPublishers_ShouldReturnPublishersList() {
        //given
        PublisherDto firstPublisherDto = createPublisher(1L,"WSIP");
        PublisherDto secondPublisherDto = createPublisher(2L,"Nowa Era");
        List<PublisherDto> publisherDtoList = List.of(firstPublisherDto, secondPublisherDto);

        Publisher firstPublisher = mapToEntity(firstPublisherDto);
        Publisher secondPublisher = mapToEntity(secondPublisherDto);
        List<Publisher> publisherList = List.of(firstPublisher, secondPublisher);
        //when
        when(publisherRepository.findAll()).thenReturn(publisherList);
        publisherMapper.when(() -> PublisherMapper.toDto(any(Publisher.class))).thenReturn(firstPublisherDto, secondPublisherDto);
        //then
        assertThat(publisherService.getAllPublishers()).isEqualTo(publisherDtoList);
    }

    @Test
    void savePublisher_ShouldReturnSavedPublisher(){
        //given
        PublisherDto publisherDto = createPublisher(null,"WSIP");
        PublisherDto savedPublisherDto = createPublisher(1L,"WSIP");
        Publisher publisher = mapToEntity(publisherDto);
        Publisher savedPublisher = mapToEntity(savedPublisherDto);
        //when
        publisherMapper.when(() -> PublisherMapper.toEntity(publisherDto)).thenReturn(publisher);
        when(publisherRepository.save(publisher)).thenReturn(savedPublisher);
        publisherMapper.when(() -> PublisherMapper.toDto(savedPublisher)).thenReturn(savedPublisherDto);
        //then
        assertThat(publisherService.savePublisher(publisherDto)).isEqualTo(savedPublisherDto);
    }

    @Test
    void savePublisher_ShouldThrowResponseStatusException_WhenPublisherHasId(){
        //given
        PublisherDto savedPublisherDto = createPublisher(1L,"WSIP");
        //when
        //then
        assertThatThrownBy(() -> publisherService.savePublisher(savedPublisherDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("400 BAD_REQUEST \"Publisher cannot have id\"");
    }

    @Test
    void getPublisherById_ShouldReturnPublisherWithProvidedId(){
        //given
        PublisherDto publisherDto = createPublisher(1L,"WSIP");
        Publisher publisher = mapToEntity(publisherDto);
        //when
        when(publisherRepository.findById(anyLong())).thenReturn(Optional.of(publisher));
        publisherMapper.when(() -> PublisherMapper.toDto(publisher)).thenReturn(publisherDto);
        //then
        assertThat(publisherService.getPublisherById(anyLong())).isEqualTo(publisherDto);
    }

    @Test
    void getPublisherById_ShouldThrowPublisherNotFoundException_WhenPublisherNotExists(){
        //given
        //when
        when(publisherRepository.findById(anyLong())).thenThrow(PublisherNotFoundException.class);
        //then
        assertThatThrownBy(() -> publisherService.getPublisherById(anyLong()))
                .isInstanceOf(PublisherNotFoundException.class);
    }

    @Test
    void updatePublisher_ShouldReturnUpdatedPublisher(){
        //given
        PublisherDto publisherDto = createPublisher(1L,"WSIP");
        PublisherDto updatedPublisherDto = createPublisher(1L,"WSIP");
        Publisher publisher = mapToEntity(publisherDto);
        Publisher updatedPublisher = mapToEntity(updatedPublisherDto);
        //when
        publisherMapper.when(() -> PublisherMapper.toEntity(publisherDto)).thenReturn(publisher);
        when(publisherRepository.save(publisher)).thenReturn(updatedPublisher);
        publisherMapper.when(() -> PublisherMapper.toDto(updatedPublisher)).thenReturn(updatedPublisherDto);
        //then
        assertThat(publisherDto).isEqualTo(updatedPublisherDto);
    }

    @Test
    void deletePublisher_ShouldReturnDeletedPublisher(){
        //given
        PublisherDto publisherDto = createPublisher(1L,"WSIP");
        Publisher publisher= mapToEntity(publisherDto);
        ArgumentCaptor<Publisher> captor = ArgumentCaptor.forClass(Publisher.class);
        //when
        when(publisherRepository.findById(anyLong())).thenReturn(Optional.of(publisher));
        publisherMapper.when(() -> PublisherMapper.toDto(publisher)).thenReturn(publisherDto);
        //then
        PublisherDto deletedPublisher = publisherService.deletePublisher(anyLong());
        verify(publisherRepository).delete(captor.capture());

        assertThat(deletedPublisher).isEqualTo(publisherDto);
        assertThat(publisher).isEqualTo(captor.getValue());
    }

    @Test
    void deletePublisher_ShouldThrowException_WhenPublisherNotExists(){
        //given
        //when
        when(publisherRepository.findById(anyLong())).thenThrow(PublisherNotFoundException.class);
        //then
        assertThatThrownBy(() -> publisherService.deletePublisher(anyLong()))
                .isInstanceOf(PublisherNotFoundException.class);
    }

    @Test
    void getAllBooksForPublisherById_ShouldReturnBookListAssignedToPublisherWithProvidedId(){
        //given
        PublisherDto publisherDto = createPublisher(1L,"WSIP");
        Publisher publisher = mapToEntity(publisherDto);

        Book book = createBook(1L,"Lew",10,"1234567890987");
        Book book2 = createBook(2L,"Bajka",20,"1234567890900");

        BookDto bookDto = createBookDto(1L,"Lew","1234567890987",10,"WSIP");
        BookDto bookDto2 = createBookDto(2L,"Bajka","1234567890900",20,"WSIP");

       List<BookDto> bookDtoList = List.of(bookDto, bookDto2);

        publisher.getBookList().add(book);
        publisher.getBookList().add(book2);
        //when
        when(publisherRepository.findById(anyLong())).thenReturn(Optional.of(publisher));
        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDto, bookDto2);
        //then
        assertThat(publisherService.getAllBooksForPublisherById(anyLong())).isEqualTo(bookDtoList);
    }

    @Test
    void getAllBooksForPublisherById_ShouldThrowPublisherNotFoundException(){
        //given
        //when
        when(publisherRepository.findById(anyLong())).thenThrow(PublisherNotFoundException.class);
        //then
        assertThatThrownBy(() -> publisherService.getAllBooksForPublisherById(anyLong()))
                .isInstanceOf(PublisherNotFoundException.class);
    }

    private PublisherDto createPublisher(Long id, String name){
        PublisherDto publisherDto = new PublisherDto();
        publisherDto.setPublisherId(id);
        publisherDto.setPublisherName(name);
        return publisherDto;
    }

    private Publisher mapToEntity (PublisherDto dto){
        Publisher entity = new Publisher();
        entity.setPublisherId(dto.getPublisherId());
        entity.setPublisherName(dto.getPublisherName());
        entity.setBookList(new ArrayList<>());
        return entity;
    }

    private Book createBook(Long id, String bookName, int quantity, String isbn){
        Book book = new Book();
        book.setBookId(id);
        book.setBookName(bookName);
        book.setAvailableQuantity(quantity);
        book.setIsbn(isbn);
        book.setAuthorList(new ArrayList<>());
        return book;
    }

    private BookDto createBookDto(Long id, String name, String isbn, int quantity, String publisher){
        BookDto bookDto = new BookDto();

        bookDto.setBookid(id);
        bookDto.setBookName(name);
        bookDto.setAvailableQuantity(quantity);
        bookDto.setIsbn(isbn);
        bookDto.setPublisher(publisher);
        return bookDto;
    }
}