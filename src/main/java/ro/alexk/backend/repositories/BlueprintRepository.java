package ro.alexk.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.alexk.backend.entities.Blueprint;
import ro.alexk.backend.models.db.projections.BlueprintId;
import ro.alexk.backend.models.rest.BlueprintDTO;

import java.util.List;
import java.util.Optional;

public interface BlueprintRepository extends JpaRepository<Blueprint, Integer> {
    Optional<BlueprintId> getByName(String name);
    @Query("select new ro.alexk.backend.models.rest.BlueprintDTO(b.id, b.displayName, b.isHardware, b.isValid) from Blueprint b")
    List<BlueprintDTO> getAll();

    boolean existsByIdAndIsHardware(int id, boolean isHardware);

    @Query("select b.name from Blueprint b where b.id = :id")
    Optional<String> getNameById(int id);
}
