package uk.ac.bristol.cs.application.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import uk.ac.bristol.cs.application.model.County;

public interface CountyRepository extends JpaRepository<County, String> {
@Query("FROM County c JOIN FETCH c.parent JOIN FETCH c.parent.parent")
    List<County> fetchAllFull();
}