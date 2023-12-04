package ro.alexk.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.alexk.backend.entities.PairRequest;
import ro.alexk.backend.models.db.projections.PairRequestProj;

import java.util.List;

public interface PairRequestRepository extends JpaRepository<PairRequest, Integer> {
    boolean existsByMacAddr(String macAddr);
    List<PairRequest> removeById(Integer id);
    @Query("select pr.id as id, pr.blueprint.id as blueprintId, pr.macAddr as macAddr, pr.date as date from PairRequest pr")
    List<PairRequestProj> getAll();
//    @Modifying
//    @Query("delete from PairRequest pr where pr.id = :id")
//    int deleteByIdAndReturnRowCount(@Param("id") Integer id);
}
