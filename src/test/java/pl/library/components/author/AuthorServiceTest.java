package pl.library.components.author;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.author.exceptions.AuthorNotFoundException;
import pl.library.components.book.Book;
import pl.library.components.book.BookDto;
import pl.library.components.book.BookMapper;
import pl.library.components.publisher.Publisher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
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

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("GetAuthorTest")
    @Test
    void getAuthorsTest() {
        //given
        Author a1 = createAuthor(1L,"Marek","Kowalski");
        Author a2 = createAuthor(2L,"Krystian","Kozak");
        List<Author> authorList = List.of(a1,a2);
        List<AuthorDto> authorDtoList = authorList
                .stream()
                .map(AuthorMapper::toDto)
                .collect(Collectors.toList());
        //when
        when(authorRepository.findAll()).thenReturn(authorList);
        //then
        List<AuthorDto> authors = authorService.getAuthors();
        assertThat(authors).hasSameElementsAs(authorDtoList);
    }

    @Nested
    class SaveAuthorTest {
        @Test
        void saveAuthorTest() {
            //given
            Author author = createAuthor(null, "Marek", "Kowalski");
            AuthorDto authorDto = AuthorMapper.toDto(author);
            //when
            when(authorRepository.save(author)).thenReturn(author);
            //then
            assertThat(authorService.saveAuthor(authorDto)).isEqualTo(authorDto);
        }

        @Test
        void shouldThrowException_WhenUserHas_Id() {
            //given
            Author author = createAuthor(2L, "Marek", "Kowalski");
            AuthorDto authorDto = AuthorMapper.toDto(author);
            //then
            assertThatThrownBy(() -> authorService.saveAuthor(authorDto))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessage("400 BAD_REQUEST \"Author cannot have id\"");
        }
    }

    @Nested
    class GetAuthorByIdTest {
        @Test
        void getAuthorByIdTest() {
            //given
            Author author = createAuthor(1L, "Marek", "Kowalski");
            AuthorDto authorDto = AuthorMapper.toDto(author);
            //when
            when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));
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
    void updateAuthorTest(){
        //given
        Author author = createAuthor(1L, "Marek", "Kowalski");
        AuthorDto authorDto = AuthorMapper.toDto(author);
        //when
        when(authorRepository.save(author)).thenReturn(author);
        //then
        assertThat(authorService.updateAuthor(authorDto)).isEqualTo(authorDto);
    }

    @Nested
    class DeleteAuthorTest{
        @Test
        void deleteAuthorTest(){
         //given
         Author author = createAuthor(1L, "Marek", "Kowalski");
         AuthorDto authorDto = AuthorMapper.toDto(author);
         ArgumentCaptor<Author> captor = ArgumentCaptor.forClass(Author.class);
         //when
          when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
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
        void getAllBooksForAuthorByIdTest(){
            //given
            Author author = new Author();
            Book firstBook = createBook(1L,"bookName",10,"1234567891234");
            Book secondBook = createBook(2L, "Marsjanki",20,"9876543219876");
            List<Book>bookList = List.of(firstBook, secondBook);
            List<BookDto> bookDtoList = bookList
                    .stream()
                    .map(bookMapper::toDto)
                    .collect(Collectors.toList());

            author.setAuthorId(1L);
            author.setAuthorName("Marek");
            author.setAuthorSurname("Kowal");
            author.setBookList(bookList);

            //when
            when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));
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
}