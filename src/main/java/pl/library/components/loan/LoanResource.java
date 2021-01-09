package pl.library.components.loan;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
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
    public ResponseEntity<LoanDto> saveLoan(@Valid @RequestBody LoanDto loanDto, BindingResult result){
        if(result.hasErrors())
            loanService.checkErrors(result);
        System.out.println(loanDto);
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
    public ResponseEntity<LoanDto> updateLoan(@PathVariable Long id,@Valid @RequestBody LoanDto dto, BindingResult result){
            if(result.hasErrors())
                loanService.checkErrors(result);
           if(!id.equals(dto.getLibrarianId()))
               throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Loan must have id same as path variable");
           LoanDto updatedLoan = loanService.updateLoan(dto);
           URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build()
                .toUri();
           return ResponseEntity.created(location).body(updatedLoan);
    }

    @DeleteMapping("/{id}")
    public LoanDto deleteLoan(@PathVariable Long id){
        return loanService.deleteLoan(id);
    }
}
