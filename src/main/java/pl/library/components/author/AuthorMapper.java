package pl.library.components.author;

public class AuthorMapper {

    static AuthorDto toDto(Author entity){
        AuthorDto dto = new AuthorDto();
        dto.setAuthorId(entity.getAuthorId());
        dto.setAuthorName(entity.getAuthorName());
        dto.setAuthorSurname(entity.getAuthorSurname());
        return dto;
    }

    static Author toEntity(AuthorDto dto){
        Author entity = new Author();
        entity.setAuthorId(dto.getAuthorId());
        entity.setAuthorName(dto.getAuthorName());
        entity.setAuthorSurname(dto.getAuthorSurname());
        return entity;
    }
}
