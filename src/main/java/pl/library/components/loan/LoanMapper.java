package pl.library.components.loan;

import org.springframework.stereotype.Service;
import pl.library.components.book.BookMapper;
import pl.library.components.book.BookRepository;
import pl.library.components.customer.Customer;
import pl.library.components.customer.CustomerMapper;
import pl.library.components.customer.CustomerRepository;
import pl.library.components.customer.exceptions.CustomerNotFoundException;
import pl.library.components.librarian.Librarian;
import pl.library.components.librarian.LibrarianRepository;
import pl.library.components.librarian.exceptions.LibrarianNotFoundException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


@Service
public class LoanMapper {

    private LoanRepository loanRepository;
    private LibrarianRepository librarianRepository;
    private CustomerRepository customerRepository;
    private BookRepository bookRepository;
    private BookMapper bookMapper;

    public LoanMapper(LoanRepository loanRepository, LibrarianRepository librarianRepository,CustomerRepository customerRepository, BookRepository bookRepository, BookMapper bookMapper){
        this.librarianRepository = librarianRepository;
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }


    public LoanDto toDto(Loan entity){
        LoanDto dto = new LoanDto();

        dto.setLoanId(entity.getLoanId());
        dto.setStartLoanDate(entity.getStartLoanDate());
        dto.setReturnLoanDate(entity.getReturnLoanDate());
        dto.setActive(entity.isActive());
        dto.setBookList(entity.getBookList()
                        .stream()
                        .map(bookMapper::toDto)
                        .collect(Collectors.toList())
                );
        dto.setCustomer(CustomerMapper.toDto(entity.getCustomer()));
        dto.setLibrarianId(entity.getLibrarian().getLibrarianId());
        return dto;
    }

    public Loan toEntity(LoanDto dto){
        Loan entity = new Loan();
        Librarian librarian = librarianRepository.findById(dto.getLibrarianId()).orElseThrow(LibrarianNotFoundException::new);

        Customer customer = new Customer();
        customer.setCustomerId(dto.getCustomer().getCustomerId());
        customer.setCustomerName(dto.getCustomer().getCustomerName());
        customer.setCustomerSurname(dto.getCustomer().getCustomerSurname());
        customer.setAddress(dto.getCustomer().getAddress());

        entity.setLoanId(dto.getLoanId());
        entity.setStartLoanDate(LocalDateTime.now());
        entity.setReturnLoanDate(dto.getReturnLoanDate());
        entity.setActive(true);
        entity.setBookList(dto.getBookList()
                .stream()
                .map(bookMapper::toEntity)
                .collect(Collectors.toList())
        );
        entity.setCustomer(customer);
        entity.setLibrarian(librarian);
        return entity;
    }
}
