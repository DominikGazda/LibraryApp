package pl.library.components.author;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AuthorMapperTest {

    @Test
    void toDto_ShouldReturnAuthorDto() {
        //given
        Author author = createAuthor(1L, "Adam", "Autor");
        AuthorDto authorDto = mapAuthorToDto(author);
        //when
        //then
        AuthorDto resultAuthorDto = AuthorMapper.toDto(author);
        assertThat(authorDto).isEqualTo(resultAuthorDto);
    }

    @Test
    void toEntity_ShouldReturnAuthorEntity() {
        //given
        Author author = createAuthor(1L, "Adam", "Autor");
        AuthorDto authorDto = mapAuthorToDto(author);
        //when
        //then
        Author resultAuthor = AuthorMapper.toEntity(authorDto);
        assertThat(author).isEqualTo(resultAuthor);
    }

    private Author createAuthor(Long id, String name, String surname){
        Author author = new Author();
        author.setAuthorId(id);
        author.setAuthorName(name);
        author.setAuthorSurname(surname);
        author.setBookList(new ArrayList<>());
        return author;
    }

    public AuthorDto mapAuthorToDto(Author entity){
        AuthorDto dto = new AuthorDto();
        if(entity.getAuthorId() == null)
            dto.setAuthorId(null);
        else
            dto.setAuthorId(entity.getAuthorId());
        dto.setAuthorName(entity.getAuthorName());
        dto.setAuthorSurname(entity.getAuthorSurname());
        return dto;
    }
}