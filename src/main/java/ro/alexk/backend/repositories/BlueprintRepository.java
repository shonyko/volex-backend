package ro.alexk.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.alexk.backend.entities.Blueprint;
import ro.alexk.backend.models.db.projections.BlueprintId;

import java.util.Optional;

public interface BlueprintRepository extends JpaRepository<Blueprint, Integer> {
    Optional<BlueprintId> getByName(String name);
}
