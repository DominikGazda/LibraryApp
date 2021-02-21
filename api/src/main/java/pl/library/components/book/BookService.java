package pl.library.components.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.author.*;
import pl.library.components.author.exceptions.AuthorNotFoundException;
import pl.library.components.book.exceptions.BookNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private BookRepository bookRepository;
    private BookMapper bookMapper;
    private AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, BookMapper bookMapper, AuthorRepository authorRepository){
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.authorRepository = authorRepository;
    }

    public Page<BookDto> getBooks(Integer pageNo, Integer pageSize){
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Book> pageResult = bookRepository.findAll(paging);
        Page<BookDto> pageDtoResult = pageResult.map(bookMapper::toDto);
        return pageDtoResult;
    }

    public BookDto saveBook(BookDto dto){
        if(dto.getBookid() != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Book cannot have id");
        return mapAndSaveBook(dto);
    }

    public BookDto getBookById(Long id){
        Book foundBook = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return bookMapper.toDto(foundBook);
    }

    public BookDto updateBook(BookDto dto){
        return mapAndSaveBook(dto);
    }

    public BookDto deleteBook(Long id){
        Book bookToDelete = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        bookRepository.delete(bookToDelete);
        return bookMapper.toDto(bookToDelete);
    }

    public List<AuthorDto> getAllAuthorFromBookById(Long id){
        Book foundBook = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return foundBook.getAuthorList()
                .stream()
                .map(AuthorMapper::toDto)
                .collect(Collectors.toList());
    }

    public AuthorDto saveAuthorForBookById(Long bookId, Long authorId){
        Book foundBook = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        Author foundAuthor = authorRepository.findById(authorId).orElseThrow(AuthorNotFoundException::new);

        for(Author a: foundBook.getAuthorList()){
            if(a.equals(foundAuthor))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Book already have assigned author with provided id");
        }

        foundBook.getAuthorList().add(foundAuthor);
        bookRepository.save(foundBook);
        return AuthorMapper.toDto(foundAuthor);
    }

    public List<BookDto> findBooksByName(String bookName){
        return bookRepository.findByBookNameContainingIgnoreCase(bookName)
                .stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    private BookDto mapAndSaveBook(BookDto dto){
        Book bookToSave = bookMapper.toEntity(dto);
        Book savedBook = bookRepository.save(bookToSave);
        return bookMapper.toDto(savedBook);
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
}
