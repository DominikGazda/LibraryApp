package pl.library.components.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.author.AuthorDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
class BookResourceTest {

    private MockMvc mockMvc;
    @Mock
    private BookService bookService;
    @InjectMocks
    private BookResource bookResource;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookResource).build();
    }

    @DisplayName("GetBooksTest")
    @Test
    void getBooks_ShouldReturnBooksList() throws Exception {
        //given
        BookDto firstBook = createBookDto(1L,"Bajka","WSIP","9876543212345",10);
        BookDto secondBook = createBookDto(2L,"Wiedźmin","Nowa Era","9876543212300",20);
        List<BookDto> bookDtoList = List.of(firstBook, secondBook);
        String url = "/api/book";
        //when
        when(bookService.getBooks()).thenReturn(bookDtoList);
        //then
        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].bookid").value(1L))
                .andExpect(jsonPath("$[0].bookName").value("Bajka"))
                .andExpect(jsonPath("$[0].publisher").value("WSIP"))
                .andExpect(jsonPath("$[0].isbn").value("9876543212345"))
                .andExpect(jsonPath("$[0].availableQuantity").value(10))
                .andExpect(jsonPath("$[1].bookid").value(2L))
                .andExpect(jsonPath("$[1].bookName").value("Wiedźmin"))
                .andExpect(jsonPath("$[1].publisher").value("Nowa Era"))
                .andExpect(jsonPath("$[1].isbn").value("9876543212300"))
                .andExpect(jsonPath("$[1].availableQuantity").value(20))
                .andExpect(status().isOk());
    }

    @Nested
    class SaveBookTest{
        @Test
        void saveBook_ShouldReturnSavedBook() throws Exception {
            //given
            BookDto book = createBookDto(null,"Bajka","WSIP","9876543212345",10);
            BookDto savedBook = createBookDto(1L,"Bajka","WSIP","9876543212345",10);
            String url = "/api/book";
            //when
            when(bookService.saveBook(book)).thenReturn(savedBook);
            //then
            mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(book)))
                    .andExpect(jsonPath("$.bookid").value(1L))
                    .andExpect(jsonPath("$.bookName").value("Bajka"))
                    .andExpect(jsonPath("$.publisher").value("WSIP"))
                    .andExpect(jsonPath("$.isbn").value("9876543212345"))
                    .andExpect(jsonPath("$.availableQuantity").value(10))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location","http://localhost/api/book/1"));

        }

        @Test
        void shouldThrowException_WhenBookName_IsBlank() throws Exception {
            //given
            BookDto book = createBookDto(1L,"","WSIP","9876543212345",10);
            String url = "/api/book";
            //when
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Book name cannot be empty")).when(bookService).checkErrors(any(BindingResult.class));
            //then
            mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(book)))
                    .andExpect(status().isBadRequest())
                    .andExpect(status().reason("Book name cannot be empty"));
        }

        @Test
        void shouldThrowException_WhenBookIsb_BadPattern() throws Exception {
            //given
            BookDto book = createBookDto(1L,"Bajka","WSIP","987654321234522",10);
            String url = "/api/book";
            //when
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Isbn number has to have 13 digits")).when(bookService).checkErrors(any(BindingResult.class));
            //then
            mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(book)))
                    .andExpect(status().isBadRequest())
                    .andExpect(status().reason("Isbn number has to have 13 digits"));
        }

        @Test
        void shouldThrowException_WhenBookPublisher_IsBlank() throws Exception {
            //given
            BookDto book = createBookDto(1L,"Bajka","WSIP","987654321234522",10);
            String url = "/api/book";
            //when
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Publisher field cannot be empty")).when(bookService).checkErrors(any(BindingResult.class));
            //then
            mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(book)))
                    .andExpect(status().isBadRequest())
                    .andExpect(status().reason("Publisher field cannot be empty"));
        }
    }

    @DisplayName("GetBookByIdTest")
    @Test
    void getBookById_ShouldReturnBookWithProvidedId(){
        //given
        BookDto book = createBookDto(1L,"Bajka","WSIP","987654321234522",10);
        String url = "/api/book/{id}";
        //when
        when(bookService.getBookById(anyLong())).thenReturn(book);
        //then
        assertThat(bookResource.getBookById(anyLong())).isEqualTo(book);
    }

    @Nested
    class UpdateBookTest{
        @Test
        void updateBook_ShouldReturnUpdatedBook() throws Exception {
            //given
            BookDto book = createBookDto(1L,"Bajka","WSIP","987654321234522",10);
            BookDto updatedBook = createBookDto(1L,"Majka","WSIP","987654321234522",10);
            String url = "/api/book/1";
            //when
            when(bookService.updateBook(book)).thenReturn(updatedBook);
            //then
            mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(book)))
                    .andExpect(jsonPath("$.bookid").exists())
                    .andExpect(jsonPath("$.bookName").value("Majka"))
                    .andExpect(jsonPath("$.publisher").value("WSIP"))
                    .andExpect(jsonPath("$.isbn").value("987654321234522"))
                    .andExpect(jsonPath("$.availableQuantity").value(10))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location","http://localhost/api/book/1"));
        }

        @Test
        void shouldThrowException_WhenBookName_IsBlank() throws Exception {
            //given
            BookDto book = createBookDto(1L,"","WSIP","9876543212345",10);
            String url = "/api/book";
            //when
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Book name cannot be empty")).when(bookService).checkErrors(any(BindingResult.class));
            //then
            mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(book)))
                    .andExpect(status().isBadRequest())
                    .andExpect(status().reason("Book name cannot be empty"));
        }

        @Test
        void shouldThrowException_WhenBookIsb_BadPattern() throws Exception {
            //given
            BookDto book = createBookDto(1L,"Bajka","WSIP","987654321234522",10);
            String url = "/api/book";
            //when
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Isbn number has to have 13 digits")).when(bookService).checkErrors(any(BindingResult.class));
            //then
            mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(book)))
                    .andExpect(status().isBadRequest())
                    .andExpect(status().reason("Isbn number has to have 13 digits"));
        }

        @Test
        void shouldThrowException_WhenBookPublisher_IsBlank() throws Exception {
            //given
            BookDto book = createBookDto(1L,"Bajka","WSIP","987654321234522",10);
            String url = "/api/book";
            //when
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Publisher field cannot be empty")).when(bookService).checkErrors(any(BindingResult.class));
            //then
            mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(book)))
                    .andExpect(status().isBadRequest())
                    .andExpect(status().reason("Publisher field cannot be empty"));
        }

        @Test
        void shouldThrowException_WhenBookIdIsOtherThan_PathVariableId() throws Exception {
            //given
            BookDto book = createBookDto(1L,"Bajka","WSIP","987654321234522",10);
            String url = "/api/book/{id}";
            //when
            when(bookService.updateBook(book)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Book must have id same as path variable"));
            //then
            mockMvc.perform(put(url,2L)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(book)))
                    .andExpect(status().isBadRequest())
                    .andExpect(status().reason("Book must have id same as path variable"));
        }
    }

    @DisplayName("DeleteBookTest")
    @Test
    void deleteBook_ShouldReturnDeletedBook() throws Exception {
        //given
        BookDto book = createBookDto(1L,"Bajka","WSIP","987654321234522",10);
        String url ="/api/book/{id}";
        //when
        when(bookService.deleteBook(anyLong())).thenReturn(book);
        //then
        mockMvc.perform(delete(url,anyLong())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.bookid").exists())
                .andExpect(jsonPath("$.bookName").value("Bajka"))
                .andExpect(jsonPath("$.publisher").value("WSIP"))
                .andExpect(jsonPath("$.isbn").value("987654321234522"))
                .andExpect(status().isOk());
    }

    @DisplayName("GetAllAuthorFromBookByIdTest")
    @Test
    void getAllAuthorFromBookById_ShouldReturnAuthorListAssignedToBookWithProvidedId() throws Exception {
        //given
        AuthorDto firstAuthor = createAuthorDto(1L,"Marek","Kowal");
        AuthorDto secondAuthor = createAuthorDto(2L,"Dawid","Marek");
        List<AuthorDto> authorDtoList = List.of(firstAuthor, secondAuthor);
        String url = "/api/book/{id}/authors";
        //when
        when(bookService.getAllAuthorFromBookById(anyLong())).thenReturn(authorDtoList);
        //then
        mockMvc.perform(get(url,anyLong())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].authorId").value(1L))
                .andExpect(jsonPath("$[0].authorName").value("Marek"))
                .andExpect(jsonPath("$[0].authorSurname").value("Kowal"))
                .andExpect(jsonPath("$[1].authorId").value(2L))
                .andExpect(jsonPath("$[1].authorName").value("Dawid"))
                .andExpect(jsonPath("$[1].authorSurname").value("Marek"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @DisplayName("SaveAuthorForBookByIdTest")
    @Test
    void saveAuthorForBookById_ShouldReturnSavedAuthor() throws Exception {
        //given
        AuthorDto dto = createAuthorDto(1L,"Marek","Kowal");
        String url = "/api/book/{idBook}/authors/{idAuthor}";
        //when
        when(bookService.saveAuthorForBookById(anyLong(),anyLong())).thenReturn(dto);
        //then
        mockMvc.perform(post(url,anyLong(),anyLong())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.authorId").exists())
                .andExpect(jsonPath("$.authorName").value("Marek"))
                .andExpect(jsonPath("$.authorSurname").value("Kowal"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    private BookDto createBookDto(Long id, String name, String publisher, String isbn, int quantity){
        BookDto book = new BookDto();
        book.setBookid(id);
        book.setBookName(name);
        book.setPublisher(publisher);
        book.setIsbn(isbn);
        book.setAvailableQuantity(quantity);
        return book;
    }

    private AuthorDto createAuthorDto(Long id, String name, String surname){
        AuthorDto dto = new AuthorDto();
        dto.setAuthorId(id);
        dto.setAuthorName(name);
        dto.setAuthorSurname(surname);
        return dto;
    }
}