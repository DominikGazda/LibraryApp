package pl.library.components.loan;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.book.Book;
import pl.library.components.book.BookRepository;
import pl.library.components.book.exceptions.BookNotFoundException;
import pl.library.components.customer.Customer;
import pl.library.components.loan.exceptions.LoanNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    private LoanRepository loanRepository;
    private LoanMapper loanMapper;
    private BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, LoanMapper loanMapper, BookRepository bookRepository){
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
        this.bookRepository = bookRepository;
    }

    public List<LoanDto> getLoans(){
        return loanRepository.findAll()
                .stream()
                .map(loanMapper::toDto)
                .collect(Collectors.toList());
    }

    public LoanDto saveLoan(LoanDto dto){
        if(dto.getLoanId() != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Loan cannot have id");
        if(dto.getBookId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Loan doesn't have assigned book");
        Loan loanToSave = loanMapper.toEntity(dto);
       // Customer customerToSave = loanToSave.getCustomer();
       // loanToSave.setCustomer(customerToSave);
        Loan savedLoan = loanRepository.save(loanToSave);
        changeBookAvailableQuantity(savedLoan);
        return loanMapper.toDto(savedLoan);
    }

    public LoanDto getLoanById(Long id){
        return loanRepository.findById(id)
                .map(loanMapper::toDto)
                .orElseThrow(LoanNotFoundException::new);
    }

    public LoanDto updateLoan(LoanDto dto){
        if(dto.getBookId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Loan doesn't have assigned book");
        Loan loanToSave = loanMapper.toEntity(dto);
        Customer customerToSave = loanToSave.getCustomer();
        loanToSave.setCustomer(customerToSave);
        Loan savedLoan = loanRepository.save(loanToSave);
        return loanMapper.toDto(savedLoan);
    }

    public LoanDto deleteLoan(Long id){
        Loan foundLoan = loanRepository.findById(id).orElseThrow(LoanNotFoundException::new);
        loanRepository.delete(foundLoan);
        return loanMapper.toDto(foundLoan);
    }

    public void checkErrors(BindingResult result){
        List<ObjectError> errors = result.getAllErrors();
        String message = errors
                .stream()
                .map(ObjectError::getDefaultMessage)
                .map(s -> s.toString() + " ")
                .collect(Collectors.joining());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
    }

    private void changeBookAvailableQuantity(Loan entity){
        Book book = bookRepository.findById(entity.getBook().getBookId()).orElseThrow(BookNotFoundException::new);
        book.setAvailableQuantity(book.getAvailableQuantity()-1);
        bookRepository.save(book);
    }
}
