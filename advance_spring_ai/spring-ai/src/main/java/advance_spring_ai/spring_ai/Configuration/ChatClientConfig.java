package advance_spring_ai.spring_ai.Configuration;


import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 
public class ChatClientConfig {
  @Bean 
  ChatClient openAIChatClient (OpenAiChatModel openAiChatModel, SimpleLoggerAdvisor simpleLoggerAdvisor,SafeGuardAdvisor safeGuardAdvisor,ErrorWrappingAdvisor errorWrappingAdvisor,SystemPromptAdvisor systemPromptAdvisor,ValidationAdvisor validationAdvisor){
    return ChatClient.builder(openAiChatModel)
    .defaultAdvisors(errorWrappingAdvisor,safeGuardAdvisor,simpleLoggerAdvisor,systemPromptAdvisor,validationAdvisor)
    .build();
  }

  @Bean 
  SimpleLoggerAdvisor simpleLoggerAdvisor(){
    return SimpleLoggerAdvisor
    .builder()
    .order(1)
    .build();
  }

  @Bean
  SafeGuardAdvisor safeGuardAdvisor(){
    return SafeGuardAdvisor
                .builder()
                .sensitiveWords(List.of(
                "password", "ssn", "credit card", "iban", "bank account",
                "api_key", "secret", "private_key", "token",
                "confidential", "classified", "internal only", "Ignore previous instructions",
                "Ignore instructions", "system prompt", "hack"))
                .order(0)
                .build();
  }
}
