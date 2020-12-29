package pl.library.components.loan;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.library.components.book.Book;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan,Long> {

    //List<Loan> findAllByActiveEquals(Boolean status);
}
