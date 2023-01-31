package app.foot.controller.validator;

import app.foot.controller.rest.Player;
import app.foot.exception.BadRequestException;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;

@Component

public class PlayerUpdateValidator implements Consumer<Player> {
  @Override
  public void accept(Player player) {
    StringBuilder exceptionBuilder = new StringBuilder();
    if (player.getName() == null) {
      exceptionBuilder.append("Name is mandatory. ");
    }
    if (player.getIsGuardian() == null) {
      exceptionBuilder.append("IsGuardian is mandatory. ");
    }
    if (!exceptionBuilder.toString().isEmpty()) {
      throw new BadRequestException(exceptionBuilder.toString());
    }
  }
}
