package pl.library.components.loan;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.customer.Customer;
import pl.library.components.loan.exceptions.LoanNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    private LoanRepository loanRepository;
    private LoanMapper loanMapper;

    public LoanService(LoanRepository loanRepository, LoanMapper loanMapper){
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
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
        Loan loanToSave = loanMapper.toEntity(dto);
        Customer customerToSave = loanToSave.getCustomer();
        loanToSave.setCustomer(customerToSave);
        Loan savedLoan = loanRepository.save(loanToSave);
        return loanMapper.toDto(savedLoan);
    }

    public LoanDto getLoanById(Long id){
        return loanRepository.findById(id)
                .map(loanMapper::toDto)
                .orElseThrow(LoanNotFoundException::new);
    }

    public LoanDto updateLoan(LoanDto dto){
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
}
