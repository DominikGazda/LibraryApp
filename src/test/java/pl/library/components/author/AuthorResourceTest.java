package pl.library.components.author;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.address.Address;

import java.awt.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
class AuthorResourceTest {

    private MockMvc mockMvc;
    @Mock
    private AuthorService authorService;
    @InjectMocks
    private AuthorResource authorResource;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authorResource).build();
    }

    @DisplayName("GetAuthorsTest")
    @Test
    void getAuthorsTest() throws Exception {
        //given
        AuthorDto firstAuthor = createAuthor(1L,"Damian","Kozak");
        AuthorDto secondAuthor = createAuthor(2L,"Wojciech","Bogdan");
        String url = "http://localhost:8080/api/author";
        List<AuthorDto> authorList = List.of(firstAuthor,secondAuthor);
        //when
        when(authorService.getAuthors()).thenReturn(authorList);
        //then
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].authorId").value(1L))
                .andExpect(jsonPath("$[0].authorName").value("Damian"))
                .andExpect(jsonPath("$[0].authorSurname").value("Kozak"))
                .andExpect(jsonPath("$[1].authorId").value(2L))
                .andExpect(jsonPath("$[1].authorName").value("Wojciech"))
                .andExpect(jsonPath("$[1].authorSurname").value("Bogdan"));
    }

    @Nested
    class SaveAuthorTest{
            @Test
            void saveAuthorTest() throws Exception {
                //given
                AuthorDto author = createAuthor(null,"Damian","Kozak");
                AuthorDto savedAuthor = createAuthor(1L,"Damian","Kozak");
                String url = "http://localhost:8080/api/author";
                //when
                when(authorService.saveAuthor(author)).thenReturn(savedAuthor);
                //then
                mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(author)))
                        .andExpect(jsonPath("$.authorId").value(1L))
                        .andExpect(jsonPath("$.authorName").value("Damian"))
                        .andExpect(jsonPath("$.authorSurname").value("Kozak"))
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isCreated())
                        .andExpect(header().string("Location","http://localhost:8080/api/author/1"));
            }

            @Test
            void shouldThrowException_WhenAuthorName_isEmpty() throws Exception {
                //given
                AuthorDto author = createAuthor(1L,"","Kozak");
                String url = "http://localhost:8080/api/author";
                //when
                when(authorService.saveAuthor(author)).thenReturn(author);
                doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Author name cannot be empty")).when(authorService).checkErrors(any(BindingResult.class));
                //then
                mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(author)))
                        .andExpect(status().isBadRequest())
                        .andExpect(status().reason("Author name cannot be empty"));
            }

        @Test
        void shouldThrowException_WhenAuthorSurname_isEmpty() throws Exception {
            //given
            AuthorDto author = createAuthor(1L,"Bartosz","");
            String url = "http://localhost:8080/api/author";
            //when
            when(authorService.saveAuthor(author)).thenReturn(author);
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Author surname cannot be empty")).when(authorService).checkErrors(any(BindingResult.class));
            //then
            mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(author)))
                    .andExpect(status().isBadRequest())
                    .andExpect(status().reason("Author surname cannot be empty"));
        }

    }

    @DisplayName("GetAuthorByIdTest")
    @Test
    void getAuthorByIdTest() throws Exception {
        //given
        AuthorDto author = createAuthor(1L,"Bartosz","Kozak");
        String url = "http://localhost:8080/api/author/{id}";
        //when
        when(authorService.getAuthorById(anyLong())).thenReturn(author);
        //then
        mockMvc.perform(get(url, anyLong()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.authorId").value(1L))
                .andExpect(jsonPath("$.authorName").value("Bartosz"))
                .andExpect(jsonPath("$.authorSurname").value("Kozak"))
                .andExpect(status().isOk());
    }

    @Nested
    class UpdateAuthorTest{
        @Test
        void updateAuthorTest() throws Exception {
            //given
            AuthorDto author = createAuthor(1L,"Bartosz","Kozak");
            String url = "http://localhost:8080/api/author/{id}";
            //when
            when(authorService.updateAuthor(any(AuthorDto.class))).thenReturn(author);
            //then
            mockMvc.perform(put(url, 1L)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(author)))
                    .andExpect(jsonPath("$.authorId").exists())
                    .andExpect(jsonPath("$.authorName").value("Bartosz"))
                    .andExpect(jsonPath("$.authorSurname").value("Kozak"))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(header().string("Location","http://localhost:8080/api/author/1"))
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldThrowException_WhenAuthorName_isEmpty() throws Exception {
            //given
            AuthorDto author = createAuthor(1L,"","Kozak");
            String url = "http://localhost:8080/api/author/1";
            //when
            when(authorService.updateAuthor(author)).thenReturn(author);
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Author name cannot be empty")).when(authorService).checkErrors(any(BindingResult.class));
            //then
            mockMvc.perform(put(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(author)))
                    .andExpect(status().isBadRequest())
                    .andExpect(status().reason("Author name cannot be empty"));
        }

        @Test
        void shouldThrowException_WhenAuthorSurname_isEmpty() throws Exception {
            //given
            AuthorDto author = createAuthor(1L,"Bartosz","");
            String url = "http://localhost:8080/api/author/1";
            //when
            when(authorService.updateAuthor(author)).thenReturn(author);
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Author surname cannot be empty")).when(authorService).checkErrors(any(BindingResult.class));
            //then
            mockMvc.perform(put(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(author)))
                    .andExpect(status().isBadRequest())
                    .andExpect(status().reason("Author surname cannot be empty"));
        }

        @Test
        void shouldThrowException_WhenAuthorId_IsOtherThanPathVariableId() throws Exception{
            //given
            AuthorDto author = createAuthor(1L,"Bartosz","Kozak");
            String url = "http://localhost:8080/api/author/2";
            //when
            //then
            mockMvc.perform(put(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(author)))
                    .andExpect(status().isBadRequest())
                    .andExpect(status().reason("Author must have id same as path variable"));
        }
    }

    private AuthorDto createAuthor(Long id, String name, String surname){
        AuthorDto author = new AuthorDto();
        author.setAuthorId(id);
        author.setAuthorName(name);
        author.setAuthorSurname(surname);
        return author;
    }


}