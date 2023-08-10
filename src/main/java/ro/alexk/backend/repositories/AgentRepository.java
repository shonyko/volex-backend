package ro.alexk.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.alexk.backend.entities.Agent;

public interface AgentRepository extends JpaRepository<Agent, Integer> {
}
