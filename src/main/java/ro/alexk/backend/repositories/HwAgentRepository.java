package ro.alexk.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.alexk.backend.entities.HwAgent;
import ro.alexk.backend.models.db.projections.config.HwAgentConfig;

public interface HwAgentRepository extends JpaRepository<HwAgent, Integer> {
    boolean existsByMacAddr(String macAddr);
    HwAgentConfig findByMacAddr(String macAddr);
}
