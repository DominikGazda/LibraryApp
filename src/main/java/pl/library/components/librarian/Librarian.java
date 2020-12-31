package pl.library.components.librarian;

import pl.library.components.loan.Loan;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name= "librarians")
public class Librarian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long librarianId;
    @NotEmpty(message = "{pl.library.components.librarian.Librarian.librarianName.NotEmpty}")
    private String librarianName;
    @NotEmpty(message = "{pl.library.components.librarian.Librarian.librarianSurname.NotEmpty}")
    private String librarianSurname;

    @OneToMany(mappedBy = "librarian")
    private List<Loan> loanList;

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

    public List<Loan> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<Loan> loanList) {
        this.loanList = loanList;
    }

    public void addLoan(Loan loan){
        loan.setLibrarian(this);
        this.loanList.add(loan);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Librarian librarian = (Librarian) o;
        return Objects.equals(librarianId, librarian.librarianId) &&
                Objects.equals(librarianName, librarian.librarianName) &&
                Objects.equals(librarianSurname, librarian.librarianSurname) &&
                Objects.equals(loanList, librarian.loanList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(librarianId, librarianName, librarianSurname, loanList);
    }
}
