package pl.library.components.librarian;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LibrarianMapperTest {

    @Test
    void toDto_ShouldReturnLibrarianDto() {
        //given
        Librarian librarian = createLibrarian(1L,"Kamil","Kamil");
        LibrarianDto librarianDto = createLibrarianDto(1L, "Kamil", "Kamil");
        //when
        //then
        LibrarianDto resultLibrarian = LibrarianMapper.toDto(librarian);
        assertThat(resultLibrarian).isEqualTo(librarianDto);
    }

    @Test
    void toEntity_ShouldReturnLibrarianEntity() {
        //given
        Librarian librarian = createLibrarian(1L,"Kamil","Kamil");
        LibrarianDto librarianDto = createLibrarianDto(1L, "Kamil", "Kamil");
        //when
        //then
        Librarian resultLibrarian = LibrarianMapper.toEntity(librarianDto);
        assertThat(resultLibrarian).isEqualTo(librarian);
    }

    private Librarian createLibrarian(Long id, String name, String surname){
        Librarian librarian = new Librarian();
        librarian.setLibrarianId(id);
        librarian.setLibrarianName(name);
        librarian.setLibrarianSurname(surname);
        librarian.setLoanList(new ArrayList<>());
        return librarian;
    }

    private LibrarianDto createLibrarianDto(Long id, String name, String surname){
        LibrarianDto librarian = new LibrarianDto();
        librarian.setLibrarianId(id);
        librarian.setLibrarianName(name);
        librarian.setLibrarianSurname(surname);
        return librarian;
    }
}