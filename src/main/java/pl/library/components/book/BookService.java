package pl.library.components.book;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
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

    public List<BookDto> getBooks(){
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
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

    private BookDto mapAndSaveBook(BookDto dto){
        Book bookToSave = bookMapper.toEntity(dto);
        Book savedBook = bookRepository.save(bookToSave);
        return bookMapper.toDto(savedBook);
    }
}
