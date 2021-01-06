package pl.library.components.librarian;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
class LibrarianResourceTest {

    private MockMvc mockMvc;
    @Mock
    private LibrarianService librarianService;
    @InjectMocks
    private LibrarianResource librarianResource;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(librarianResource).build();
    }

    @Test
    void getLibrarians_ShouldReturnLibrarianList() throws Exception {
        //given
        LibrarianDto firstLibrarianDto = createLibrarian(1L,"Marek","Kowal");
        LibrarianDto secondLibrarianDto = createLibrarian(2L,"Marcin","Kozak");
        List<LibrarianDto> librarianDtoList = List.of(firstLibrarianDto, secondLibrarianDto);
        String url = "/api/librarian";
        //when
        when(librarianService.getLibrarians()).thenReturn(librarianDtoList);
        //then
        mockMvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(new ObjectMapper().writeValueAsString(librarianDtoList)))
                .andExpect(jsonPath("$[0].librarianId").value(1L))
                .andExpect(jsonPath("$[0].librarianName").value("Marek"))
                .andExpect(jsonPath("$[0].librarianSurname").value("Kowal"))
                .andExpect(jsonPath("$[1].librarianId").value(2L))
                .andExpect(jsonPath("$[1].librarianName").value("Marcin"))
                .andExpect(jsonPath("$[1].librarianSurname").value("Kozak"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void saveLibrarian_ShouldReturnSavedLibrarian() throws Exception{
        //given
        LibrarianDto librarianDto = createLibrarian(null,"Marek","Kowal");
        LibrarianDto savedLibrarianDto = createLibrarian(1L,"Marek","Kowal");
        String url = "/api/librarian";
        //when
        when(librarianService.saveLibrarian(librarianDto)).thenReturn(savedLibrarianDto);
        //then
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(librarianDto)))
                .andExpect(jsonPath("$.librarianId").value(1L))
                .andExpect(jsonPath("$.librarianName").value("Marek"))
                .andExpect(jsonPath("$.librarianSurname").value("Kowal"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string("Location","http://localhost/api/librarian/1"));
    }

    @Test
    void saveLibrarian_ShouldThrowResponseStatusException_WhenLibrarianNameIsEmpty() throws Exception{
        //given
        LibrarianDto librarianDto = createLibrarian(null,"","Kowal");
        String url = "/api/librarian";
        //when
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Librarian name cannot be empty")).when(librarianService).checkErrors(any(BindingResult.class));
        //then
         mockMvc.perform(post(url)
                 .contentType(MediaType.APPLICATION_JSON_VALUE)
                 .content(new ObjectMapper().writeValueAsString(librarianDto)))
                 .andExpect(status().isBadRequest())
                 .andExpect(status().reason("Librarian name cannot be empty"));
    }

    @Test
    void saveLibrarian_ShouldThrowResponseStatusException_WhenLibrarianSurnameIsEmpty() throws Exception{
        //given
        LibrarianDto librarianDto = createLibrarian(null,"Damian","");
        String url = "/api/librarian";
        //when
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Librarian surname cannot be empty")).when(librarianService).checkErrors(any(BindingResult.class));
        //then
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(librarianDto)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Librarian surname cannot be empty"));
    }

    @Test
    void getLibrarianById_ShouldReturnLibrarianWithProvidedId() throws Exception{
        //given
        LibrarianDto librarianDto = createLibrarian(1L,"Damian","Kozak");
        String url = "/api/librarian/{id}";
        //when
        when(librarianService.getLibrarianById(anyLong())).thenReturn(librarianDto);
        //then
        mockMvc.perform(get(url, anyLong())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(librarianDto)))
                .andExpect(jsonPath("$.librarianId").value(1L))
                .andExpect(jsonPath("$.librarianName").value("Damian"))
                .andExpect(jsonPath("$.librarianSurname").value("Kozak"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void updateLibrarian_ShouldReturnUpdatedLibrarian() throws Exception{
        //given
        LibrarianDto librarianDto = createLibrarian(1L,"Damian","Kozak");
        LibrarianDto updatedLibrarianDto = createLibrarian(1L,"Karol","Kozak");
        String url = "/api/librarian/{id}";
        //when
        when(librarianService.updateLibrarian(librarianDto)).thenReturn(updatedLibrarianDto);
        //then
        mockMvc.perform(put(url, 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(librarianDto)))
                    .andExpect(jsonPath("$.librarianId").value(1L))
                    .andExpect(jsonPath("$.librarianName").value("Karol"))
                    .andExpect(jsonPath("$.librarianSurname").value("Kozak"))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(header().string("Location","http://localhost/api/librarian/1"));
    }

    @Test
    void updateLibrarian_ShouldThrowResponseStatusException_WhenLibrarianNameIsEmpty() throws Exception{
        //given
        LibrarianDto librarianDto = createLibrarian(1L,"","Kozak");
        String url = "/api/librarian/{id}";
        //when
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Librarian name cannot be empty")).when(librarianService).checkErrors(any(BindingResult.class));
        //then
        mockMvc.perform(put(url, 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(librarianDto)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Librarian name cannot be empty"));
    }

    @Test
    void updateLibrarian_ShouldThrowResponseStatusException_WhenLibrarianSurnameIsEmpty() throws Exception{
        //given
        LibrarianDto librarianDto = createLibrarian(1L,"","Kozak");
        String url = "/api/librarian/{id}";
        //when
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Librarian surname cannot be empty")).when(librarianService).checkErrors(any(BindingResult.class));
        //then
        mockMvc.perform(put(url, 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(librarianDto)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Librarian surname cannot be empty"));
    }

    @Test
    void updateLibrarian_ShouldThrowResponseStatusException_WhenLibrarianIdIsOtherThanPathVariable() throws Exception{
        //given
        LibrarianDto librarianDto = createLibrarian(1L,"","Kozak");
        String url = "/api/librarian/{id}";
        //when
        //then
        mockMvc.perform(put(url, 2L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(librarianDto)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Librarian must have id same as path variable"));
    }

    @Test
    void deleteLibrarian_ShouldReturnDeletedLibrarian() throws Exception{
        //given
        LibrarianDto librarianDto = createLibrarian(1L,"Karol","Kozak");
        String url = "/api/librarian/{id}";
        //when
        when(librarianService.deleteLibrarian(anyLong())).thenReturn(librarianDto);
        //then
        mockMvc.perform(delete(url, anyLong())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(librarianDto)))
                .andExpect(jsonPath("$.librarianId").value(1L))
                .andExpect(jsonPath("$.librarianName").value("Karol"))
                .andExpect(jsonPath("$.librarianSurname").value("Kozak"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    private LibrarianDto createLibrarian(Long id, String name, String surname){
        LibrarianDto librarianDto = new LibrarianDto();
        librarianDto.setLibrarianId(id);
        librarianDto.setLibrarianName(name);
        librarianDto.setLibrarianSurname(surname);
        return librarianDto;
    }

}