package advance_spring_ai.spring_ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import advance_spring_ai.spring_ai.DTO.RequestDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@RestController 
public class SpringAIAdvisorController {
  private final ChatClient chatClient;

  @PostMapping ("/sumarize-advisor")
  public String sumarizeAdvisor (@RequestBody RequestDTO requestDTO){
    return chatClient.prompt()
    .user(requestDTO.getQuery())
    //.advisors(simpleLoggerAdvisor) // Advisor is already implemented in the ChatClient Config class
    .call()
    .content();
  }
}

/*
  Advisor is the interceptor which sits between the application and the llm 
  and it intercept the request when going out from the application to the LLM and when comming out from the LLM and going to the application

  App → Advisor 1 → Advisor 2 → LLM
  App ← Advisor 1 ← Advisor 2 ← LLM

  Pre-request: context, policy ya system instruction jodna
  Post-response: output ko sanitize, validate ya log karna

  Around Advisor
    "Around" matlab model call ke pehle aur baad, dono jagah aapka code chalta hai. Woh:

    1. Request padh ya badal sakta hai
    2. Aage badhne ya wahi rok dene (short-circuit) ka faisla kar sakta hai
    3. Next advisor ya model ko call karta hai
    4. Response padh ya badal sakta hai
    5. Exception handle ya retry kar sakta hai

    Advisor can be added in each controller and in the config also centrally 
    If there are multiple advisor then the first added advisor will be the last one to process the request
    and the first to process the response (Behave similar to the stack fashion) 

    // SimpleLog Advisor :-  Log the whole prompt and the response including 
    the personal data and password so it should be off on the production

    INFO	Application ke normal, important events	"Server started on port 8080"
    DEBUG	Developer ke liye andar ki detail, troubleshooting mein

    SafeGuard Advisor :- It rejects the request to the LLM if there
    are sensitive keywords present in the request and the words can be 
    given in the List when defining the SafeGuard Advisor

    CustomErrorWrappingAdvisor :-  If any sensitive information is sharing then it will not the call the next advisor or model it will reject the request
  
    Custom System Advisor :- Need to create manually same work as the defaultSystem 
    // Why to write the Custom System Advisor :- Need to give the defaultSystem in every controller with the 
    Custom System Advisor we can write the one rule and the same rule can be applied to the other controller

    Can Custom System Advisor and DefaultSystem can be used simultaneously => Yes defaultSystem provided the fixed
    rule Custom System Advisor can be modified dynamically

    // Validation Advisor :- For validating the request and response


    /*
    // Config: hamesha same
        .defaultSystem("You are a summarizer.")

        // Advisor: request ke hisaab se
        String extra = userIsPremium
            ? " Give a detailed summary."
            : " Keep the summary under 50 words.";
     */
