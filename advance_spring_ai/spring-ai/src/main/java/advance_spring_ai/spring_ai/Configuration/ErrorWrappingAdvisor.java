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
      // Validation errors are safe to show; anything else stays generic (details are in the log)
      String text = e instanceof IllegalArgumentException
          ? e.getMessage()
          : "Something went wrong. Please try again.";
      var message = new AssistantMessage(text);
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