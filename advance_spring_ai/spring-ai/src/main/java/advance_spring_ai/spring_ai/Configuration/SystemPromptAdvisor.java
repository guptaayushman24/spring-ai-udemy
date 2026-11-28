package advance_spring_ai.spring_ai.Configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Component 
public class SystemPromptAdvisor implements CallAdvisor{
  public ChatClientRequest updateSystemMessage (ChatClientRequest chatClientRequest){
    List<Message> existingMesages =  new ArrayList<>(chatClientRequest.prompt().getUserMessages());
    SystemMessage existingSystemMessage = chatClientRequest.prompt().getSystemMessage();

      existingMesages.add(new SystemMessage("You are a summarizer for a given text or content." +
                " Do not answer anything other than the summarization." +
                " If the question is not about summarization, " +
                " respond with 'I can only help with summarization tasks." +
                " Never provide any other information. " + existingSystemMessage));

      log.info("Exising system message is ",existingMesages);

        chatClientRequest = chatClientRequest.mutate()
        .prompt(Prompt.builder()
        .messages(existingMesages)
        .build()).build();

        String systemMessage = chatClientRequest.prompt().getSystemMessage().getText();
        log.info("System Message is updated as : {}",systemMessage);
      
    return chatClientRequest;
  }

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return "SystemPromptAdvisor";
  }

  @Override
  public int getOrder() {
    return 0;
  }

  @Override
  public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
    // TODO Auto-generated method stub
     chatClientRequest = updateSystemMessage (chatClientRequest);
     return callAdvisorChain.nextCall(chatClientRequest);
  }
  
}
