package ro.alexk.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.alexk.backend.entities.HwAgent;

public interface HwAgentRepository extends JpaRepository<HwAgent, Integer> {
    boolean existsByMacAddr(String macAddr);
}
