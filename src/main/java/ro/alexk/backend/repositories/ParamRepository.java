package ro.alexk.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.alexk.backend.entities.Param;

public interface ParamRepository extends JpaRepository<Param, Integer> {
}
