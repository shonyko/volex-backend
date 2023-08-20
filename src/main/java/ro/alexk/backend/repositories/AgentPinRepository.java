package ro.alexk.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.alexk.backend.entities.AgentPin;
import ro.alexk.backend.models.db.projections.config.SrcPin;

import java.util.Optional;

public interface AgentPinRepository extends JpaRepository<AgentPin, Integer> {
    @Query(value = "select ap2.id, ap2.last_value from (select ap.src_pin_id from agent_pin ap where ap.id = :id) ap1 left join agent_pin ap2 on ap1.src_pin_id = ap2.id", nativeQuery = true)
    Optional<SrcPin> getSrcPinById(@Param("id") Integer id);
}
