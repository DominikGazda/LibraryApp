package pl.library.components.librarian;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.librarian.exceptions.LibrarianNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LibrarianServiceTest {

    @Mock
    private LibrarianRepository librarianRepository;
    @InjectMocks
    private LibrarianService librarianService;
    MockedStatic<LibrarianMapper> librarianMapper;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        librarianMapper = Mockito.mockStatic(LibrarianMapper.class);
    }

    @AfterEach
    void end(){
        librarianMapper.close();
    }
    @Test
    void getLibrarians_ShouldReturnLibrarianList() {
        //given
        LibrarianDto firstLibrarianDto = createLibrarian(1L,"Marek","Kowal");
        LibrarianDto secondLibrarianDto = createLibrarian(2L,"Marcin","Kozak");
        List<LibrarianDto> librarianDtoList = List.of(firstLibrarianDto, secondLibrarianDto);

        Librarian firstLibrarian = mapToEntity(firstLibrarianDto);
        Librarian secondLibrarian = mapToEntity(secondLibrarianDto);
        List<Librarian> librarianList = List.of(firstLibrarian, secondLibrarian);
        //when
        when(librarianRepository.findAll()).thenReturn(librarianList);
        librarianMapper.when(() -> LibrarianMapper.toDto(any(Librarian.class))).thenReturn(firstLibrarianDto, secondLibrarianDto);
        //then
        assertThat(librarianService.getLibrarians()).isEqualTo(librarianDtoList);

    }

    @Test
    void saveLibrarian_ShouldReturnSavedLibrarian(){
        //given
        LibrarianDto librarianDto = createLibrarian(null,"Marek","Kowal");
        LibrarianDto savedLibrarianDto = createLibrarian(1L,"Marek","Kowal");
        Librarian librarian = mapToEntity(librarianDto);
        Librarian savedLibrarian = mapToEntity(savedLibrarianDto);
        //when
        librarianMapper.when(() -> LibrarianMapper.toEntity(librarianDto)).thenReturn(librarian);
        when(librarianRepository.save(librarian)).thenReturn(savedLibrarian);
        librarianMapper.when(() -> LibrarianMapper.toDto(savedLibrarian)).thenReturn(savedLibrarianDto);
        //then
        assertThat(librarianService.saveLibrarian(librarianDto)).isEqualTo(savedLibrarianDto);
    }

    @Test
    void saveLibrarian_ShouldThrowResponseStatusException_WhenLibrarianHasId(){
        //given
        LibrarianDto librarianDto = createLibrarian(1L,"Marek","Kowal");
        //when
        //then
        assertThatThrownBy(() -> librarianService.saveLibrarian(librarianDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("400 BAD_REQUEST \"Librarian cannot have id\"");
    }

    @Test
    void getLibrarianById_ShouldReturnLibrarianWithProvidedId(){
        //given
        LibrarianDto librarianDto = createLibrarian(1L,"Marek","Kowal");
        Librarian librarian = mapToEntity(librarianDto);
        //when
        when(librarianRepository.findById(anyLong())).thenReturn(Optional.of(librarian));
        librarianMapper.when(() -> LibrarianMapper.toDto(librarian)).thenReturn(librarianDto);
        //then
        assertThat(librarianService.getLibrarianById(anyLong())).isEqualTo(librarianDto);
    }

    @Test
    void getLibrarianById_ShouldThrowLibrarianNotFoundException_WhenLibrarianNotExists(){
        //given
        //when
        when(librarianRepository.findById(anyLong())).thenThrow(new LibrarianNotFoundException());
        //then
        assertThatThrownBy(() -> librarianService.deleteLibrarian(anyLong()))
                .isInstanceOf(LibrarianNotFoundException.class);
    }

    @Test
    void updateLibrarian_ShouldReturnUpdatedLibrarian(){
        //given
        LibrarianDto librarianDto = createLibrarian(1L,"Marek","Kowal");
        LibrarianDto updatedLibrarianDto = createLibrarian(1L,"Kamil","Kowal");
        Librarian librarian = mapToEntity(librarianDto);
        Librarian updatedLibrarian = mapToEntity(updatedLibrarianDto);
        //when
        librarianMapper.when(() -> LibrarianMapper.toEntity(librarianDto)).thenReturn(librarian);
        when(librarianRepository.save(librarian)).thenReturn(updatedLibrarian);
        librarianMapper.when(() -> LibrarianMapper.toDto(updatedLibrarian)).thenReturn(updatedLibrarianDto);
        //then
        assertThat(librarianService.updateLibrarian(librarianDto)).isEqualTo(updatedLibrarianDto);
    }

    @Test
    void deleteLibrarian_ShouldReturnDeletedLibrarian(){
        //then
        LibrarianDto librarianDto = createLibrarian(1L,"Marek","Kowal");
        Librarian librarian = mapToEntity(librarianDto);
        ArgumentCaptor<Librarian> captor = ArgumentCaptor.forClass(Librarian.class);
        //when
        when(librarianRepository.findById(anyLong())).thenReturn(Optional.of(librarian));
        librarianMapper.when(() -> LibrarianMapper.toDto(librarian)).thenReturn(librarianDto);
        //then
        LibrarianDto deletedLibrarian = librarianService.deleteLibrarian(anyLong());
        verify(librarianRepository).delete(captor.capture());
        assertAll(
                () -> assertThat(deletedLibrarian).isEqualTo(librarianDto),
                () -> assertThat(captor.getValue()).isEqualTo(librarian)
        );
    }

    @Test
    void deleteLibrarian_ShouldThrowLibrarianNotFoundException_WhenLibrarianNotExists(){
        //given
        //when
        when(librarianRepository.findById(anyLong())).thenThrow(new LibrarianNotFoundException());
        //then
        assertThatThrownBy(() -> librarianService.deleteLibrarian(anyLong()))
                .isInstanceOf(LibrarianNotFoundException.class);
    }

    private LibrarianDto createLibrarian(Long id, String name, String surname){
        LibrarianDto librarianDto = new LibrarianDto();
        librarianDto.setLibrarianId(id);
        librarianDto.setLibrarianName(name);
        librarianDto.setLibrarianSurname(surname);
        return librarianDto;
    }

        private Librarian mapToEntity (LibrarianDto dto){
        Librarian librarian = new Librarian();
        librarian.setLibrarianId(dto.getLibrarianId());
        librarian.setLibrarianName(dto.getLibrarianName());
        librarian.setLibrarianSurname(dto.getLibrarianSurname());
        return librarian;
    }
}