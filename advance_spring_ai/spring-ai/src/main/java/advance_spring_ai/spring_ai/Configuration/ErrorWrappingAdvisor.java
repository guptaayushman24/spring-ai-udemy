package advance_spring_ai.spring_ai.Configuration;

import java.util.List;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component 
public class ErrorWrappingAdvisor implements CallAdvisor {

  @Override
  public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
    try {
      return chain.nextCall(request);
    } catch (Exception e) {
      log.error("LLM call failed", e);
      var message = new AssistantMessage("Please do not ask and share any sensitive information in the query");
      return ChatClientResponse.builder()
          .chatResponse(new ChatResponse(List.of(new Generation(message))))
          .context(request.context())
          .build();
    }
  }

  @Override
  public String getName() { return "ErrorWrappingAdvisor"; }

  @Override
  public int getOrder() { return -1; }   // SafeGuard (0) se bhi pehle
}