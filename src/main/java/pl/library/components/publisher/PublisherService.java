package pl.library.components.publisher;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.book.BookDto;
import pl.library.components.book.BookMapper;
import pl.library.components.publisher.exceptions.PublisherNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherService {

    private PublisherRepository publisherRepository;

    public PublisherService (PublisherRepository publisherRepository){
        this.publisherRepository = publisherRepository;
    }

    public List<PublisherDto> getAllPublishers(){
        return publisherRepository.findAll()
                .stream()
                .map(PublisherMapper::toDto)
                .collect(Collectors.toList());
    }

    public PublisherDto savePublisher(PublisherDto dto){
        if(dto.getPublisherId() != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Publisher cannot have id");
        return mapAndSavePublisher(dto);
    }

    public PublisherDto getPublisherById(Long id){
        Publisher foundPublisher = publisherRepository.findById(id).orElseThrow(PublisherNotFoundException::new);
        return PublisherMapper.toDto(foundPublisher);
    }

    public PublisherDto updatePublisher(PublisherDto dto){
        return mapAndSavePublisher(dto);
    }

    public PublisherDto deletePublisher(Long id){
        Publisher publisherToDelete = publisherRepository.findById(id).orElseThrow(PublisherNotFoundException::new);
        publisherRepository.delete(publisherToDelete);
        return PublisherMapper.toDto(publisherToDelete);
    }

    public List<BookDto> getAllBooksForPublisherById(Long id){
        Publisher foundPublisher = publisherRepository.findById(id).orElseThrow(PublisherNotFoundException::new);
        return foundPublisher.getBookList()
                .stream()
                .map(BookMapper::toDto)
                .collect(Collectors.toList());
    }

    private PublisherDto mapAndSavePublisher(PublisherDto dto){
        Publisher publisherToSave = PublisherMapper.toEntity(dto);
        Publisher savedPublisher = publisherRepository.save(publisherToSave);
        return PublisherMapper.toDto(savedPublisher);
    }

}
