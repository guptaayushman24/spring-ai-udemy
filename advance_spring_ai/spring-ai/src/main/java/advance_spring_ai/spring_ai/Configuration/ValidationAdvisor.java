package advance_spring_ai.spring_ai.Configuration;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Component 
public class ValidationAdvisor implements CallAdvisor{
  public Integer MAX_TOKENS = 10;
  private void validateInput(ChatClientRequest chatClientRequest){
    String input = chatClientRequest.prompt().getContents();
    if (input.length()>MAX_TOKENS){
      throw new IllegalArgumentException("Prompt too long: exceeds "+MAX_TOKENS+" characters!");
    }
  }

    private void validateOuput (ChatClientResponse chatClientResponse){
      ChatResponse chatResponse = chatClientResponse.chatResponse();
      if (chatResponse==null){
        throw new IllegalArgumentException("LLM returned a null result!");
      }

      String output = chatResponse.getResult().getOutput().getText();

      if (output==null || output.trim().isEmpty()){
        throw new IllegalArgumentException("LLM returned an empty resource");
      }
    }

  @Override
  public String getName() {
    return "Validation Advisor";
  }

  @Override
  public int getOrder() {
    return 0;
  }

  @Override
  public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
    validateInput (chatClientRequest);
    ChatClientResponse chatClientResponse =  callAdvisorChain.nextCall(chatClientRequest);
    validateOuput(chatClientResponse);
    
    return chatClientResponse;
  }
  
}
