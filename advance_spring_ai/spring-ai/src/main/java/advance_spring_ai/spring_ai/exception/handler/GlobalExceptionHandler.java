package advance_spring_ai.spring_ai.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RestControllerAdvice 
public class GlobalExceptionHandler {
  @ResponseBody 
  @ExceptionHandler (value = {Exception.class})
  public ErrorDto handleException (Exception exception){
    log.error("Exception occured : {}",exception.getMessage(),exception);

    return new ErrorDto(HttpStatus.BAD_REQUEST.getReasonPhrase(),exception.getMessage());
  }
}
