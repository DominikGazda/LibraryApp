package pl.library.components.librarian;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.librarian.exceptions.LibrarianNotFoundException;
import pl.library.components.loan.Loan;
import pl.library.components.loan.LoanDto;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibrarianService {

    private LibrarianRepository librarianRepository;

    public LibrarianService(LibrarianRepository librarianRepository){
        this.librarianRepository = librarianRepository;
    }

    public List<LibrarianDto> getLibrarians(){
        return librarianRepository.findAll()
                .stream()
                .map(LibrarianMapper::toDto)
                .collect(Collectors.toList());
    }

    public LibrarianDto saveLibrarian(LibrarianDto dto){
        if (dto.getLibrarianId() != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Librarian cannot have id");
        return mapAndSaveLibrarian(dto);
    }

    public LibrarianDto getLibrarianById(Long id){
        Librarian foundLibrarian = librarianRepository.findById(id).orElseThrow(LibrarianNotFoundException::new);
        return LibrarianMapper.toDto(foundLibrarian);
    }

    public LibrarianDto updateLibrarian(LibrarianDto dto){
        return mapAndSaveLibrarian(dto);
    }

    public LibrarianDto deleteLibrarian(Long id){
        Librarian librarianToDelete = librarianRepository.findById(id).orElseThrow(LibrarianNotFoundException::new);
        librarianRepository.delete(librarianToDelete);
        return LibrarianMapper.toDto(librarianToDelete);
    }

    public void checkErrors(BindingResult result){
        List<ObjectError> errors = result.getAllErrors();
        String message = errors
                .stream()
                .map(ObjectError::getDefaultMessage)
                .map(s -> s.toString() +" ")
                .collect(Collectors.joining());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
    }

    private LibrarianDto mapAndSaveLibrarian(LibrarianDto dto){
        Librarian librarianToSave = LibrarianMapper.toEntity(dto);
        Librarian savedLibrarian = librarianRepository.save(librarianToSave);
        return LibrarianMapper.toDto(savedLibrarian);
    }
}
