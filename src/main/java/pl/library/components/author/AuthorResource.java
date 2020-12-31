package pl.library.components.author;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.library.components.book.BookDto;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/author", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthorResource {

    private AuthorService authorService;

    public AuthorResource(AuthorService authorService){
        this.authorService = authorService;
    }

    @GetMapping("")
    public List<AuthorDto> getAuthors(){
        return  authorService.getAuthors();
    }

    @PostMapping("")
    public ResponseEntity<AuthorDto> saveAuthor(@Valid @RequestBody AuthorDto dto, BindingResult result){
        if(result.hasErrors())
            authorService.checkErrors(result);
        AuthorDto savedAuthor = authorService.saveAuthor(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedAuthor.getAuthorId())
                .toUri();
        return ResponseEntity.created(location).body(savedAuthor);
    }

    @GetMapping("/{id}")
    public AuthorDto getAuthorById(@PathVariable Long id){
        return authorService.getAuthorById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable Long id,@Valid @RequestBody AuthorDto dto){
        if(!dto.getAuthorId().equals(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Author must have id same as path variable");
        AuthorDto updatedAuthor = authorService.updateAuthor(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updatedAuthor.getAuthorId())
                .toUri();
        return ResponseEntity.created(location).body(updatedAuthor);
    }

    @DeleteMapping("/{id}")
    public AuthorDto deleteAuthor(@PathVariable Long id){
        return authorService.deleteAuthor(id);
    }

    @GetMapping("/{id}/books")
    public List<BookDto> getAllBooksForAuthorById(@PathVariable Long id){
        return authorService.getAllBooksForAuthorById(id);
    }
}
