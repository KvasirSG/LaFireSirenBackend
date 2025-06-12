package kenn767r.lafiresirenbackend.repository;

import kenn767r.lafiresirenbackend.model.Siren;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SirenRepository extends JpaRepository<Siren, Long> {
}
