package pl.library;

import org.junit.jupiter.api.*;
import pl.library.components.address.Address;
import pl.library.components.author.Author;
import pl.library.components.author.AuthorDto;
import pl.library.components.book.Book;
import pl.library.components.book.BookDto;
import pl.library.components.customer.Customer;
import pl.library.components.customer.CustomerDto;
import pl.library.components.librarian.Librarian;
import pl.library.components.librarian.LibrarianDto;
import pl.library.components.loan.Loan;
import pl.library.components.loan.LoanDto;
import pl.library.components.publisher.Publisher;
import pl.library.components.publisher.PublisherDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationTests {

    private ValidatorFactory validatorFactory;
    private Validator validator;

    @BeforeEach
    void init(){
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterEach
    void stop(){
        validatorFactory.close();
    }

    @Test
    void Address_ShouldReturnViolation_WhenCityIsEmpty(){
        //given
        Address address = createAddress(1L,null,"33-100");
        String emptyCityViolation = "[ConstraintViolationImpl{interpolatedMessage='City field cannot be empty', propertyPath=city, rootBeanClass=class pl.library.components.address.Address, messageTemplate='{pl.library.components.address.Address.city.NotEmpty}'}]";
        //when
        //then
        Set<ConstraintViolation<Address>> violations;
        violations = validator.validate(address);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(emptyCityViolation)
        );
    }

    @Test
    void Address_ShouldReturnViolation_WhenPostalCodeWithBadPattern(){
        //given
        Address address = createAddress(1L,"Krakow","33-1000");
        String badPatternViolation = "[ConstraintViolationImpl{interpolatedMessage='Postal Code must match the expression 00-000', propertyPath=postalCode, rootBeanClass=class pl.library.components.address.Address, messageTemplate='{pl.library.components.address.Address.postalCode.Pattern}'}]";
        //when
        //then
        Set<ConstraintViolation<Address>> violations = validator.validate(address);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(badPatternViolation)
        );
    }

    @Test
    void Author_ShouldReturnViolation_WhenAuthorNameIsEmpty(){
        //given
        Author author = createAuthor(1L,"","Bibliotekarz");
        String authorNameViolation = "[ConstraintViolationImpl{interpolatedMessage='Author name cannot be empty', propertyPath=authorName, rootBeanClass=class pl.library.components.author.Author, messageTemplate='{pl.library.components.author.Author.authorName.NotEmpty}'}]";
        //when
        //then
        Set<ConstraintViolation<Author>> violations = validator.validate(author);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(authorNameViolation)
        );
    }

    @Test
    void Author_ShouldReturnViolation_WhenAuthorSurnameIsEmpty(){
        //given
        Author author = createAuthor(1L,"Adam","");
        String authorSurnameViolation = "[ConstraintViolationImpl{interpolatedMessage='Author surname cannot be empty', propertyPath=authorSurname, rootBeanClass=class pl.library.components.author.Author, messageTemplate='{pl.library.components.author.Author.authorSurname.NotEmpty}'}]";
        //when
        //then
        Set<ConstraintViolation<Author>> violations = validator.validate(author);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(authorSurnameViolation)
        );
    }

    @Test
    void AuthorDto_ShouldReturnViolation_WhenAuthorNameIsEmpty(){
        //given
        AuthorDto authorDto = createAuthorDto(1L,"","Bibliotekarz");
        String authorNameViolation = "[ConstraintViolationImpl{interpolatedMessage='Author name cannot be empty', propertyPath=authorName, rootBeanClass=class pl.library.components.author.AuthorDto, messageTemplate='{pl.library.components.author.Author.authorName.NotEmpty}'}]";
        //when
        //then
        Set<ConstraintViolation<AuthorDto>> violations = validator.validate(authorDto);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(authorNameViolation)
        );
    }

    @Test
    void AuthorDto_ShouldReturnViolation_WhenAuthorSurnameIsEmpty(){
        //given
        AuthorDto authorDto = createAuthorDto(1L,"Adam","");
        String authorSurnameViolation = "[ConstraintViolationImpl{interpolatedMessage='Author surname cannot be empty', propertyPath=authorSurname, rootBeanClass=class pl.library.components.author.AuthorDto, messageTemplate='{pl.library.components.author.Author.authorSurname.NotEmpty}'}]";
        //when
        //then
        Set<ConstraintViolation<AuthorDto>> violations = validator.validate(authorDto);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(authorSurnameViolation)
        );
    }

    @Test
    void Book_ShouldReturnViolation_WhenBookNameIsBlank(){
        //given
        Book book = createBook(1L," ","9876789876543",20);
        String bookNameViolation = "[ConstraintViolationImpl{interpolatedMessage='Book name cannot be empty', propertyPath=bookName, rootBeanClass=class pl.library.components.book.Book, messageTemplate='{pl.library.components.book.Book.bookName.NotBlank}'}]";
        //when
        //then
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(bookNameViolation)
        );
    }

    @Test
    void Book_ShouldReturnViolation_WhenIsbnBadPattern(){
        //given
        Book book = createBook(1L,"Mafia","98767898765432",20);
        String bookPatternViolation = "[ConstraintViolationImpl{interpolatedMessage='Isbn number has to have 13 digits', propertyPath=isbn, rootBeanClass=class pl.library.components.book.Book, messageTemplate='{pl.library.components.book.Book.isbn.Pattern}'}]";
        //when
        //then
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(bookPatternViolation)
        );
    }

    @Test
    void BookDto_ShouldReturnViolation_WhenBookNameIsBlank(){
        //given
        BookDto bookDto = createBookDto(1L," ","9876789876543",20,"WSIP");
        String bookNameViolation = "[ConstraintViolationImpl{interpolatedMessage='Book name cannot be empty', propertyPath=bookName, rootBeanClass=class pl.library.components.book.BookDto, messageTemplate='{pl.library.components.book.Book.bookName.NotBlank}'}]";
        //when
        //then
        Set<ConstraintViolation<BookDto>> violations = validator.validate(bookDto);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(bookNameViolation)
        );
    }

    @Test
    void BookDto_ShouldReturnViolation_WhenIsbnBadPattern(){
        //given
        BookDto bookDto = createBookDto(1L,"Mafia","987678987654322",20,"WSIP");
        String bookPatternViolation = "[ConstraintViolationImpl{interpolatedMessage='Isbn number has to have 13 digits', propertyPath=isbn, rootBeanClass=class pl.library.components.book.BookDto, messageTemplate='{pl.library.components.book.Book.isbn.Pattern}'}]";
        //when
        //then
        Set<ConstraintViolation<BookDto>> violations = validator.validate(bookDto);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(bookPatternViolation)
        );
    }

    @Test
    void BookDto_ShouldReturnViolation_WhenPublisherIsBlank(){
        //given
        BookDto bookDto = createBookDto(1L,"Mafia","7678987654322",20,"");
        String bookPatternViolation = "[ConstraintViolationImpl{interpolatedMessage='Publisher field cannot be empty', propertyPath=publisher, rootBeanClass=class pl.library.components.book.BookDto, messageTemplate='{pl.library.components.book.BookDto.publisher.NotBlank}'}]";
        //when
        //then
        Set<ConstraintViolation<BookDto>> violations = validator.validate(bookDto);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(bookPatternViolation)
        );
    }

    @Test
    void Customer_ShouldReturnViolation_WhenCustomerNameIsBlank(){
        //given
        Customer customer = createCustomer(1L, "", "Marek");
        String customerNameViolation = "[ConstraintViolationImpl{interpolatedMessage='Customer name cannot be empty', propertyPath=customerName, rootBeanClass=class pl.library.components.customer.Customer, messageTemplate='{pl.library.components.customer.Customer.customerName.NotBlank}'}]";
        //when
        //then
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(customerNameViolation)
        );
    }

    @Test
    void Customer_ShouldReturnViolation_WhenCustomerSurnameIsBlank(){
        //given
        Customer customer = createCustomer(1L, "Damian", "");
        String customerSurnameViolation = "[ConstraintViolationImpl{interpolatedMessage='Customer surname cannot be empty', propertyPath=customerSurname, rootBeanClass=class pl.library.components.customer.Customer, messageTemplate='{pl.library.components.customer.Customer.customerSurname.NotBlank}'}]";
        //when
        //then
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(customerSurnameViolation)
        );
    }

    @Test
    void CustomerDto_ShouldReturnViolation_WhenCustomerNameIsBlank(){
        //given
        CustomerDto customerDto = createCustomerDto(1L, "", "Marek");
        String customerDtoNameViolation = "[ConstraintViolationImpl{interpolatedMessage='Customer name cannot be empty', propertyPath=customerName, rootBeanClass=class pl.library.components.customer.CustomerDto, messageTemplate='{pl.library.components.customer.Customer.customerName.NotBlank}'}]";
        //when
        //then
        Set<ConstraintViolation<CustomerDto>> violations = validator.validate(customerDto);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(customerDtoNameViolation)
        );
    }

    @Test
    void CustomerDto_ShouldReturnViolation_WhenCustomerSurnameIsBlank(){
        //given
        CustomerDto customerDto = createCustomerDto(1L, "Damian", "");
        String customerDtoSurnameViolation = "[ConstraintViolationImpl{interpolatedMessage='Customer surname cannot be empty', propertyPath=customerSurname, rootBeanClass=class pl.library.components.customer.CustomerDto, messageTemplate='{pl.library.components.customer.Customer.customerSurname.NotBlank}'}]";
        //when
        //then
        Set<ConstraintViolation<CustomerDto>> violations = validator.validate(customerDto);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(customerDtoSurnameViolation)
        );
    }

    @Test
    void Librarian_ShouldReturnViolation_WhenLibrarianNameIsEmpty(){
        //given
        Librarian librarian = createLibrarian(1L, "", "Kozak");
        String librarianNameViolation = "[ConstraintViolationImpl{interpolatedMessage='Librarian name cannot be empty', propertyPath=librarianName, rootBeanClass=class pl.library.components.librarian.Librarian, messageTemplate='{pl.library.components.librarian.Librarian.librarianName.NotEmpty}'}]";
        //when
        //then
        Set<ConstraintViolation<Librarian>> violations = validator.validate(librarian);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(librarianNameViolation)
        );
    }

    @Test
    void Librarian_ShouldReturnViolation_WhenLibrarianSurnameIsEmpty(){
        //given
        Librarian librarian = createLibrarian(1L, "Bartosz", "");
        String librarianSurnameViolation = "[ConstraintViolationImpl{interpolatedMessage='Librarian surname cannot be empty', propertyPath=librarianSurname, rootBeanClass=class pl.library.components.librarian.Librarian, messageTemplate='{pl.library.components.librarian.Librarian.librarianSurname.NotEmpty}'}]";
        //when
        //then
        Set<ConstraintViolation<Librarian>> violations = validator.validate(librarian);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(librarianSurnameViolation)
        );
    }

    @Test
    void LibrarianDto_ShouldReturnViolation_WhenLibrarianNameIsEmpty(){
        //given
        LibrarianDto librarianDto = createLibrarianDto(1L, "", "Kozak");
        String librarianNameViolation = "[ConstraintViolationImpl{interpolatedMessage='Librarian name cannot be empty', propertyPath=librarianName, rootBeanClass=class pl.library.components.librarian.LibrarianDto, messageTemplate='{pl.library.components.librarian.Librarian.librarianName.NotEmpty}'}]";
        //when
        //then
        Set<ConstraintViolation<LibrarianDto>> violations = validator.validate(librarianDto);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(librarianNameViolation)
        );
    }

    @Test
    void LibrarianDto_ShouldReturnViolation_WhenLibrarianSurnameIsEmpty(){
        //given
        LibrarianDto librarianDto = createLibrarianDto(1L, "Bartosz", "");
        String librarianSurnameViolation = "[ConstraintViolationImpl{interpolatedMessage='Librarian surname cannot be empty', propertyPath=librarianSurname, rootBeanClass=class pl.library.components.librarian.LibrarianDto, messageTemplate='{pl.library.components.librarian.Librarian.librarianSurname.NotEmpty}'}]";
        //when
        //then
        Set<ConstraintViolation<LibrarianDto>> violations = validator.validate(librarianDto);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(librarianSurnameViolation)
        );
    }

    @Test
    void Loan_ShouldReturnViolation_WhenStartLoanDateIsNull(){
        //given
        Customer customer = createCustomer(1L,"Marek","Pawlak");
        Book book = createBook(1L, "Mafia","9876567890123",20);
        Librarian librarian = createLibrarian(1L, "Damian","Kozak");
        Loan loan = createLoan(1L, book, customer, librarian);
        loan.setStartLoanDate(null);
        String loanStartDateViolation = "[ConstraintViolationImpl{interpolatedMessage='{pl.library.components.loan.Loan.startLoanDate.NotEmpty}', propertyPath=startLoanDate, rootBeanClass=class pl.library.components.loan.Loan, messageTemplate='{pl.library.components.loan.Loan.startLoanDate.NotEmpty}'}]";
        //when
        //then
        Set<ConstraintViolation<Loan>> violations = validator.validate(loan);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(loanStartDateViolation)
        );
    }

    @Test
    void Loan_ShouldReturnViolation_WhenBookIsNull(){
        //given
        Customer customer = createCustomer(1L,"Marek","Pawlak");
        Librarian librarian = createLibrarian(1L, "Damian","Kozak");
        Loan loan = createLoan(1L, null, customer, librarian);
        String loanBookViolation = "[ConstraintViolationImpl{interpolatedMessage='{pl.library.components.loan.Loan.book.NotEmpty}', propertyPath=book, rootBeanClass=class pl.library.components.loan.Loan, messageTemplate='{pl.library.components.loan.Loan.book.NotEmpty}'}]";
        //when
        //then
        Set<ConstraintViolation<Loan>> violations = validator.validate(loan);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(loanBookViolation)
        );
    }

    @Test
    void Loan_ShouldReturnViolation_WhenCustomerIsNull(){
        //given
        Book book = createBook(1L, "Mafia","9876567890123",20);
        Librarian librarian = createLibrarian(1L, "Damian","Kozak");
        Loan loan = createLoan(1L, book, null, librarian);
        String loanCustomerViolation = "[ConstraintViolationImpl{interpolatedMessage='Loan must have assigned customer', propertyPath=customer, rootBeanClass=class pl.library.components.loan.Loan, messageTemplate='{pl.library.components.loan.Loan.customer.NotNull}'}]";
        //when
        //then
        Set<ConstraintViolation<Loan>> violations = validator.validate(loan);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(loanCustomerViolation)
        );
    }

    @Test
    void Loan_ShouldReturnViolation_WhenLibrarianIsNull(){
        //given
        Customer customer = createCustomer(1L,"Marek","Pawlak");
        Book book = createBook(1L, "Mafia","9876567890123",20);
        Loan loan = createLoan(1L, book, customer, null);
        String loanLibrarianViolation = "[ConstraintViolationImpl{interpolatedMessage='{pl.library.components.loan.Loan.librarian.NotNull}', propertyPath=librarian, rootBeanClass=class pl.library.components.loan.Loan, messageTemplate='{pl.library.components.loan.Loan.librarian.NotNull}'}]";
        //when
        //then
        Set<ConstraintViolation<Loan>> violations = validator.validate(loan);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(loanLibrarianViolation)
        );
    }

    @Test
    void LoanDto_ShouldReturnViolation_WhenStartLoanDateIsNotNull(){
        //given
        LoanDto loanDto = createLoanDto(1L, 1L, 1L, 1L);
        String loanDtoStartDateViolation = "[ConstraintViolationImpl{interpolatedMessage='Loan start date must be empty', propertyPath=startLoanDate, rootBeanClass=class pl.library.components.loan.LoanDto, messageTemplate='{pl.library.components.loan.Loan.startLoanDate.Null}'}]";
        //when
        //then
        Set<ConstraintViolation<LoanDto>> violations = validator.validate(loanDto);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(loanDtoStartDateViolation)
        );
    }

    @Test
    void LoanDto_ShouldReturnViolation_WhenBookIsNull(){
        //given
        LoanDto loanDto = createLoanDto(1L, null, 1L, 1L);
        String loanDtoBookViolation = "[ConstraintViolationImpl{interpolatedMessage='Loan must have assigned book', propertyPath=bookId, rootBeanClass=class pl.library.components.loan.LoanDto, messageTemplate='{pl.library.components.loan.Loan.book.NotNull}'}]";
        loanDto.setStartLoanDate(null);
        //when
        //then
        Set<ConstraintViolation<LoanDto>> violations = validator.validate(loanDto);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(loanDtoBookViolation)
        );
    }

    @Test
    void LoanDto_ShouldReturnViolation_WhenCustomerIsNull(){
        //given
        LoanDto loanDto = createLoanDto(1L, null, 1L, 1L);
        loanDto.setStartLoanDate(null);
        String loanDtoCustomerViolation = "[ConstraintViolationImpl{interpolatedMessage='Loan must have assigned book', propertyPath=bookId, rootBeanClass=class pl.library.components.loan.LoanDto, messageTemplate='{pl.library.components.loan.Loan.book.NotNull}'}]";
        //when
        //then
        Set<ConstraintViolation<LoanDto>> violations = validator.validate(loanDto);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(loanDtoCustomerViolation)
        );
    }

    @Test
    void LoanDto_ShouldReturnViolation_WhenLibrarianIsNull(){
        //given
        LoanDto loanDto = createLoanDto(1L, null, 1L, 1L);
        loanDto.setStartLoanDate(null);
        String loanDtoLibrarianViolation = "[ConstraintViolationImpl{interpolatedMessage='Loan must have assigned book', propertyPath=bookId, rootBeanClass=class pl.library.components.loan.LoanDto, messageTemplate='{pl.library.components.loan.Loan.book.NotNull}'}]";
        //when
        //then
        Set<ConstraintViolation<LoanDto>> violations = validator.validate(loanDto);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(loanDtoLibrarianViolation)
        );
    }

    @Test
    void Publisher_ShouldReturnViolation_WhenPublisherNameIsEmpty(){
        //given
        Publisher publisher = createPublisher(1L, "");
        String publisherNameViolation = "[ConstraintViolationImpl{interpolatedMessage='Publisher name cannot be empty', propertyPath=publisherName, rootBeanClass=class pl.library.components.publisher.Publisher, messageTemplate='{pl.library.components.publisher.Publisher.publisherName.NotEmpty}'}]";
        //when
        //then
        Set<ConstraintViolation<Publisher>> violations = validator.validate(publisher);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(publisherNameViolation)
        );
    }

    @Test
    void PublisherDto_ShouldReturnViolation_WhenPublisherNameIsEmpty(){
        //given
        PublisherDto publisherDto = createPublisherDto(1L, "");
        String publisherDtoNameViolation = "[ConstraintViolationImpl{interpolatedMessage='Publisher name cannot be empty', propertyPath=publisherName, rootBeanClass=class pl.library.components.publisher.PublisherDto, messageTemplate='{pl.library.components.publisher.Publisher.publisherName.NotEmpty}'}]";
        //when
        //then
        Set<ConstraintViolation<PublisherDto>> violations = validator.validate(publisherDto);
        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertThat(violations.toString()).contains(publisherDtoNameViolation)
        );
    }

    private Address createAddress(Long id, String city, String postalCode){
        Address address = new Address();
        address.setAddressId(id);
        address.setCity(city);
        address.setPostalCode(postalCode);
        return address;
    }

    private Author createAuthor(Long id, String name, String surname){
        Author author = new Author();
        author.setAuthorId(id);
        author.setAuthorName(name);
        author.setAuthorSurname(surname);
        author.setBookList(new ArrayList<>());
        return author;
    }

    private AuthorDto createAuthorDto(Long id, String name, String surname){
        AuthorDto authorDto = new AuthorDto();
        authorDto.setAuthorId(id);
        authorDto.setAuthorName(name);
        authorDto.setAuthorSurname(surname);
        return authorDto;
    }

    private Book createBook(Long id, String name, String isbn, int quantity){
        Book book = new Book();
        book.setBookId(id);
        book.setBookName(name);
        book.setIsbn(isbn);
        book.setAvailableQuantity(quantity);
        return book;
    }

    private BookDto createBookDto(Long id, String name, String isbn, int quantity, String publisher){
        BookDto bookDto = new BookDto();
        bookDto.setBookid(id);
        bookDto.setBookName(name);
        bookDto.setIsbn(isbn);
        bookDto.setAvailableQuantity(quantity);
        bookDto.setPublisher(publisher);
        return bookDto;
    }

    private Customer createCustomer(Long id, String name, String surname){
        Customer customer = new Customer();
        customer.setCustomerId(id);
        customer.setCustomerName(name);
        customer.setCustomerSurname(surname);
        return customer;
    }

    private CustomerDto createCustomerDto(Long id, String name, String surname){
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(id);
        customerDto.setCustomerName(name);
        customerDto.setCustomerSurname(surname);
        return customerDto;
    }

    private Librarian createLibrarian(Long id, String name, String surname){
        Librarian librarian = new Librarian();
        librarian.setLibrarianId(id);
        librarian.setLibrarianName(name);
        librarian.setLibrarianSurname(surname);
        return librarian;
    }

    private LibrarianDto createLibrarianDto(Long id, String name, String surname){
        LibrarianDto librarianDto = new LibrarianDto();
        librarianDto.setLibrarianId(id);
        librarianDto.setLibrarianName(name);
        librarianDto.setLibrarianSurname(surname);
        return librarianDto;
    }

    private Loan createLoan(Long id, Book book, Customer customer, Librarian librarian){
        Loan loan = new Loan();
        loan.setLoanId(id);
        loan.setStartLoanDate(returnExampleDateTime());
        loan.setActive(true);
        loan.setBook(book);
        loan.setCustomer(customer);
        loan.setLibrarian(librarian);
        return loan;
    }

    private LoanDto createLoanDto(Long id, Long bookId, Long customerId, Long librarianId){
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanId(id);
        loanDto.setStartLoanDate(returnExampleDateTime());
        loanDto.setActive(true);
        loanDto.setCustomerId(customerId);
        loanDto.setBookId(bookId);
        loanDto.setLibrarianId(librarianId);
        return loanDto;
    }

    private LocalDateTime returnExampleDateTime(){
        String str = "2020-01-01 11:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(str, formatter);
    }

    private Publisher createPublisher(Long id, String name){
        Publisher publisher = new Publisher();
        publisher.setPublisherId(id);
        publisher.setPublisherName(name);
        return publisher;
    }

    private PublisherDto createPublisherDto(Long id, String name){
        PublisherDto publisherDto = new PublisherDto();
        publisherDto.setPublisherId(id);
        publisherDto.setPublisherName(name);
        return publisherDto;
    }
}
