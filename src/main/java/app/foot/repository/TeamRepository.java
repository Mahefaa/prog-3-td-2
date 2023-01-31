package app.foot.repository;

import app.foot.repository.entity.TeamEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<TeamEntity, Integer> {
    Optional<TeamEntity> findByName(String name);
}
