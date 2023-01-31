package app.foot.service;

import app.foot.exception.NotFoundException;
import app.foot.repository.TeamRepository;
import app.foot.repository.entity.TeamEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TeamService {
  private final TeamRepository repository;

  public TeamEntity getEntityByName(String name) {
    return repository.findByName(name).orElseThrow(() -> new NotFoundException(
        "Team#"+name+" not found."));
  }
}
