package pl.library.components.librarian;

public class LibrarianMapper {

    public static LibrarianDto toDto(Librarian entity){
        LibrarianDto dto = new LibrarianDto();
        dto.setLibrarianId(entity.getLibrarianId());
        dto.setLibrarianName(entity.getLibrarianName());
        dto.setLibrarianSurname(entity.getLibrarianSurname());
        return dto;
    }

    public static Librarian toEntity (LibrarianDto dto){
        Librarian librarian = new Librarian();
        librarian.setLibrarianId(dto.getLibrarianId());
        librarian.setLibrarianName(dto.getLibrarianName());
        librarian.setLibrarianSurname(dto.getLibrarianSurname());
        return librarian;
    }
}
