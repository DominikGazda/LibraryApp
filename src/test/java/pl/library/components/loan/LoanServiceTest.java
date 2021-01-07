package pl.library.components.loan;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.address.Address;
import pl.library.components.book.Book;
import pl.library.components.book.BookRepository;
import pl.library.components.customer.Customer;
import pl.library.components.customer.CustomerDto;
import pl.library.components.librarian.Librarian;
import pl.library.components.librarian.LibrarianDto;
import pl.library.components.loan.exceptions.LoanNotFoundException;
import pl.library.components.publisher.Publisher;

import java.time.LocalDateTime;
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

class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;
    @Mock
    private LoanMapper loanMapper;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLoans_ShouldReturnLoansList() {
        //given
        Book firstBook = createBook(1L,"Bajka",10,"9876548293721");
        Customer firstCustomer = createCustomer(1L,"Marek","Kowal");
        Librarian firstLibrarian = createLibrarian(1L,"Damian","Kozak");
        Loan firstLoan = createLoan(1L,firstBook,firstCustomer,firstLibrarian);

        Book secondBook = createBook(2L,"Bajka",10,"9876548293700");
        Customer secondCustomer = createCustomer(2L,"Marek","Kawka");
        Librarian secondLibrarian = createLibrarian(1L,"Damian","Kozak");
        Loan secondLoan = createLoan(2L, secondBook, secondCustomer, secondLibrarian);

        List<Loan> loanList = List.of(firstLoan, secondLoan);

        LoanDto firstLoanDto = mapLoanToDto(firstLoan);
        LoanDto secondLoanDto = mapLoanToDto(secondLoan);

        List<LoanDto> loanDtoList = List.of(firstLoanDto, secondLoanDto);
        //when
        when(loanRepository.findAll()).thenReturn(loanList);
        when(loanMapper.toDto(any(Loan.class))).thenReturn(firstLoanDto, secondLoanDto);
        //then
        assertThat(loanService.getLoans()).isEqualTo(loanDtoList);
    }

    @Test
    void saveLoan_ShouldReturnSavedLoan(){
        //given
        Book book = createBook(1L,"Bajka",10,"9876548293721");
        Customer customer = createCustomer(1L,"Marek","Kowal");
        Librarian librarian = createLibrarian(1L,"Damian","Kozak");
        Loan loan = createLoan(null, book, customer, librarian);
        LoanDto loanDto = mapLoanToDto(loan);
        Loan savedLoan = createLoan(1L, book, customer, librarian);
        LoanDto savedLoanDto = mapLoanToDto(savedLoan);
        //when
        when(loanMapper.toEntity(loanDto)).thenReturn(loan);
        when(loanRepository.save(loan)).thenReturn(savedLoan);
        when(loanMapper.toDto(savedLoan)).thenReturn(savedLoanDto);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(savedLoan.getBook()));
        when(bookRepository.save(savedLoan.getBook())).thenReturn(savedLoan.getBook());
        //then
        assertThat(loanService.saveLoan(loanDto)).isEqualTo(savedLoanDto);
    }

    @Test
    void saveLoan_ShouldThrowResponseStatusException_WhenLoanHasId(){
        //given
        Book book = createBook(1L,"Bajka",10,"9876548293721");
        Customer customer = createCustomer(1L,"Marek","Kowal");
        Librarian librarian = createLibrarian(1L,"Damian","Kozak");
        Loan loan = createLoan(1L, book, customer, librarian);
        LoanDto loanDto = mapLoanToDto(loan);
        //when
        //then
        assertThatThrownBy(() -> loanService.saveLoan(loanDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("400 BAD_REQUEST \"Loan cannot have id\"");
    }

    @Test
    void saveLoan_ShouldThrowResponseStatusException_WhenLoanBookHasNotId(){
        //given
        Book book = createBook(null,"Bajka",10,"9876548293721");
        Customer customer = createCustomer(1L,"Marek","Kowal");
        Librarian librarian = createLibrarian(1L,"Damian","Kozak");
        Loan loan = createLoan(null, book, customer, librarian);
        LoanDto loanDto = mapLoanToDto(loan);
        //when
        //then
        assertThatThrownBy(() -> loanService.saveLoan(loanDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("400 BAD_REQUEST \"Loan doesn't have assigned book\"");
    }

    @Test
    void getLoanById_ShouldReturnLoanWithProvidedId(){
        //given
        Book book = createBook(null,"Bajka",10,"9876548293721");
        Customer customer = createCustomer(1L,"Marek","Kowal");
        Librarian librarian = createLibrarian(1L,"Damian","Kozak");
        Loan loan = createLoan(null, book, customer, librarian);
        LoanDto loanDto = mapLoanToDto(loan);
        //when
        when(loanRepository.findById(anyLong())).thenReturn(Optional.of(loan));
        when(loanMapper.toDto(any(Loan.class))).thenReturn(loanDto);
        //then
        assertThat(loanService.getLoanById(anyLong())).isEqualTo(loanDto);
    }

    @Test
    void getLoanById_ShouldThrowLoanNotFoundException_WhenLoanNotExists(){
        //given
        //when
        when(loanRepository.findById(anyLong())).thenThrow(LoanNotFoundException.class);
        //then
        assertThatThrownBy(() -> loanService.getLoanById(anyLong()))
                .isInstanceOf(LoanNotFoundException.class);
    }

    @Test
    void updateLoan_ShouldReturnUpdatedLoan(){
        //given
        Book book = createBook(1L,"Bajka",10,"9876548293721");
        Customer customer = createCustomer(1L,"Marek","Kowal");
        Librarian librarian = createLibrarian(1L,"Damian","Kozak");
        Loan loan = createLoan(1L, book, customer, librarian);
        LoanDto loanDto = mapLoanToDto(loan);

        Book book2 = createBook(1L,"Majka",10,"9876548293721");
        Loan updatedLoan = createLoan(1L, book2, customer, librarian);
        LoanDto updatedLoanDto = mapLoanToDto(updatedLoan);
        //when
        when(loanMapper.toEntity(loanDto)).thenReturn(loan);
        when(loanRepository.save(loan)).thenReturn(updatedLoan);
        when(loanMapper.toDto(updatedLoan)).thenReturn(updatedLoanDto);
        //then
        assertThat(loanService.updateLoan(loanDto)).isEqualTo(updatedLoanDto);
    }

    @Test
    void updateLoan_ShouldThrowResponseStatusException_WhenLoanHasNotAssignedBooks(){
        //given
        Book book = createBook(null,"Bajka",10,"9876548293721");
        Customer customer = createCustomer(1L,"Marek","Kowal");
        Librarian librarian = createLibrarian(1L,"Damian","Kozak");
        Loan loan = createLoan(1L, book, customer, librarian);
        LoanDto loanDto = mapLoanToDto(loan);
        //when
        //then
        assertThatThrownBy(() -> loanService.updateLoan(loanDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("400 BAD_REQUEST \"Loan doesn't have assigned book\"");
    }

    @Test
    void deleteLoan_ShouldReturnDeletedLoan(){
        //given
        Book book = createBook(null,"Bajka",10,"9876548293721");
        Customer customer = createCustomer(1L,"Marek","Kowal");
        Librarian librarian = createLibrarian(1L,"Damian","Kozak");
        Loan loan = createLoan(1L, book, customer, librarian);
        LoanDto loanDto = mapLoanToDto(loan);
        ArgumentCaptor<Loan> captor = ArgumentCaptor.forClass(Loan.class);
        //when
        when(loanRepository.findById(anyLong())).thenReturn(Optional.of(loan));
        when(loanMapper.toDto(loan)).thenReturn(loanDto);
        //then
        LoanDto deletedLoan = loanService.deleteLoan(anyLong());
        verify(loanRepository).delete(captor.capture());

        assertThat(deletedLoan).isEqualTo(loanService.deleteLoan(anyLong()));
        assertThat(captor.getValue()).isEqualTo(loan);
    }

    @Test
    void deleteLoan_ShouldThrowLoanNotFoundException(){
        //given
        //when
        when(loanRepository.findById(anyLong())).thenThrow(new LoanNotFoundException());
        //then
        assertThatThrownBy(() -> loanService.deleteLoan(anyLong()))
                .isInstanceOf(LoanNotFoundException.class);
    }

    private Loan createLoan(Long id, Book book, Customer customer, Librarian librarian) {
        Loan loan = new Loan();
        loan.setLoanId(id);
        loan.setBook(book);
        loan.setCustomer(customer);
        loan.setLibrarian(librarian);
        loan.setActive(true);
        loan.setStartLoanDate(LocalDateTime.now());
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
}