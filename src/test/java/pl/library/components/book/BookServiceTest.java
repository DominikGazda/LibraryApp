package pl.library.components.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.author.Author;
import pl.library.components.author.AuthorDto;
import pl.library.components.author.AuthorMapper;
import pl.library.components.author.AuthorRepository;
import pl.library.components.book.exceptions.BookNotFoundException;
import pl.library.components.publisher.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private BookMapper bookMapper;
    private BookService bookService;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        bookService = new BookService(bookRepository,bookMapper,authorRepository);
    }


    @DisplayName("GetBooksTest")
    @Test
    void getBooksTest() {
        //given
        Book book = createBook(1L,"Lew",10,"1234567890987");
        Book book2 = createBook(2L,"Bajka",20,"1234567890900");
        List<Book>bookList = List.of(book, book2);

        BookDto book1 = createBookDto(1L,"Lew","1234567890987",10,"WSIP");
        BookDto book22 = createBookDto(2L,"Bajka","1234567890900",20,"WSIP");
        List<BookDto>bookDtoList = List.of(book1,book22);

        //when
        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDtoList.get(0),bookDtoList.get(1));
        when(bookRepository.findAll()).thenReturn(bookList);

        List<BookDto> essa = bookService.getBooks();
        essa.forEach(System.out::println);
        bookDtoList.forEach(System.out::println);
        //then
        verify(bookMapper, times(2)).toDto(any(Book.class));
        assertEquals(essa, bookDtoList);
    }

    @Nested
    class SaveBookTest {
            @Test
            void saveBookTest() {
                //given
                BookDto bookDto = createBookDto(null, "Bajka", "9876543212345", 10, "WSIP");
                Book book = createBook(1L, "Bajka", 10, "9876543212345");
                //when
                when(bookMapper.toEntity(bookDto)).thenReturn(book);
                when(bookRepository.save(book)).thenReturn(book);
                when(bookMapper.toDto(book)).thenReturn(bookDto);
                //then
                assertThat(bookService.saveBook(bookDto)).isEqualTo(bookDto);
            }

            @Test
            void shouldThrowException_WhenBookHas_Id() {
                //given
                BookDto bookDto = createBookDto(1L, "Bajka", "9876543212345", 10, "WSIP");
                //when
                //then
                assertThatThrownBy(() -> bookService.saveBook(bookDto))
                        .isInstanceOf(ResponseStatusException.class)
                        .hasMessage("400 BAD_REQUEST \"Book cannot have id\"");
            }
    }

    @Nested
    class GetBookByIdTest{
        @Test
        void getBookByIdTest(){
            //given
            Book book = createBook(1L, "Bajka", 10, "9876543212345");
            BookDto bookDto = toDto(book);
            //when
            when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
            when(bookMapper.toDto(any(Book.class))).thenReturn(bookDto);
            //then
            assertThat(bookService.getBookById(anyLong()).equals(bookDto));
        }

        @Test
        void shouldThrowException_WhenBookNotFound(){
            //given
            //when
            when(bookRepository.findById(anyLong())).thenThrow(new BookNotFoundException());
            //then
            assertThatThrownBy(() -> bookService.getBookById(anyLong()))
                    .isInstanceOf(BookNotFoundException.class);
        }
    }

    @DisplayName("UpdateBookTest")
    @Test
    void updateBookTest(){
        //given
        Book book = createBook(1L, "Bajka", 10, "9876543212345");
        BookDto bookDto = toDto(book);
        //when
        when(bookMapper.toEntity(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        //then
        assertThat(bookService.updateBook(bookDto)).isEqualTo(bookDto);
    }

    @Nested
    class DeleteBookTest{
        @Test
        void deleteBookTest() {
            //given
            Book book = createBook(1L, "Bajka", 10, "9876543212345");
            BookDto bookDto = toDto(book);
            //when
            when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
            when(bookMapper.toDto(book)).thenReturn(bookDto);
            BookDto updatedBookDto = bookService.deleteBook(anyLong());
            //then
            ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
            verify(bookRepository).delete(captor.capture());

            assertAll(
                    () -> assertThat(updatedBookDto).isEqualTo(bookDto),
                    () -> assertThat(captor.getValue()).isEqualTo(book)
            );
        }

        @Test
        void shouldThrowException_WhenBookNotFound(){
            //given
            //when
            when(authorRepository.findById(anyLong())).thenThrow(new BookNotFoundException());
            //then
            assertThatThrownBy(() -> bookService.deleteBook(anyLong()))
                    .isInstanceOf(BookNotFoundException.class);
        }
    }

    @Nested
    class GetAllAuthorFromBookByIdTest{
        @Test
        void getAllAuthorFromBookByIdTest(){
            //given
            Book book = createBook(1L, "Bajka", 10, "9876543212345");

            Author firstAuthor = createAuthor(1L,"Marian","Kowalski");
            AuthorDto firstAuthorDto = mapAuthorToDto(firstAuthor);

            Author secondAuthor = createAuthor(2L,"Marcin","Kowal");
            AuthorDto secondAuthorDto = mapAuthorToDto(secondAuthor);

            List<Author> authorList = List.of(firstAuthor, secondAuthor);
            List<AuthorDto> authorDtoList = List.of(firstAuthorDto, secondAuthorDto);

            book.setAuthorList(authorList);
            MockedStatic<AuthorMapper> authorMapper = Mockito.mockStatic(AuthorMapper.class);
            //when
            when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
            authorMapper.when(() -> AuthorMapper.toDto(firstAuthor)).thenReturn(firstAuthorDto);
            authorMapper.when(() -> AuthorMapper.toDto(secondAuthor)).thenReturn(secondAuthorDto);
           //then
            assertThat(bookService.getAllAuthorFromBookById(anyLong())).isEqualTo(authorDtoList);
            authorMapper.close();
        }

        @Test
        void shouldThrowException_WhenBookNotFound(){
            //given
            //when
            when(bookRepository.findById(anyLong())).thenThrow(new BookNotFoundException());
            //then
            assertThatThrownBy(() -> bookService.getAllAuthorFromBookById(anyLong()))
                    .isInstanceOf(BookNotFoundException.class);
        }
    }

    @Nested
    class SaveAuthorForBookId{
        @Test
        void saveAuthorForBookId(){
            //given
            Book book = createBook(1L, "Bajka", 10, "9876543212345");
            Author author = createAuthor(1L,"Marian","Kowalski");
            AuthorDto authorDto = mapAuthorToDto(author);
            List<Author>authorList = List.of(author);
            MockedStatic<AuthorMapper> authorMapper = Mockito.mockStatic(AuthorMapper.class);
            ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
            //when
            when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
            when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));
            authorMapper.when(() -> AuthorMapper.toDto(author)).thenReturn(authorDto);
            //then
            AuthorDto savedAuthor = bookService.saveAuthorForBookById(1L,1L);
            verify(bookRepository).save(captor.capture());
            book.getAuthorList().add(author);

            assertAll(
                    () -> assertThat(savedAuthor).isEqualTo(authorDto),
                    () -> assertThat(captor.getValue()).isEqualTo(book)
            );
            authorMapper.close();
        }

        @Test
        void shouldThrowException_WhenBookNotFound(){
            //given
            //when
            when(bookRepository.findById(anyLong())).thenThrow(new BookNotFoundException());
            //then
            assertThatThrownBy(() -> bookService.saveAuthorForBookById(1L, 1L))
                    .isInstanceOf(BookNotFoundException.class);
        }

        @Test
        void shouldThrowException_WhenAuthorNotFound(){
            //given
            //when
            when(authorRepository.findById(anyLong())).thenThrow(new BookNotFoundException());
            //then
            assertThatThrownBy(() -> bookService.saveAuthorForBookById(1L, 1L))
                    .isInstanceOf(BookNotFoundException.class);
        }
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

    private Author createAuthor(Long id, String name, String surname){
        Author author = new Author();
        author.setAuthorId(id);
        author.setAuthorName(name);
        author.setAuthorSurname(surname);
        return author;
    }

    private  AuthorDto mapAuthorToDto(Author entity){
        AuthorDto dto = new AuthorDto();
        if(entity.getAuthorId() == null)
            dto.setAuthorId(null);
        else
            dto.setAuthorId(entity.getAuthorId());
        dto.setAuthorName(entity.getAuthorName());
        dto.setAuthorSurname(entity.getAuthorSurname());
        return dto;
    }


}