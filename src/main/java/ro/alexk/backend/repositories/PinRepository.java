package ro.alexk.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.alexk.backend.entities.Pin;

public interface PinRepository extends JpaRepository<Pin, Integer> {
}
