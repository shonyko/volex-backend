package ro.alexk.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.alexk.backend.entities.PairRequest;

import java.util.List;

public interface PairRequestRepository extends JpaRepository<PairRequest, Integer> {
    boolean existsByMacAddr(String macAddr);
    List<PairRequest> removeById(Integer id);
//    @Modifying
//    @Query("delete from PairRequest pr where pr.id = :id")
//    int deleteByIdAndReturnRowCount(@Param("id") Integer id);
}
