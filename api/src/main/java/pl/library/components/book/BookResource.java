package pl.library.components.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.library.components.author.AuthorDto;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookResource {

    private BookService bookService;

    @Autowired
    public BookResource(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping("")
    public Page<BookDto> getBooks(@RequestParam(defaultValue = "0") Integer pageNumber,
                                  @RequestParam(defaultValue = "8") Integer pageSize){
        return bookService.getBooks(pageNumber, pageSize);
    }

    @PostMapping("")
    public ResponseEntity<BookDto> saveBook(@Valid @RequestBody BookDto dto, BindingResult result){
        if(result.hasErrors())
            bookService.checkErrors(result);
        BookDto savedBook = bookService.saveBook(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBook.getBookid())
                .toUri();
        return ResponseEntity.created(location).body(savedBook);
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id){
        return bookService.getBookById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id,@Valid @RequestBody BookDto dto, BindingResult result){
        if(result.hasErrors())
            bookService.checkErrors(result);
        if (!id.equals(dto.getBookid()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Book must have id same as path variable");
        BookDto updatedBook = bookService.updateBook(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build()
                .toUri();
        return ResponseEntity.created(location).body(updatedBook);
    }

    @DeleteMapping("/{id}")
    public BookDto deleteBook(@PathVariable Long id){
        return bookService.deleteBook(id);
    }

    @GetMapping("/{id}/authors")
    public List<AuthorDto> getAllAuthorFromBookById(@PathVariable Long id){
        return bookService.getAllAuthorFromBookById(id);
    }

    @PostMapping("{/{idBook}/authors/{idAuthor}}")
    public AuthorDto saveAuthorForBookById(@PathVariable Long idBook, @PathVariable Long idAuthor){
        return bookService.saveAuthorForBookById(idBook,idAuthor);
    }

    @GetMapping("/search/findBooksByName")
    public List<BookDto> findBooksByName(@RequestParam String name){
        return bookService.findBooksByName(name);
    }

}
