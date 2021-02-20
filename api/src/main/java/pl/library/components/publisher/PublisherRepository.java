package pl.library.components.publisher;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher,Long> {

    Optional<Publisher> findByPublisherNameIgnoreCase(String name);
}
