package pl.library.components.librarian;

import javax.validation.constraints.NotEmpty;

public class LibrarianDto {

    private Long librarianId;
    @NotEmpty(message = "{pl.library.components.librarian.Librarian.librarianName.NotEmpty}")
    private String librarianName;
    @NotEmpty(message = "{pl.library.components.librarian.Librarian.librarianSurname.NotEmpty}")
    private String librarianSurname;

    public Long getLibrarianId() {
        return librarianId;
    }

    public void setLibrarianId(Long librarianId) {
        this.librarianId = librarianId;
    }

    public String getLibrarianName() {
        return librarianName;
    }

    public void setLibrarianName(String librarianName) {
        this.librarianName = librarianName;
    }

    public String getLibrarianSurname() {
        return librarianSurname;
    }

    public void setLibrarianSurname(String librarianSurname) {
        this.librarianSurname = librarianSurname;
    }
}
