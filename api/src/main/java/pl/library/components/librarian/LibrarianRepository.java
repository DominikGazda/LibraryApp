package pl.library.components.librarian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.library.components.loan.Loan;

import java.util.List;

@Repository
public interface LibrarianRepository extends JpaRepository<Librarian,Long> {

}
