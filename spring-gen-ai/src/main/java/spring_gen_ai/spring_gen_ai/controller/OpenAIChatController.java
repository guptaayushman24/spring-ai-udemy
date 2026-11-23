package spring_gen_ai.spring_gen_ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring_gen_ai.spring_gen_ai.DTO.RequestDTO;

@RestController 
@RequestMapping ("/api/openai/chat")
@RequiredArgsConstructor
@Slf4j 
class OpenAIChatController {
  private final ChatClient chatClient;

  @PostMapping ("/summariz")
  public String summarize (@RequestBody RequestDTO requestDTO) throws Exception{
   try{
     return chatClient.prompt()
    .user(requestDTO.getQuery())
    .call()
    .content();
   }
   catch (Exception e){
    log.error("Exception is ;:::: {}",e.toString());
    throw new Exception("An exception occured");
   }
  }

}