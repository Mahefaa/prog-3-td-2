package app.foot.service;

import app.foot.exception.NotFoundException;
import app.foot.model.Player;
import app.foot.repository.PlayerRepository;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.mapper.PlayerMapper;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PlayerService {
  private final PlayerRepository repository;
  private final PlayerMapper mapper;

  public List<Player> getPlayers() {
    return repository.findAll().stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }

  public List<Player> createPlayers(List<Player> toCreate) {
    return repository.saveAll(toCreate.stream()
            .map(mapper::toEntity)
            .collect(Collectors.toUnmodifiableList())).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
  }

  @Transactional
  public Player updatePlayer(Player toUpdate, int id) {
    PlayerEntity entity = getPlayerEntityById(id);
    entity.setName(toUpdate.getName());
    entity.setGuardian(toUpdate.getIsGuardian());
    return mapper.toDomain(repository.save(entity));
  }

  public PlayerEntity getPlayerEntityById(int id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException("Player#" + id + " not"
        + " found."));
  }

  public Player getPlayerById(int id) {
    return mapper.toDomain(getPlayerEntityById(id));
  }
}
