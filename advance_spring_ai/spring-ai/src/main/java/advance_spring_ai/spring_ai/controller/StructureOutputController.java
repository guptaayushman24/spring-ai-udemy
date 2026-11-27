package advance_spring_ai.spring_ai.controller;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import advance_spring_ai.spring_ai.DTO.RequestDTO;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor 
@RequestMapping ("/chat/openai")
public class StructureOutputController {
  private final ChatClient chatClient;

  @PostMapping ("/list-structure")
  public List<String> structuredOutputList (@RequestBody RequestDTO requestDTO){

    // Override the model of the which is defined in the application.yaml
    ChatOptions.Builder <?> chatOption = ChatOptions.builder();
    
    chatOption.model("llama3.2:latest")
    .maxTokens(100)
    .build();

    return chatClient.prompt()
    .options(chatOption)
    .user(requestDTO.getQuery())
    .call()
    .entity(new ListOutputConverter());
  }

  @PostMapping("/map-structure")
  public Map<String,Object> struturedOutputMap (@RequestBody  RequestDTO requestDTO){
    return chatClient.prompt()
    .user(requestDTO.getQuery())
    .call()
    .entity(new MapOutputConverter());
  }
}
