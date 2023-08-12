package ro.alexk.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.alexk.backend.entities.DataType;

public interface DataTypeRepository extends JpaRepository<DataType, Integer> {
    @Query("select dt.id from DataType dt where dt.name = :name")
    int getIdByName(@Param("name") String name);
}
