package spring_gen_ai.spring_gen_ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
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
  private static final String SYSTEM_PROMPT = "You are a helpful assistant that summarize any given content. "
        + "Ensure the summary is concise, informative, and captures the key points. "
        + "Use a friendly and approachable tone while maintaining professionalism.";

  @PostMapping ("/summariz")
  public String summarize (@RequestBody RequestDTO requestDTO) throws Exception{
   try{
     return chatClient.prompt()
     .system(SYSTEM_PROMPT)
    .user(requestDTO.getQuery())
    .call()
    .content();
   }
   catch (Exception e){
    log.error("Exception is ;:::: {}",e.toString());
    throw new Exception("An exception occured");
   }
  }

   @PostMapping ("/summarize-chat-client-response")
  public ChatClientResponse summarizeChatClientResponse (@RequestBody RequestDTO requestDTO) throws Exception{
   try{
     return chatClient.prompt()
     .system(SYSTEM_PROMPT)
    .user(requestDTO.getQuery())
    .call()
    .chatClientResponse();
   }
   catch (Exception e){
    log.error("Exception is ;:::: {}",e.toString());
    throw new Exception("An exception occured");
   }
  }

  @PostMapping("/sumarize-chat-client-meeting-response")
  public String summarizeMeetingNotes (@RequestBody RequestDTO requestDTO) throws Exception{
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
    .content();
   }
   catch (Exception e){
    log.error("Exception is ;:::: {}",e.toString());
    throw new Exception("An exception occured");
   }
  }

}


/*Returning a String

.call().content() gives you only the model's reply text. The client gets plain text, which suits a simple summarize endpoint. You can't see how many tokens it used, which model answered, or why generation stopped.

Returning ChatClientResponse

.call().chatClientResponse() gives you the full response object, which has two parts:

chatResponse(): the reply text, plus metadata. That includes token usage (prompt, completion and total), the model name, the finish reason and the response ID.
context(): a map that advisors write to, such as RAG documents or memory info.
Returning this from a controller sends all of that as JSON, so the client has to dig the text out of result.output.text. The context map can also contain objects that don't serialize cleanly.

When to use which

Use content() when you only need the answer, as in a REST API response.
Use chatClientResponse() when you need metadata or advisor context, for example to log token usage. In that case, read what you need inside the controller and return a small DTO instead of the raw object.
If you only need metadata and no advisor context, .chatResponse() is lighter than .chatClientResponse() */

/*promptTokens (115): the tokens in everything you sent to the model. That's the system prompt, the user's text, and any extra context such as chat history or RAG documents. Here it's mostly SYSTEM_PROMPT plus the text to summarize.
completionTokens (66): the tokens in the model's reply, which is the summary it generated.
totalTokens (181): prompt plus completion. This is the figure that hosted providers like OpenAI bill on, usually at different per-token rates for input and output. */


/*
  ChatClient -> Perform stateless request to an using a fluent API
  ChatModel -> Perform the model 
*/

/*
  ChatClient Interface -> Default ChatClientImplementation 
  // ChatClientResponseSpec+CallResponseSpec
*/

/*
  ChatModel Interface -> The implemetation of ChatModel provided different implementation per AI model
*/

/*
  call () => call the AI model
 */

  /*
    Advisor architecture => Flexible,modular way to intercpt,modify or extend the behaviour of chat request and response
  */


/*Call	Purpose
chatClient.prompt()	Starts a new request and returns a ChatClientRequestSpec, a builder for one call. It holds the system text, user text, params, options and advisors until you send it. Each call to prompt() is a fresh, independent request.
.system(...)	Sets the instructions that shape the model's behaviour for this request.
.user(...)	Sets the user's message. With the lambda form (u -> u.text(...).param(...)) it fills {placeholders}.
.call()	Sends the request to the model and waits for the full reply. The alternative is .stream(), which returns tokens as they arrive.
.chatClientResponse()	Chooses what to read from the reply. It returns the full response object.
What .chatClientResponse() returns

It returns a ChatClientResponse, which has two parts:

chatResponse(): the reply text plus metadata (token usage, model name, finish reason, response ID).
context(): a map that advisors write to, such as RAG documents or memory info.
The other ways to finish the chain are:

.content() returns only the reply text, as a String.
.chatResponse() returns the reply plus metadata, without the advisor context. It's lighter than .chatClientResponse().
.entity(MyClass.class) returns the reply parsed into a Java object */