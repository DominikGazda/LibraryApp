package pl.library.components.loan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.address.Address;
import pl.library.components.book.Book;
import pl.library.components.customer.Customer;
import pl.library.components.librarian.Librarian;
import pl.library.components.publisher.Publisher;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMapOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
class LoanResourceTest {

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    @Mock
    private LoanService loanService;
    @InjectMocks
    private LoanResource loanResource;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loanResource).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    void getLoans_ShouldReturnLoanList() throws Exception {
        //given
        Book firstBook = createBook(1L,"Bajka",10,"9876548293721");
        Customer firstCustomer = createCustomer(1L,"Marek","Kowal");
        Librarian firstLibrarian = createLibrarian(1L,"Damian","Kozak");
        Loan firstLoan = createLoan(1L,firstBook,firstCustomer,firstLibrarian);

        Book secondBook = createBook(2L,"Bajka",10,"9876548293700");
        Customer secondCustomer = createCustomer(2L,"Marek","Kawka");
        Librarian secondLibrarian = createLibrarian(1L,"Damian","Kozak");
        Loan secondLoan = createLoan(2L, secondBook, secondCustomer, secondLibrarian);

        LoanDto firstLoanDto = mapLoanToDto(firstLoan);
        LoanDto secondLoanDto = mapLoanToDto(secondLoan);
        String url = "/api/loan";
        //when
        when(loanService.getLoans()).thenReturn(List.of(firstLoanDto, secondLoanDto));
        //then
        mockMvc.perform(get(url))
                .andExpect(jsonPath("$[0].loanId").value(1L))
                .andExpect(jsonPath("$[0].startLoanDate").value(mapDateToList(returnExampleDateTime())))
                .andExpect(jsonPath("$[0].returnLoanDate").value(firstLoanDto.getReturnLoanDate()))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[0].bookId").value(1L))
                .andExpect(jsonPath("$[0].customerId").value(1L))
                .andExpect(jsonPath("$[0].librarianId").value(1L))
                .andExpect(jsonPath("$[1].loanId").value(2L))
                .andExpect(jsonPath("$[1].startLoanDate").value(mapDateToList(returnExampleDateTime())))
                .andExpect(jsonPath("$[1].returnLoanDate").value(secondLoanDto.getReturnLoanDate()))
                .andExpect(jsonPath("$[1].active").value(true))
                .andExpect(jsonPath("$[1].bookId").value(2L))
                .andExpect(jsonPath("$[1].customerId").value(2L))
                .andExpect(jsonPath("$[1].librarianId").value(1L))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void  saveLoan_ShouldReturnSavedLoan() throws Exception {
        //given
        Book firstBook = createBook(1L,"Bajka",10,"9876548293721");
        Customer firstCustomer = createCustomer(1L,"Marek","Kowal");
        Librarian firstLibrarian = createLibrarian(1L,"Damian","Kozak");
        Loan firstLoan = createLoan(null,firstBook,firstCustomer,firstLibrarian);
        LoanDto firstLoanDto = mapLoanToDto(firstLoan);
        firstLoanDto.setStartLoanDate(null);

        Loan savedFirstLoan = createLoan(1L,firstBook,firstCustomer,firstLibrarian);
        LoanDto savedFirstLoanDto = mapLoanToDto(savedFirstLoan);
        savedFirstLoanDto.setLoanId(1L);
        String url = "/api/loan";
        //when
        when(loanService.saveLoan(any(LoanDto.class))).thenReturn(savedFirstLoanDto);
        //then
        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(new ObjectMapper().writeValueAsString(firstLoanDto)))
                .andExpect(jsonPath("$.loanId").value(1L))
                .andExpect(jsonPath("$.startLoanDate").value(mapDateToList(returnExampleDateTime())))
                .andExpect(jsonPath("$.returnLoanDate").value(firstLoanDto.getReturnLoanDate()))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.bookId").value(1L))
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.librarianId").value(1L))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location","http://localhost/api/loan/1"));
    }

    @Test
    void saveLoan_ShouldThrowResponseStatusException_WhenLoanHasStartDate() throws Exception{
        //given
        Book firstBook = createBook(1L,"Bajka",10,"9876548293721");
        Customer firstCustomer = createCustomer(1L,"Marek","Kowal");
        Librarian firstLibrarian = createLibrarian(1L,"Damian","Kozak");
        Loan firstLoan = createLoan(null,firstBook,firstCustomer,firstLibrarian);
        LoanDto firstLoanDto = mapLoanToDto(firstLoan);
        String url = "/api/loan";
        //when
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loan start date must be empty")).when(loanService).checkErrors(any(BindingResult.class));
        //then
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstLoanDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(status().reason("Loan start date must be empty"));
    }

    @Test
    void saveLoan_ShouldThrowResponseStatusException_WhenBookIdIsNull() throws Exception{
        //given
        Book firstBook = createBook(null,"Bajka",10,"9876548293721");
        Customer firstCustomer = createCustomer(1L,"Marek","Kowal");
        Librarian firstLibrarian = createLibrarian(1L,"Damian","Kozak");
        Loan firstLoan = createLoan(null,firstBook,firstCustomer,firstLibrarian);
        LoanDto firstLoanDto = mapLoanToDto(firstLoan);
        firstLoanDto.setStartLoanDate(null);
        String url = "/api/loan";
        //when
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loan must have assigned book")).when(loanService).checkErrors(any(BindingResult.class));
        //then
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstLoanDto)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Loan must have assigned book"));
    }

    @Test
    void saveLoan_ShouldThrowResponseStatusException_WhenCustomerIdIsNull() throws Exception{
        //given
        Book firstBook = createBook(1L,"Bajka",10,"9876548293721");
        Customer firstCustomer = createCustomer(null,"Marek","Kowal");
        Librarian firstLibrarian = createLibrarian(1L,"Damian","Kozak");
        Loan firstLoan = createLoan(null,firstBook,firstCustomer,firstLibrarian);
        LoanDto firstLoanDto = mapLoanToDto(firstLoan);
        firstLoanDto.setStartLoanDate(null);
        String url = "/api/loan";
        //when
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loan must have assigned customer")).when(loanService).checkErrors(any(BindingResult.class));
        //then
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstLoanDto)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Loan must have assigned customer"));
    }

    @Test
    void saveLoan_ShouldThrowResponseStatusException_WhenLibrarianIdIsNull() throws Exception{
        //given
        Book firstBook = createBook(1L,"Bajka",10,"9876548293721");
        Customer firstCustomer = createCustomer(1L,"Marek","Kowal");
        Librarian firstLibrarian = createLibrarian(null,"Damian","Kozak");
        Loan firstLoan = createLoan(null,firstBook,firstCustomer,firstLibrarian);
        LoanDto firstLoanDto = mapLoanToDto(firstLoan);
        firstLoanDto.setStartLoanDate(null);
        String url = "/api/loan";
        //when
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loan must have assigned librarian")).when(loanService).checkErrors(any(BindingResult.class));
        //then
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstLoanDto)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Loan must have assigned librarian"));
    }

    @Test
    void getLoanById_ShouldReturnLoanWithProvidedId() throws Exception{
        //given
        Book firstBook = createBook(1L,"Bajka",10,"9876548293721");
        Customer firstCustomer = createCustomer(1L,"Marek","Kowal");
        Librarian firstLibrarian = createLibrarian(1L,"Damian","Kozak");
        Loan firstLoan = createLoan(1L,firstBook,firstCustomer,firstLibrarian);
        LoanDto firstLoanDto = mapLoanToDto(firstLoan);
        String url= "/api/loan/{id}";
        //when
        when(loanService.getLoanById(anyLong())).thenReturn(firstLoanDto);
        //then
        mockMvc.perform(get(url, anyLong()))
                .andExpect(jsonPath("$.loanId").value(1L))
                .andExpect(jsonPath("$.startLoanDate").value(mapDateToList(returnExampleDateTime())))
                .andExpect(jsonPath("$.returnLoanDate").value(firstLoanDto.getReturnLoanDate()))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.bookId").value(1L))
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.librarianId").value(1L))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void updateLoan_ShouldReturnUpdatedLoan() throws Exception {
        //given
        Customer firstCustomer = createCustomer(1L,"Marek","Kowal");
        Librarian firstLibrarian = createLibrarian(1L,"Damian","Kozak");
        Book updatedBook = createBook(1L,"Bajka",20,"9876548293721");
        Loan updatedFirstLoan = createLoan(1L,updatedBook,firstCustomer,firstLibrarian);
        LoanDto updatedFirstLoanDto = mapLoanToDto(updatedFirstLoan);
        String url = "/api/loan/{id}";
        //when
        when(loanService.updateLoan(updatedFirstLoanDto)).thenReturn(updatedFirstLoanDto);
        //then
        mockMvc.perform(put(url, 1L)
            .content(objectMapper.writeValueAsString(updatedFirstLoanDto))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.loanId").value(1L))
                .andExpect(jsonPath("$.startLoanDate").value(mapDateToList(returnExampleDateTime())))
                .andExpect(jsonPath("$.returnLoanDate").value(updatedFirstLoan.getReturnLoanDate()))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.bookId").value(1L))
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.librarianId").value(1L))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location","http://localhost/api/loan/1"));

    }

    @Test
    void updateLoan_ShouldThrowResponseStatusException_WhenLoanHasOtherIdThanPathVariable() throws Exception{
        //given
        Customer firstCustomer = createCustomer(1L,"Marek","Kowal");
        Librarian firstLibrarian = createLibrarian(1L,"Damian","Kozak");
        Book updatedBook = createBook(1L,"Bajka",20,"9876548293721");
        Loan updatedFirstLoan = createLoan(1L,updatedBook,firstCustomer,firstLibrarian);
        LoanDto updatedFirstLoanDto = mapLoanToDto(updatedFirstLoan);
        String url = "/api/loan/{id}";
        //when
        //then
        mockMvc.perform(put(url, 2L)
                .content(objectMapper.writeValueAsString(updatedFirstLoanDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Loan must have id same as path variable"));
    }

    @Test
    void updateLoan_ShouldThrowResponseStatusException_WhenBookIdIsNull() throws Exception{
        //given
        Book firstBook = createBook(null,"Bajka",10,"9876548293721");
        Customer firstCustomer = createCustomer(1L,"Marek","Kowal");
        Librarian firstLibrarian = createLibrarian(1L,"Damian","Kozak");
        Loan firstLoan = createLoan(1L,firstBook,firstCustomer,firstLibrarian);
        LoanDto firstLoanDto = mapLoanToDto(firstLoan);
        String url = "/api/loan/{id}";
        //when
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loan must have assigned book")).when(loanService).checkErrors(any(BindingResult.class));
        //then
        mockMvc.perform(put(url,1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstLoanDto)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Loan must have assigned book"));
    }

    @Test
    void updateLoan_ShouldThrowResponseStatusException_WhenCustomerIdIsNull() throws Exception{
        //given
        Book firstBook = createBook(1L,"Bajka",10,"9876548293721");
        Customer firstCustomer = createCustomer(null,"Marek","Kowal");
        Librarian firstLibrarian = createLibrarian(1L,"Damian","Kozak");
        Loan firstLoan = createLoan(1L,firstBook,firstCustomer,firstLibrarian);
        LoanDto firstLoanDto = mapLoanToDto(firstLoan);
        String url = "/api/loan/{id}";
        //when
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loan must have assigned customer")).when(loanService).checkErrors(any(BindingResult.class));
        //then
        mockMvc.perform(put(url, 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstLoanDto)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Loan must have assigned customer"));
    }

    @Test
    void updateLoan_ShouldThrowResponseStatusException_WhenLibrarianIdIsNull() throws Exception{
        //given
        Book firstBook = createBook(1L,"Bajka",10,"9876548293721");
        Customer firstCustomer = createCustomer(1L,"Marek","Kowal");
        Librarian firstLibrarian = createLibrarian(null,"Damian","Kozak");
        Loan firstLoan = createLoan(1L,firstBook,firstCustomer,firstLibrarian);
        LoanDto firstLoanDto = mapLoanToDto(firstLoan);
        String url = "/api/loan/{id}";
        //when
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loan must have assigned librarian")).when(loanService).checkErrors(any(BindingResult.class));
        //then
        mockMvc.perform(put(url, 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstLoanDto)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Loan must have assigned librarian"));
    }

    @Test
    void deleteLoan_ShouldReturnDeletedLoan() throws Exception {
        //given
        Book firstBook = createBook(1L,"Bajka",10,"9876548293721");
        Customer firstCustomer = createCustomer(1L,"Marek","Kowal");
        Librarian firstLibrarian = createLibrarian(1L,"Damian","Kozak");
        Loan firstLoan = createLoan(1L,firstBook,firstCustomer,firstLibrarian);
        LoanDto firstLoanDto = mapLoanToDto(firstLoan);
        String url = "/api/loan/{id}";
        //when
        when(loanService.deleteLoan(anyLong())).thenReturn(firstLoanDto);
        //then
        mockMvc.perform(delete(url, anyLong()))
                .andExpect(jsonPath("$.loanId").value(1L))
                .andExpect(jsonPath("$.startLoanDate").value(mapDateToList(returnExampleDateTime())))
                .andExpect(jsonPath("$.returnLoanDate").value(firstLoan.getReturnLoanDate()))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.bookId").value(1L))
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.librarianId").value(1L))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    private Loan createLoan(Long id, Book book, Customer customer, Librarian librarian) {
        Loan loan = new Loan();
        loan.setLoanId(id);
        loan.setBook(book);
        loan.setCustomer(customer);
        loan.setLibrarian(librarian);
        loan.setActive(true);
        loan.setStartLoanDate(returnExampleDateTime());
        return loan;
    }


    private Book createBook(Long id, String bookName, int quantity, String isbn) {
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

    private Librarian createLibrarian(Long id, String name, String surname) {
        Librarian librarian = new Librarian();
        librarian.setLibrarianId(id);
        librarian.setLibrarianName(name);
        librarian.setLibrarianSurname(surname);
        return librarian;
    }

    private Customer createCustomer(Long id, String name, String surname) {
        Customer customer = new Customer();
        customer.setCustomerId(id);
        customer.setCustomerName(name);
        customer.setCustomerSurname(surname);
        return customer;
    }

    private Address createAddress(Long id, String city, String postalCode) {
        Address address = new Address();
        address.setAddressId(id);
        address.setCity(city);
        address.setPostalCode(postalCode);
        return address;
    }

    private LoanDto mapLoanToDto(Loan entity) {
        LoanDto dto = new LoanDto();

        dto.setLoanId(entity.getLoanId());
        dto.setStartLoanDate(entity.getStartLoanDate());
        dto.setReturnLoanDate(entity.getReturnLoanDate());
        dto.setActive(entity.isActive());
        dto.setBookId(entity.getBook().getBookId());
        dto.setCustomerId(entity.getCustomer().getCustomerId());
        dto.setLibrarianId(entity.getLibrarian().getLibrarianId());
        return dto;
    }

    private LocalDateTime returnExampleDateTime(){
        String str = "2020-01-01 11:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(str, formatter);
    }

    private List<Integer> mapDateToList(LocalDateTime localDateTime){
        List<Integer> dataList = new ArrayList<>();
        dataList.add(localDateTime.getYear());
        dataList.add(localDateTime.getMonth().getValue());
        dataList.add(localDateTime.getDayOfMonth());
        dataList.add(localDateTime.getHour());
        dataList.add(localDateTime.getMinute());
        return dataList;
    }
}