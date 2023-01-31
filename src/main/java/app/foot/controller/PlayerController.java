package app.foot.controller;

import app.foot.controller.rest.Player;
import app.foot.controller.rest.mapper.PlayerRestMapper;
import app.foot.controller.validator.PlayerUpdateValidator;
import app.foot.service.PlayerService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PlayerController {
  private final PlayerRestMapper mapper;
  private final PlayerService service;
  private final PlayerUpdateValidator updateValidator;

  @GetMapping("/players")
  public List<Player> getPlayers() {
    return service.getPlayers().stream()
        .map(mapper::toRest)
        .collect(Collectors.toUnmodifiableList());
  }

  @PostMapping("/players")
  public List<Player> addPlayers(@RequestBody List<Player> toCreate) {
    List<app.foot.model.Player> domain = toCreate.stream()
        .map(mapper::toDomain)
        .collect(Collectors.toUnmodifiableList());
    return service.createPlayers(domain).stream()
        .map(mapper::toRest)
        .collect(Collectors.toUnmodifiableList());
  }

  @PutMapping("/players/{id}")
  public Player updatePlayer(@RequestBody Player toUpdate, @PathVariable int id) {
    updateValidator.accept(toUpdate);
    return mapper.toRest(service.updatePlayer(mapper.toDomain(toUpdate), id));
  }
}
