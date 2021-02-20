package pl.library.components.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.book.Book;
import pl.library.components.book.BookDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
class PublisherResourceTest {

    private MockMvc mockMvc;
    @Mock
    private PublisherService publisherService;
    @InjectMocks
    private PublisherResource publisherResource;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(publisherResource).build();
    }

    @Test
    void getAllPublishers_ShouldReturnPublisherList() throws Exception {
        //given
        PublisherDto firstPublisherDto = createPublisher(1L,"WSIP");
        PublisherDto secondPublisherDto = createPublisher(2L,"Nowa Era");
        List<PublisherDto> publisherDtoList = List.of(firstPublisherDto, secondPublisherDto);
        String url = "/api/publisher";
        //when
        when(publisherService.getAllPublishers()).thenReturn(publisherDtoList);
        //then
        mockMvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].publisherId").value(1L))
                .andExpect(jsonPath("$[0].publisherName").value("WSIP"))
                .andExpect(jsonPath("$[1].publisherId").value(2L))
                .andExpect(jsonPath("$[1].publisherName").value("Nowa Era"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void savePublisher_ShouldReturnSavedPublisher() throws Exception {
        //given
        PublisherDto publisherDto = createPublisher(null,"WSIP");
        PublisherDto savedPublisherDto = createPublisher(1L,"WSIP");
        String url = "/api/publisher";
        //when
        when(publisherService.savePublisher(publisherDto)).thenReturn(savedPublisherDto);
        //then
        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(new ObjectMapper().writeValueAsString(publisherDto)))
                .andExpect(jsonPath("$.publisherId").value(1L))
                .andExpect(jsonPath("$.publisherName").value("WSIP"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string("Location","http://localhost/api/publisher/1"));
    }

    @Test
    void savePublisher_ShouldThrowException_WhenPublisherNameIsEmpty() throws Exception {
        //given
        PublisherDto publisherDto = createPublisher(null,"");
        String url = "/api/publisher";
        //when
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Publisher name cannot be empty")).when(publisherService).checkErrors(any(BindingResult.class));
        //then
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(publisherDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(status().reason("Publisher name cannot be empty"));
    }

    @Test
    void getPublisherById_ShouldReturnPublisherWithProvidedId() throws Exception {
        //given
        PublisherDto publisherDto = createPublisher(1L,"WSIP");
        String url = "/api/publisher/{id}";
        //when
        when(publisherService.getPublisherById(anyLong())).thenReturn(publisherDto);
        //then
        mockMvc.perform(get(url, anyLong())
               .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.publisherId").value(1L))
                    .andExpect(jsonPath("$.publisherName").value("WSIP"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void updatePublisher_ShouldReturnUpdatedPublisher() throws Exception {
        //given
        PublisherDto publisherDto = createPublisher(1L,"WSIP");
        PublisherDto updatedPublisherDto = createPublisher(1L,"Nowa Era");
        String url = "/api/publisher/{id}";
        //when
        when(publisherService.updatePublisher(publisherDto)).thenReturn(updatedPublisherDto);
        //then
        mockMvc.perform(put(url, 1L)
                .content(new ObjectMapper().writeValueAsString(publisherDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.publisherId").value(1L))
                .andExpect(jsonPath("$.publisherName").value("Nowa Era"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string("Location","http://localhost/api/publisher/1"));
    }

    @Test
    void updatePublisher_ShouldThrowException_WhenPublisherNameIsEmpty() throws Exception{
        //given
        PublisherDto publisherDto = createPublisher(1L,"");
        String url = "/api/publisher/{id}";
        //when
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Publisher name cannot be empty")).when(publisherService).checkErrors(any(BindingResult.class));
        //then
        mockMvc.perform(put(url, anyLong())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(publisherDto)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Publisher name cannot be empty"));
    }

    @Test
    void updatePublisher_ShouldThrowException_WhenPublisherHasIdOtherThanPathVariable() throws Exception {
        //given
        PublisherDto publisherDto = createPublisher(1L,"WSIP");
        String url = "/api/publisher/{id}";
        //when
        //then
        //then
        mockMvc.perform(put(url,2L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(publisherDto)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Publisher must have id same as path variable"));
    }

    @Test
    void deletePublisher_ShouldReturnDeletedPublisher() throws Exception{
        //given
        PublisherDto publisherDto = createPublisher(1L,"WSIP");
        String url = "/api/publisher/{id}";
        //when
        when(publisherService.deletePublisher(anyLong())).thenReturn(publisherDto);
        //then
        mockMvc.perform(delete(url, anyLong())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.publisherId").value(1L))
                .andExpect(jsonPath("$.publisherName").value("WSIP"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void getAllBooksForPublisherById_ShouldReturnBookListAssignedToPublisherWithProvidedId() throws Exception {
        //given
        PublisherDto publisherDto = createPublisher(1L,"WSIP");
        BookDto bookDto = createBookDto(1L,"Lew","1234567890987",10,"WSIP");
        BookDto bookDto2 = createBookDto(2L,"Bajka","1234567890900",20,"Nowa Era");
        List<BookDto> bookDtoList = List.of(bookDto, bookDto2);
        String url = "/api/publisher/{id}/books";
        //when
        when(publisherService.getAllBooksForPublisherById(anyLong())).thenReturn(bookDtoList);
        //then
        mockMvc.perform(get(url, anyLong())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$[0].bookid").value(1L))
                    .andExpect(jsonPath("$[0].bookName").value("Lew"))
                    .andExpect(jsonPath("$[0].isbn").value("1234567890987"))
                    .andExpect(jsonPath("$[0].availableQuantity").value(10))
                    .andExpect(jsonPath("$[0].publisher").value("WSIP"))
                    .andExpect(jsonPath("$[1].bookid").value(2L))
                    .andExpect(jsonPath("$[1].bookName").value("Bajka"))
                    .andExpect(jsonPath("$[1].isbn").value("1234567890900"))
                    .andExpect(jsonPath("$[1].availableQuantity").value(20))
                    .andExpect(jsonPath("$[1].publisher").value("Nowa Era"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }


    private PublisherDto createPublisher(Long id, String name){
        PublisherDto publisherDto = new PublisherDto();
        publisherDto.setPublisherId(id);
        publisherDto.setPublisherName(name);
        return publisherDto;
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
