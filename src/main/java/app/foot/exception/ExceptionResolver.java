package app.foot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionResolver {
  @ExceptionHandler({NotFoundException.class})
  public ResponseEntity<NotFoundException> handleNotFoundException(NotFoundException e) {
    return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({BadRequestException.class})
  public ResponseEntity<BadRequestException> handleBadRequestException(BadRequestException e) {
    return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({InternalServerException.class})
  public ResponseEntity<InternalServerException> handleInternalServerException(
      InternalServerException e) {
    return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
