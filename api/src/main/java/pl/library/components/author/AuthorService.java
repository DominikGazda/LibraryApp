package pl.library.components.author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.author.exceptions.AuthorNotFoundException;
import pl.library.components.book.BookDto;
import pl.library.components.book.BookMapper;
import pl.library.components.book.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private AuthorRepository authorRepository;
    private BookRepository bookRepository;
    private BookMapper bookMapper;

    public AuthorService(AuthorRepository authorRepository, BookMapper bookMapper, BookRepository bookRepository){
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
    }

    public Page<AuthorDto> getAuthors(Integer pageNumber, Integer pageSize){
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<Author> authors = authorRepository.findAll(paging);
        Page<AuthorDto> authorDto = authors.map(AuthorMapper::toDto);
        return authorDto;
    }

    public AuthorDto saveAuthor(AuthorDto dto){
        if(dto.getAuthorId() != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Author cannot have id");
        Author authorToSave = AuthorMapper.toEntity(dto);
        Author savedAuthor = authorRepository.save(authorToSave);
        return AuthorMapper.toDto(savedAuthor);
    }

    public AuthorDto getAuthorById(Long id){
        Author foundAuthor = authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
        return AuthorMapper.toDto(foundAuthor);
    }

    public AuthorDto updateAuthor(AuthorDto dto){
        Author authorToUpdate = AuthorMapper.toEntity(dto);
        Author updatedAuthor = authorRepository.save(authorToUpdate);
        return AuthorMapper.toDto(updatedAuthor);
    }

    public AuthorDto deleteAuthor(Long id){
        Author authorToDelete = authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
        authorRepository.delete(authorToDelete);
        return AuthorMapper.toDto(authorToDelete);
    }

    public Page<BookDto> getAllBooksForAuthorById(Long id, Integer pageNumber, Integer pageSize){
        Author foundAuthor = authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        List<BookDto> bookDtoList = foundAuthor.getBookList().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());

        int total = bookDtoList.size();
        int start = (int)paging.getOffset();
        int end = Math.min((start + paging.getPageSize()), total);
        List<BookDto> output = new ArrayList<>();

        if(start <= end)
            output = bookDtoList.subList(start, end);
        return new PageImpl<>(output, paging, total);
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
