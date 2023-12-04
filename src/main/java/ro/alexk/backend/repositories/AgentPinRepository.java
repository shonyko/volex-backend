package ro.alexk.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.alexk.backend.entities.AgentPin;
import ro.alexk.backend.models.db.projections.AgentPinProj;
import ro.alexk.backend.models.db.projections.config.SrcPin;

import java.util.List;
import java.util.Optional;

public interface AgentPinRepository extends JpaRepository<AgentPin, Integer> {
    @Query(value = "select ap2.id, ap2.last_value from (select ap.src_pin_id from agent_pin ap where ap.id = :id) ap1 left join agent_pin ap2 on ap1.src_pin_id = ap2.id", nativeQuery = true)
    Optional<SrcPin> getSrcPinById(@Param("id") Integer id);

    @Modifying
    @Query("update AgentPin ap set ap.lastValue = :value where ap.id = :id or ap.srcPin.id = :id")
    void updatePinValue(@Param("id") Integer id, String value);

    @Query("select ap.id as id, ap.pin.name as name, ap.pin.type as pinType, ap.pin.dataType.name as dataType, ap.pin.blueprint.id as blueprintId, ap.agent.id as agentId, ap.lastValue as value, ap.srcPin.id as srcPinId from AgentPin ap")
    List<AgentPinProj> getAll();
}
