package pl.library.components.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.address.Address;
import pl.library.components.author.Author;
import pl.library.components.book.Book;
import pl.library.components.customer.exceptions.CustomerNotFoundException;
import pl.library.components.librarian.Librarian;
import pl.library.components.loan.Loan;
import pl.library.components.loan.LoanDto;
import pl.library.components.loan.LoanMapper;
import pl.library.components.publisher.Publisher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private LoanMapper loanMapper;
    @InjectMocks
    private CustomerService customerService;
    private MockedStatic<CustomerMapper> customerMapper;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        customerMapper = Mockito.mockStatic(CustomerMapper.class);
    }

    @AfterEach
    void end(){
        customerMapper.close();
    }

    @Test
    void getCustomers_ShouldReturnCustomerList() {
        //given
        CustomerDto firstCustomer = createCustomerDto(1L,"Damian","Kowal");
        CustomerDto secondCustomer = createCustomerDto(2L,"Karol","Kot");
        Address address = createAddress(1L, "Krakow","32-800");
        firstCustomer.setAddress(address);
        secondCustomer.setAddress(address);
        List<CustomerDto> customerDtoList = List.of(firstCustomer, secondCustomer);
        List<Customer> customerList = customerDtoList
                .stream()
                .map(this::mapCustomerDtoToCustomer)
                .collect(Collectors.toList());
        //when
        when(customerRepository.findAll()).thenReturn(customerList);
        customerMapper.when(() -> CustomerMapper.toDto(any(Customer.class))).thenReturn(firstCustomer,secondCustomer);
        //then
        assertThat(customerService.getCustomers()).isEqualTo(customerDtoList);
    }

    @Test
    void saveCustomer_ShouldReturnSavedCustomer(){
        //given
        CustomerDto customerDto = createCustomerDto(null,"Marek","Kowal");
        CustomerDto savedCustomerDto = createCustomerDto(1L,"Marek","Kowal");
        Customer customer = mapCustomerDtoToCustomer(customerDto);
        Customer savedCustomer = mapCustomerDtoToCustomer(savedCustomerDto);
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        //when
        customerMapper.when(() -> CustomerMapper.toEntity(customerDto)).thenReturn(customer);
        when(customerRepository.save(argumentCaptor.capture())).thenReturn(savedCustomer);
        customerMapper.when(() -> CustomerMapper.toDto(savedCustomer)).thenReturn(savedCustomerDto);
        //then
        assertAll(
                () ->assertThat(customerService.saveCustomer(customerDto)).isEqualTo(savedCustomerDto),
                () -> assertThat(argumentCaptor.getValue()).isEqualTo(customer)
        );

    }

    @Test
    void saveCustomer_ShouldReturnResponseEntityException_WhenCustomerHasId(){
        //given
        CustomerDto customerDto = createCustomerDto(1L,"Marek","Kowal");
        //when
        //then
        assertThatThrownBy(() -> customerService.saveCustomer(customerDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("400 BAD_REQUEST \"Customer cannot have id\"");
    }

    @Test
    void getCustomerById_ShouldReturnCustomer_WithProvidedId(){
        //given
        CustomerDto customerDto = createCustomerDto(1L,"Marek","Kowal");
        Customer customer = mapCustomerDtoToCustomer(customerDto);
        //when
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        customerMapper.when(() -> CustomerMapper.toDto(customer)).thenReturn(customerDto);
        //then
        assertThat(customerService.getCustomerById(anyLong())).isEqualTo(customerDto);
    }

    @Test
    void getCustomerById_ShouldReturnCustomerNotFoundException_WhenCustomerNotExists(){
        //given
        CustomerDto customerDto = createCustomerDto(1L,"Marek","Kowal");
        //when
        when(customerRepository.findById(anyLong())).thenThrow(new CustomerNotFoundException());
        //then
        assertThatThrownBy(() -> customerService.getCustomerById(anyLong()))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void updateCustomer_ShouldReturnUpdatedCustomer(){
        //given
        CustomerDto customerDto = createCustomerDto(1L,"Marek","Kowal");
        CustomerDto updatedCustomerDto = createCustomerDto(1L,"Bogdan","Kowal");
        Customer customer = mapCustomerDtoToCustomer(customerDto);
        Customer updatedCustomer = mapCustomerDtoToCustomer(updatedCustomerDto);
        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        //when
        customerMapper.when(() -> CustomerMapper.toEntity(customerDto)).thenReturn(customer);
        when(customerRepository.save(captor.capture())).thenReturn(updatedCustomer);
        customerMapper.when(() -> CustomerMapper.toDto(updatedCustomer)).thenReturn(updatedCustomerDto);
        //then
        assertThat(customerService.updateCustomer(customerDto)).isEqualTo(updatedCustomerDto);
        assertThat(captor.getValue()).isEqualTo(customer);
    }

    @Test
    void deleteCustomer_ShouldReturnDeletedCustomer(){
        //given
        CustomerDto customerDto = createCustomerDto(1L,"Marek","Kowal");
        Customer customer = mapCustomerDtoToCustomer(customerDto);
        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        //when
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        customerMapper.when(() -> CustomerMapper.toDto(customer)).thenReturn(customerDto);
        //then
        assertThat(customerService.deleteCustomer(anyLong())).isEqualTo(customerDto);
        verify(customerRepository).delete(captor.capture());
        assertThat(captor.getValue()).isEqualTo(customer);
    }

    @Test
    void deleteCustomer_ShouldThrowCustomerNotFoundException_WhenCustomerNotExists(){
        //given
        CustomerDto customerDto = createCustomerDto(1L,"Marek","Kowal");
        //when
        when(customerRepository.findById(anyLong())).thenThrow(new CustomerNotFoundException());
        //then
        assertThatThrownBy(() -> customerService.deleteCustomer(anyLong()))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void getAddressFromCustomerById_ShouldReturnCustomerAddress(){
        //given
        CustomerDto customerDto = createCustomerDto(1L,"Marek","Kowal");
        Customer customer = mapCustomerDtoToCustomer(customerDto);
        Address address = createAddress(1L,"Krakow","33-100");
        customer.setAddress(address);
        //when
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        //then
        assertThat(customerService.getAddressFromCustomerById(anyLong())).isEqualTo(address);
    }

    @Test
    void getAddressFromCustomerById_ShouldThrowCustomerNotFoundException_WhenCustomerNotExists(){
        //given
        //when
        when(customerRepository.findById(anyLong())).thenThrow(new CustomerNotFoundException());
        //then
        assertThatThrownBy(() -> customerService.getAddressFromCustomerById(anyLong()))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void getAllLoansFromCustomerById_ShouldReturnLoanListAssignedToCustomerWithProvidedId(){
        //given
        CustomerDto customerDto = createCustomerDto(1L,"Marek","Kowal");
        Customer customer = mapCustomerDtoToCustomer(customerDto);
        LoanDto firstLoanDto = createLoanDto(1L,1L,1L,1L);
        LoanDto secondLoanDto = createLoanDto(2L,1L,2L,1L);

        Loan firstLoan = mapToLoan(firstLoanDto);
        Loan secondLoan = mapToLoan(secondLoanDto);
        List<LoanDto> loanDtoList = List.of(firstLoanDto, secondLoanDto);
        customer.getLoanList().add(firstLoan);
        customer.getLoanList().add(secondLoan);
        //when
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(loanMapper.toDto(any(Loan.class))).thenReturn(firstLoanDto, secondLoanDto);
        //then
        assertThat(customerService.getAllLoansFromCustomerById(anyLong())).isEqualTo(loanDtoList);
    }

    private CustomerDto createCustomerDto(Long id, String name, String surname){
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(id);
        customerDto.setCustomerName(name);
        customerDto.setCustomerSurname(surname);
        return customerDto;
    }

    private Address createAddress(Long id, String city, String postalCode){
        Address address = new Address();
        address.setAddressId(id);
        address.setCity(city);
        address.setPostalCode(postalCode);
        return address;
    }

    private Customer mapCustomerDtoToCustomer(CustomerDto customerDto){
        Customer customer = new Customer();
        customer.setCustomerId(customerDto.getCustomerId());
        customer.setCustomerName(customerDto.getCustomerName());
        customer.setCustomerSurname(customerDto.getCustomerSurname());
        customer.setLoanList(new ArrayList<>());
        return customer;
    }

    private LoanDto createLoanDto(Long loanId, Long customerId, Long bookId, Long librarianId){
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanId(loanId);
        loanDto.setCustomerId(customerId);
        loanDto.setBookId(bookId);
        loanDto.setActive(true);
        loanDto.setStartLoanDate(LocalDateTime.now());
        loanDto.setLibrarianId(librarianId);
        return loanDto;
    }

    private Loan mapToLoan(LoanDto dto){
        Loan loan = new Loan();
        Book book = createBook(1L,"Bajka",10,"9876543456722");
        Librarian librarian = createLibrarian(1L,"Marek","Kowal");

        loan.setLoanId(dto.getLoanId());
        loan.setBook(book);
        loan.setLibrarian(librarian);
        return loan;
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

    private Librarian createLibrarian(Long id, String name, String surname){
        Librarian librarian = new Librarian();
        librarian.setLibrarianId(id);
        librarian.setLibrarianName(name);
        librarian.setLibrarianSurname(surname);
        return librarian;
    }


}