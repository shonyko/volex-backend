package ro.alexk.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ro.alexk.backend.entities.AgentParam;
import ro.alexk.backend.models.rest.AgentParamDTO;

import java.util.List;

public interface AgentParamsRepository extends JpaRepository<AgentParam, Integer> {
    @Query("select new ro.alexk.backend.models.rest.AgentParamDTO(ap.id, ap.param.name, ap.param.blueprint.id, ap.param.dataType.name, ap.value, ap.agent.id) from AgentParam ap")
    List<AgentParamDTO> getAll();

    @Modifying
    @Query("update AgentParam ap set ap.value = :value where ap.id = :id")
    int updateValue(int id, String value);
}
