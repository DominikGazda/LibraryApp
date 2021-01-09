package pl.library.components.loan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.library.components.book.Book;
import pl.library.components.book.BookMapper;
import pl.library.components.book.BookRepository;
import pl.library.components.book.exceptions.BookNotFoundException;
import pl.library.components.customer.Customer;
import pl.library.components.customer.CustomerMapper;
import pl.library.components.customer.CustomerRepository;
import pl.library.components.customer.exceptions.CustomerNotFoundException;
import pl.library.components.librarian.Librarian;
import pl.library.components.librarian.LibrarianRepository;
import pl.library.components.librarian.exceptions.LibrarianNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class LoanMapper {


    private LibrarianRepository librarianRepository;
    private BookRepository bookRepository;
    private CustomerRepository customerRepository;

    @Autowired
    public LoanMapper(LibrarianRepository librarianRepository, BookRepository bookRepository, CustomerRepository customerRepository){
        this.librarianRepository = librarianRepository;
        this.bookRepository = bookRepository;
        this.customerRepository = customerRepository;
    }


    public LoanDto toDto(Loan entity){
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

    public Loan toEntity(LoanDto dto){
        Loan entity = new Loan();
        Librarian librarian = librarianRepository.findById(dto.getLibrarianId()).orElseThrow(LibrarianNotFoundException::new);
        Book book = bookRepository.findById(dto.getBookId()).orElseThrow(BookNotFoundException::new);
        Customer customer = customerRepository.findById(dto.getCustomerId()).orElseThrow(CustomerNotFoundException::new);

        entity.setLoanId(dto.getLoanId());
        entity.setStartLoanDate(LocalDateTime.now());
        entity.setReturnLoanDate(dto.getReturnLoanDate());
        entity.setActive(true);
        entity.setBook(book);
        entity.setCustomer(customer);
        entity.setLibrarian(librarian);
        return entity;
    }

}
