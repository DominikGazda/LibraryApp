package pl.library.components.loan;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.library.components.book.Book;
import pl.library.components.book.BookRepository;
import pl.library.components.book.exceptions.BookNotFoundException;
import pl.library.components.customer.Customer;
import pl.library.components.customer.CustomerRepository;
import pl.library.components.customer.exceptions.CustomerNotFoundException;
import pl.library.components.librarian.Librarian;
import pl.library.components.librarian.LibrarianRepository;
import pl.library.components.librarian.LibrarianResource;
import pl.library.components.librarian.exceptions.LibrarianNotFoundException;
import pl.library.components.loan.exceptions.LoanNotFoundException;
import pl.library.components.publisher.Publisher;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class LoanMapperTest {

    @Mock
    private LibrarianRepository librarianRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private LoanMapper loanMapper;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toDto_ShouldReturnLoanDto() {
        //given
        Book book = createBook(1L,"Bajka",10,"9876548293721");
        Customer customer = createCustomer(1L,"Marek","Kowal");
        Librarian librarian = createLibrarian(1L,"Damian","Kozak");
        Loan loan = createLoan(1L,book, customer, librarian);
        LoanDto loanDto = createLoanDto(1L, 1L, 1L, 1L);
        //when
        //then
        LoanDto resultLoan = loanMapper.toDto(loan);
        resultLoan.setStartLoanDate(returnExampleDateTime());
        assertThat(resultLoan).isEqualTo(loanDto);
    }

    @Test
    void toEntity_ShouldReturnLoanEntity() {
        //given
        Book book = createBook(1L,"Bajka",10,"9876548293721");
        Customer customer = createCustomer(1L,"Marek","Kowal");
        Librarian librarian = createLibrarian(1L,"Damian","Kozak");
        Loan loan = createLoan(1L,book, customer, librarian);
        LoanDto loanDto = createLoanDto(1L, 1L, 1L, 1L);
        //when
        when(librarianRepository.findById(anyLong())).thenReturn(Optional.of(librarian));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        //then
        Loan resultLoan = loanMapper.toEntity(loanDto);
        resultLoan.setStartLoanDate(returnExampleDateTime());
        assertThat(resultLoan).isEqualTo(loan);
    }

    @Test
    void toEntity_ShouldThrowLibrarianNotFoundException(){
        //given
        LoanDto loanDto = createLoanDto(1L, 1L, 1L, 1L);
        //when
        when(librarianRepository.findById(anyLong())).thenThrow(LibrarianNotFoundException.class);
        //then
        assertThatThrownBy(() -> loanMapper.toEntity(loanDto))
                .isInstanceOf(LibrarianNotFoundException.class);
    }

    @Test
    void toEntity_ShouldThrowBookNotFoundException(){
        //given
        LoanDto loanDto = createLoanDto(1L, 1L, 1L, 1L);
        Librarian librarian = createLibrarian(1L,"Damian","Kozak");
        //when
        when(librarianRepository.findById(anyLong())).thenReturn(Optional.of(librarian));
        when(bookRepository.findById(anyLong())).thenThrow(BookNotFoundException.class);
        //then
        assertThatThrownBy(() -> loanMapper.toEntity(loanDto))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    void toEntity_ShouldThrowCustomerNotFoundException(){
        //given
        LoanDto loanDto = createLoanDto(1L, 1L, 1L, 1L);
        Librarian librarian = createLibrarian(1L,"Damian","Kozak");
        Book book = createBook(1L,"Bajka",10,"9876548293721");
        //when
        when(librarianRepository.findById(anyLong())).thenReturn(Optional.of(librarian));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(customerRepository.findById(anyLong())).thenThrow(CustomerNotFoundException.class);
        //then
        assertThatThrownBy(() -> loanMapper.toEntity(loanDto))
                .isInstanceOf(CustomerNotFoundException.class);
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

    private LoanDto createLoanDto(Long loanId, Long librarianId, Long bookId, Long customerId){
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanId(loanId);
        loanDto.setStartLoanDate(returnExampleDateTime());
        loanDto.setReturnLoanDate(null);
        loanDto.setActive(true);
        loanDto.setLibrarianId(librarianId);
        loanDto.setBookId(bookId);
        loanDto.setCustomerId(customerId);
        return loanDto;
    }

    private LocalDateTime returnExampleDateTime(){
        String str = "2020-01-01 11:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(str, formatter);
    }
}