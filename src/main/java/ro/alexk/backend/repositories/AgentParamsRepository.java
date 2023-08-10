package ro.alexk.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.alexk.backend.entities.AgentParam;

public interface AgentParamsRepository extends JpaRepository<AgentParam, Integer> {
}
