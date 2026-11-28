package prompt_engineering.prompt.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import prompt_engineering.prompt.DTO.RequestDTO;

@RestController 
@RequiredArgsConstructor 
public class Controller {
  private final ChatClient chatClient;

  @PostMapping ("/prompting-technique")
  public String promptingTechnique (@RequestBody RequestDTO requestDTO){
    return chatClient.prompt()
    .user(requestDTO.getQuery())
    .call()
    .content();
  }
}
