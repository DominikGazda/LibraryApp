package pl.library.components.author;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.author.exceptions.AuthorNotFoundException;
import pl.library.components.book.BookDto;
import pl.library.components.book.BookMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    public List<AuthorDto> getAuthors(){
        return authorRepository.findAll()
                .stream()
                .map(AuthorMapper::toDto)
                .collect(Collectors.toList());
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

    public List<BookDto> getAllBooksForAuthorById(Long id){
        Author foundAuthor = authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
        return foundAuthor.getBookList().stream()
                .map(BookMapper::toDto)
                .collect(Collectors.toList());
    }
}
