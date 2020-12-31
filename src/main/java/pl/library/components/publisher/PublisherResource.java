package pl.library.components.publisher;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/api/publisher", produces = MediaType.APPLICATION_JSON_VALUE)
public class PublisherResource {

    private PublisherService publisherService;

    @Autowired
    public PublisherResource(PublisherService publisherService){
        this.publisherService = publisherService;
    }

    @GetMapping("")
    public List<PublisherDto> getAllPublishers(){
        return publisherService.getAllPublishers();
    }

    @PostMapping("")
    public ResponseEntity<PublisherDto> savePublisher(@Valid @RequestBody PublisherDto dto, BindingResult result){
        if(result.hasErrors())
            publisherService.checkErrors(result);
        PublisherDto savedPublisher = publisherService.savePublisher(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPublisher.getPublisherId())
                .toUri();
        return ResponseEntity.created(location).body(savedPublisher);
    }

    @GetMapping("/{id}")
    public PublisherDto getPublisherById(@PathVariable Long id){
        return publisherService.getPublisherById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherDto> updatePublisher(@PathVariable Long id, @Valid @RequestBody PublisherDto dto, BindingResult result){
        if(result.hasErrors())
            publisherService.checkErrors(result);
        if(!id.equals(dto.getPublisherId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Publisher must have id same as path variable");
        PublisherDto updatedPublisher = publisherService.updatePublisher(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updatedPublisher.getPublisherId())
                .toUri();
        return ResponseEntity.created(location).body(updatedPublisher);
    }

    @DeleteMapping("/{id}")
    public PublisherDto deletePublisher(@PathVariable Long id){
        return publisherService.deletePublisher(id);
    }

    @GetMapping("/{id}/books")
    public List<BookDto> getAllBooksForPublisherById(@PathVariable Long id){
        return publisherService.getAllBooksForPublisherById(id);
    }
}
