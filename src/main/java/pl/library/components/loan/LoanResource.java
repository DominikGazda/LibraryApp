package pl.library.components.loan;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/loan", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoanResource {

    private LoanService loanService;

    public LoanResource(LoanService loanService){
        this.loanService = loanService;
    }

    @GetMapping("")
    public List<LoanDto> getLoans(){
        return loanService.getLoans();
    }

    @PostMapping("")
    public ResponseEntity<LoanDto> saveLoan(@RequestBody LoanDto loanDto){
        LoanDto savedLoan = loanService.saveLoan(loanDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedLoan.getLoanId())
                .toUri();
        return ResponseEntity.created(location).body(savedLoan);
    }

    @GetMapping("/{id}")
    public LoanDto getLoanById(@PathVariable Long id){
        return loanService.getLoanById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanDto> updateLoan(@PathVariable Long id, @RequestBody LoanDto dto){
           if(!id.equals(dto.getLibrarianId()))
               throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Loan must have id same as path variable");
           LoanDto updatedLoan = loanService.updateLoan(dto);
           URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                   .path("/{id}")
                   .buildAndExpand(updatedLoan.getLoanId())
                   .toUri();
           return ResponseEntity.created(location).body(updatedLoan);
    }

    @DeleteMapping("/{id}")
    public LoanDto deleteLoan(@PathVariable Long id){
        return loanService.deleteLoan(id);
    }
}
