package kenn767r.lafiresirenbackend.repository;

import kenn767r.lafiresirenbackend.model.Fire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface FireRepository extends JpaRepository<Fire, Long> {
    List<Fire> findByActiveTrue();

}
