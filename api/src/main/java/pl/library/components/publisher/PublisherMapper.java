package pl.library.components.publisher;

public class PublisherMapper {

    public static PublisherDto toDto (Publisher entity){
        PublisherDto dto = new PublisherDto();
        dto.setPublisherId(entity.getPublisherId());
        dto.setPublisherName(entity.getPublisherName());
        return dto;
    }

    public static Publisher toEntity (PublisherDto dto){
        Publisher entity = new Publisher();
        entity.setPublisherId(dto.getPublisherId());
        entity.setPublisherName(dto.getPublisherName());
        return entity;
    }
}
