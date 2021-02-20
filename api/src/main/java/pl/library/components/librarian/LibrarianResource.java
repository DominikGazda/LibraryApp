package pl.library.components.librarian;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.library.components.loan.LoanDto;

import javax.naming.Binding;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/librarian", produces = MediaType.APPLICATION_JSON_VALUE)
public class LibrarianResource {

    private LibrarianService librarianService;

    public LibrarianResource(LibrarianService librarianService){
        this.librarianService = librarianService;
    }

    @GetMapping("")
    public List<LibrarianDto> getLibrarians() {
        return librarianService.getLibrarians();
    }

    @PostMapping("")
    public ResponseEntity<LibrarianDto> saveLibrarian(@Valid @RequestBody LibrarianDto dto, BindingResult result){
        if(result.hasErrors())
            librarianService.checkErrors(result);
        LibrarianDto savedLibrarian = librarianService.saveLibrarian(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedLibrarian.getLibrarianId())
                .toUri();
        return ResponseEntity.created(location).body(savedLibrarian);
    }

    @GetMapping("/{id}")
    public LibrarianDto getLibrarianById(@PathVariable Long id){
        return librarianService.getLibrarianById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibrarianDto> updateLibrarian(@PathVariable Long id,@Valid @RequestBody LibrarianDto dto, BindingResult result){
        if(result.hasErrors())
            librarianService.checkErrors(result);
        if(!id.equals(dto.getLibrarianId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Librarian must have id same as path variable");
        LibrarianDto updatedLibrarian = librarianService.updateLibrarian(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build()
                .toUri();
        return ResponseEntity.created(location).body(updatedLibrarian);
    }

    @DeleteMapping("/{id}")
    public LibrarianDto deleteLibrarian(@PathVariable Long id){
        return librarianService.deleteLibrarian(id);
    }

}
