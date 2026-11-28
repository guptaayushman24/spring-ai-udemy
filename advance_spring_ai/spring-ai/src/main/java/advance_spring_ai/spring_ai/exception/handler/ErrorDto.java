package advance_spring_ai.spring_ai.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor 

public class ErrorDto {
  private String code;
  private String message;
}
