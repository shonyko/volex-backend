package ro.alexk.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.alexk.backend.entities.Agent;
import ro.alexk.backend.models.db.projections.AgentProj;
import ro.alexk.backend.models.rest.AgentDTO;

import java.util.List;
import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Integer> {
    @Query("select a.id as id, a.name as name, a.blueprint.id as blueprintId, ha.macAddr as macAddr from Agent a left join HwAgent ha on ha.agent.id = a.id")
    List<AgentProj> getAll();

    @Query("select new ro.alexk.backend.models.rest.AgentDTO(a.id, a.name, a.blueprint.id, ha.macAddr) from Agent a left join HwAgent ha on ha.agent.id = a.id where a.id = :id")
    Optional<AgentDTO> getDtoById(Integer id);
}
