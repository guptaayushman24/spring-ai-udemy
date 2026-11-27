package advance_spring_ai.spring_ai.DTO;

import java.util.List;

import lombok.Data;

@Data 
public class SummarizationResponse {
  List<String> actionItems;
  List<String> decisions;
  String errorMessage;
  
}
