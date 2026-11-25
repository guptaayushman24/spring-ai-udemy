package advance_spring_ai.spring_ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import advance_spring_ai.spring_ai.DTO.RequestDTO;

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
  @PostMapping(value = "summarize-controller-stream",produces =  MediaType.APPLICATION_JSON_VALUE)
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
    .content();
   }
   catch (Exception e){
    log.error("Exception is ;:::: {}",e.toString());
    throw new Exception("An exception occured");
   }
  }
  }
