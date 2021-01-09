package pl.library.components.author;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.author.exceptions.AuthorNotFoundException;
import pl.library.components.book.Book;
import pl.library.components.book.BookDto;
import pl.library.components.book.BookMapper;
import pl.library.components.publisher.Publisher;

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

class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private AuthorService authorService;
    private MockedStatic<AuthorMapper> authorMapper;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        authorMapper = Mockito.mockStatic(AuthorMapper.class);
    }

    @AfterEach
    void end(){
        authorMapper.close();
    }

    @DisplayName("GetAuthorTest")
    @Test
    void getAuthors_ShouldReturnAuthorsList() {
        //given
        Author a1 = createAuthor(1L,"Marek","Kowalski");
        Author a2 = createAuthor(2L,"Krystian","Kozak");
        List<Author> authorList = List.of(a1,a2);

        AuthorDto a1Dto = mapAuthorToDto(a1);
        AuthorDto a2Dto = mapAuthorToDto(a2);
        List<AuthorDto> authorDtoList = List.of(a1Dto, a2Dto);
        //when
        when(authorRepository.findAll()).thenReturn(authorList);
        authorMapper.when(() -> AuthorMapper.toDto(a1)).thenReturn(a1Dto);
        authorMapper.when(() -> AuthorMapper.toDto(a2)).thenReturn(a2Dto);
        //then
        List<AuthorDto> authors = authorService.getAuthors();
        assertThat(authors).hasSameElementsAs(authorDtoList);
    }

    @Nested
    class SaveAuthorTest {
        @Test
        void saveAuthor_ShouldReturnSavedAuthor() {
            //given
            Author author = createAuthor(null, "Marek", "Kowalski");
            AuthorDto authorDto = mapAuthorToDto(author);

            Author savedAuthor = createAuthor(1L, "Marek", "Kowalski");
            ArgumentCaptor<Author> captor = ArgumentCaptor.forClass(Author.class);

            AuthorDto savedAuthorDto = mapAuthorToDto(savedAuthor);
            //when
            when(authorRepository.save(captor.capture())).thenReturn(savedAuthor);
            authorMapper.when(() -> AuthorMapper.toEntity(authorDto)).thenReturn(author);
            authorMapper.when(() -> AuthorMapper.toDto(savedAuthor)).thenReturn(savedAuthorDto);
            //then
            assertAll(
                    ()-> assertThat(authorService.saveAuthor(authorDto)).isEqualTo(savedAuthorDto),
                    () ->assertThat(captor.getValue()).isEqualTo(author)
            );
        }

        @Test
        void shouldThrowException_WhenUserHas_Id() {
            //given
            Author author = createAuthor(2L, "Marek", "Kowalski");
            AuthorDto authorDto = mapAuthorToDto(author);
            //then
            assertThatThrownBy(() -> authorService.saveAuthor(authorDto))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessage("400 BAD_REQUEST \"Author cannot have id\"");
        }
    }

    @Nested
    class GetAuthorByIdTest {
        @Test
        void getAuthorById_ShouldReturnAuthorWithProvidedId() {
            //given
            Author author = createAuthor(1L, "Marek", "Kowalski");
            AuthorDto authorDto = mapAuthorToDto(author);
            //when
            when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));
            authorMapper.when(() -> AuthorMapper.toDto(author)).thenReturn(authorDto);
            //then
            assertThat(authorService.getAuthorById(anyLong()).equals(authorDto));
        }

        @Test
        void shouldThrowException_WhenCannotFindAuthorBy_Id() {
            //given
            //when
            when(authorRepository.findById(anyLong())).thenThrow(AuthorNotFoundException.class);
            //then
            assertThatThrownBy(() -> authorService.getAuthorById(anyLong()))
                    .isInstanceOf(AuthorNotFoundException.class);
        }
    }

    @DisplayName("UpdateAuthorTest")
    @Test
    void updateAuthor_ShouldReturnUpdatedAuthor(){
        //given
        Author author = createAuthor(1L, "Marek", "Kowalski");
        AuthorDto authorDto = mapAuthorToDto(author);
        //when
        when(authorRepository.save(author)).thenReturn(author);
        authorMapper.when(() -> AuthorMapper.toEntity(authorDto)).thenReturn(author);
        authorMapper.when(() -> AuthorMapper.toDto(author)).thenReturn(authorDto);
        //then
        assertThat(authorService.updateAuthor(authorDto)).isEqualTo(authorDto);
    }

    @Nested
    class DeleteAuthorTest{
        @Test
        void deleteAuthor_ShouldReturnDeletedAuthor(){
         //given
         Author author = createAuthor(1L, "Marek", "Kowalski");
         AuthorDto authorDto = mapAuthorToDto(author);
         ArgumentCaptor<Author> captor = ArgumentCaptor.forClass(Author.class);
         //when
          when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
          authorMapper.when(() -> AuthorMapper.toDto(author)).thenReturn(authorDto);
          AuthorDto deletedAuthor = authorService.deleteAuthor(1L);
          //then
         verify(authorRepository).delete(captor.capture());
         assertAll(
                () -> assertThat(captor.getValue()).isEqualTo(author),
                () -> assertThat(deletedAuthor).isEqualTo(authorDto)
            );
         }

        @Test
        void shouldThrowException_WhenCannotFindAuthorBy_Id() {
         //given
            //when
            when(authorRepository.findById(anyLong())).thenThrow(AuthorNotFoundException.class);
            //then
         assertThatThrownBy(() -> authorService.deleteAuthor(anyLong()))
                   .isInstanceOf(AuthorNotFoundException.class);
        }
    }

    @Nested
    class GetAllBooksForAuthorByIdTest{
        @Test
        void getAllBooksForAuthorById_ShouldReturnBooksListAssignedToAuthorWithProvidedId(){
            //given
            Author author = createAuthor(1L,"Adam","Wolak");
            Book firstBook = createBook(1L,"bookName",10,"1234567891234");
            Book secondBook = createBook(2L, "Marsjanki",20,"9876543219876");

            author.getBookList().add(firstBook);
            author.getBookList().add(secondBook);

            BookDto firstBookDto = mapBookToDto(firstBook);
            BookDto secondBookDto = mapBookToDto(secondBook);
            List<BookDto> bookDtoList = List.of(firstBookDto, secondBookDto);
            //when
            when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));
            when(bookMapper.toDto(any(Book.class))).thenReturn(firstBookDto, secondBookDto);
            //then
            assertThat(authorService.getAllBooksForAuthorById(anyLong())).isEqualTo(bookDtoList);
        }

        @Test
        void shouldThrowException_WhenCannotFindAuthorBy_Id() {
            //given
            //when
            when(authorRepository.findById(anyLong())).thenThrow(AuthorNotFoundException.class);
            //then
            assertThatThrownBy(() -> authorService.getAllBooksForAuthorById(anyLong()))
                    .isInstanceOf(AuthorNotFoundException.class);
        }
    }
    private Author createAuthor(Long id, String name, String surname){
        Author author = new Author();
        author.setAuthorId(id);
        author.setAuthorName(name);
        author.setAuthorSurname(surname);
        author.setBookList(new ArrayList<>());
        return author;
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
        return book;
    }

    private AuthorDto mapAuthorToDto(Author entity){
        AuthorDto dto = new AuthorDto();
        if(entity.getAuthorId() == null)
            dto.setAuthorId(null);
        else
            dto.setAuthorId(entity.getAuthorId());
        dto.setAuthorName(entity.getAuthorName());
        dto.setAuthorSurname(entity.getAuthorSurname());
        return dto;
    }

    public  BookDto mapBookToDto (Book book){
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
}