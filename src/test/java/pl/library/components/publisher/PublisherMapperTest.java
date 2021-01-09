package pl.library.components.publisher;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PublisherMapperTest {

    @Test
    void toDto_ShouldReturnPublisherDto() {
        //given
        Publisher publisher = createPublisher(1L, "WSIP");
        PublisherDto publisherDto = createPublisherDto(1L, "WSIP");
        //when
        //then
        PublisherDto resultPublisher = PublisherMapper.toDto(publisher);
        assertThat(resultPublisher).isEqualTo(publisherDto);
    }

    @Test
    void toEntity_ShouldReturnPublisherEntity() {
        //given
        Publisher publisher = createPublisher(1L, "WSIP");
        PublisherDto publisherDto = createPublisherDto(1L, "WSIP");
        //when
        //then
        Publisher resultPublisher = PublisherMapper.toEntity(publisherDto);
        assertThat(publisher).isEqualTo(resultPublisher);
    }

    private PublisherDto createPublisherDto(Long id, String name){
        PublisherDto publisherDto = new PublisherDto();
        publisherDto.setPublisherId(id);
        publisherDto.setPublisherName(name);
        return publisherDto;
    }

    private Publisher createPublisher(Long id, String name){
        Publisher publisher = new Publisher();
        publisher.setPublisherId(id);
        publisher.setPublisherName(name);
        publisher.setBookList(new ArrayList<>());
        return publisher;
    }
}