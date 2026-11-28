package advance_spring_ai.spring_ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;

import advance_spring_ai.spring_ai.DTO.RequestDTO;
import advance_spring_ai.spring_ai.DTO.SummarizationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.RequestBody;

@RestController 
@RequiredArgsConstructor 
@Slf4j 
public class SummarizeController {
  private static final String SYSTEM_PROMPT = "You are an assistant that summarizes meeting notes into action items and decisions.";
  private final ChatClient chatClient;
  @PostMapping(value = "/summarize-controller-stream",produces =  MediaType.APPLICATION_JSON_VALUE)
 public Flux<String> summarizeMeetingNotes (@RequestBody RequestDTO requestDTO) throws Exception{
    try{
     return chatClient.prompt()
     .system(SYSTEM_PROMPT)
    .user(u -> u.text("Can you summarize the following meeting notes: {meetingNotes}" +
                                " Use the format as described in the following example while doing the summarization:" +
                                " Input: In today's sales strategy meeting, we reviewed Q3 targets and performance gaps. The team agreed to focus on enterprise clients and strengthen partnerships." +
                                " A proposal was made to expand into two new regions. Marketing suggested aligning campaigns with sales objectives to improve lead conversion and shorten sales cycles." +
                                " Output:" +
                                " Action Items:" +
                                "* Focus on enterprise clients and partnerships." +
                                "* Explore expansion into two new regions." +
                                "* Align marketing campaigns with sales objectives." +
                                " Decisions:" +
                                "* Enterprise clients prioritized for Q3." +
                                "* Marketing and sales to work jointly on lead conversion.")
        .param("meetingNotes", requestDTO.getQuery()))
    .stream()
    .content()
    .bufferTimeout(400, Duration.ofMillis(200))
    .map(tokenList->String.join("",tokenList));
   }
   catch (Exception e){
    log.error("Exception is ;:::: {}",e.toString());
    throw new Exception("An exception occured");
   }
  }

  // Structured Output
    @PostMapping(value = "/summarize-controller-structure",produces =  MediaType.APPLICATION_JSON_VALUE)
 public SummarizationResponse summarizeMeetingNotesStructure (@RequestBody RequestDTO requestDTO) throws Exception{
    try{
     return chatClient.prompt()
     .system(SYSTEM_PROMPT)
    .user(u -> u.text("Can you summarize the following meeting notes: {meetingNotes}" +
                                " Use the format as described in the following example while doing the summarization:" +
                                " Input: In today's sales strategy meeting, we reviewed Q3 targets and performance gaps. The team agreed to focus on enterprise clients and strengthen partnerships." +
                                " A proposal was made to expand into two new regions. Marketing suggested aligning campaigns with sales objectives to improve lead conversion and shorten sales cycles." +
                                " Output:" +
                                " Action Items:" +
                                "* Focus on enterprise clients and partnerships." +
                                "* Explore expansion into two new regions." +
                                "* Align marketing campaigns with sales objectives." +
                                " Decisions:" +
                                "* Enterprise clients prioritized for Q3." +
                                "* Marketing and sales to work jointly on lead conversion.")
        .param("meetingNotes", requestDTO.getQuery()))
    .call()
    .entity(SummarizationResponse.class);
   }
   catch (Exception e){
    log.error("Exception is ;:::: {}",e.toString());
    throw new Exception("An exception occured");
   }

  }

  // Getting the different opinion (response) from the llm 

      @PostMapping(value = "/summarize-controller-structure-output-list",produces =  MediaType.APPLICATION_JSON_VALUE)
 public List<SummarizationResponse> summarizeMeetingNotesStructureOutputList (@RequestBody RequestDTO requestDTO) throws Exception{
    try{
      ChatOptions.Builder<?> chatOptions = ChatOptions.builder();
     return chatClient.prompt()
     .options(chatOptions)
     .system(SYSTEM_PROMPT)
    .user(u -> u.text("Can you summarize the following meeting notes: {meetingNotes}" +
                        "Give me 3 different summarize in the same format so that I can chhose from."+
                                " Use the format as described in the following example while doing the summarization:" +
                                " Input: In today's sales strategy meeting, we reviewed Q3 targets and performance gaps. The team agreed to focus on enterprise clients and strengthen partnerships." +
                                " A proposal was made to expand into two new regions. Marketing suggested aligning campaigns with sales objectives to improve lead conversion and shorten sales cycles." +
                                " Output:" +
                                " Action Items:" +
                                "* Focus on enterprise clients and partnerships." +
                                "* Explore expansion into two new regions." +
                                "* Align marketing campaigns with sales objectives." +
                                " Decisions:" +
                                "* Enterprise clients prioritized for Q3." +
                                "* Marketing and sales to work jointly on lead conversion.")
        .param("meetingNotes", requestDTO.getQuery()))
    .call()
    .entity(new ParameterizedTypeReference<List<SummarizationResponse>>() {
      
    });
   }
   catch (Exception e){
    log.error("Exception is ;:::: {}",e.toString());
    throw new Exception("An exception occured");
   }
}

   @PostMapping("/general-chat")
   public String generalChat (@RequestBody RequestDTO requestDTO){
      ChatOptions.Builder<?> chatOption = ChatOptions.builder();
      chatOption.maxTokens(200)
      //.temperature(0.0) // As temperature is 0 there is no randomness in the output we will get same response each time
      .temperature(2.0)
      //.topP(0.1) // Here it will give the same result as 0.1 cummilative probability will be completed at first level and always 1st token will be taken
      .topP(0.9) // Here it will be generate random response because tokens will bve selected more
      .build();

      return chatClient.prompt()
      .options(chatOption)
      .user(requestDTO.getQuery())
      .call()
      .content();
   }

   @PostMapping("/general-chat-stoping-paragraph")
   public String generalChatStoppingParagraph (@RequestBody RequestDTO requestDTO){
      ChatOptions.Builder<?> chatOptions = ChatOptions.builder()
      .maxTokens(1000)
      .stopSequences(List.of("END_OF_PARAGRAPH"));

      return chatClient.prompt()
      .options(chatOptions)
      .user(requestDTO.getQuery())
      .call()
      .content();
   }
   
}


// Chat Options
// Can be implemented at the both place at the Configuration level and at the controller level

// Chat Options features
// 1 => Can provide the model Name 
// 2 => Can give the Max Tokens :- The output tokens/Completions tokens returned from LLM 

// 3 => Temperature setting
// Temperature Rage (0-2) (0=deterministic 1+ = mode random)

// 4 => Top_p :- Controlled randamness using nuclear sampling
         // Restrict the probability mass by considering token whose cumulative probability adds up to a percentage

 // 5 => Top_k :- Consider top-K most likely tokens
         // Higher top-K higer randomness (If top_k = 3) token_1,token_2 and token_3 are taken and rest are removed

   // 6 => Frequency Penalty and Presence Penalty (Specific to Open AI)
   // Rage of both the penalty is -2.0 to +2.0

// 0 => Always picks top token (greedy)
// 0.2-0.5 => Very focused,factual
// 1 -> Balanced generation
// 2 => Chaotic,sometmes incoherent

// When to use :
// Low (0-0.3) -> summaries code legal/medical text
// Medium (0.7) -> balance Q/A chatbots
// High (1.0-2.0) -> brainstorming,creative writing

// Temperature decides how many unique variation of the query you can see 

// Context Window
// All input and output tokens (user+system+tool+additional context provied) (RAG documents etc)
                              // +memory (historixcal prompts) + assistant messages in toal should not exceed the context window in a single request



// Input - token-size <= Context-size-Max-tokens (max model support)