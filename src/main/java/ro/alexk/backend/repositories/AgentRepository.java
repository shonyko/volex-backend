package ro.alexk.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.alexk.backend.entities.Agent;
import ro.alexk.backend.models.db.projections.AgentProj;

import java.util.List;

public interface AgentRepository extends JpaRepository<Agent, Integer> {
    @Query("select a.id as id, a.name as name, a.blueprint.id as blueprintId, ha.macAddr as macAddr from Agent a left join HwAgent ha on ha.agent.id = a.id")
    List<AgentProj> getAll();
}
