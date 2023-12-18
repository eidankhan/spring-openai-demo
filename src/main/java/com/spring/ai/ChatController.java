package com.spring.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class ChatController {

    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;


    @Value("${openai.api.key}")
    private String OPENAI_API_KEY;

    private WebClient webClient;

    @Autowired
    private ChatController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }


    @GetMapping("/chat")
    public String chat(@RequestParam String prompt) {
        // create a request
        ChatRequest request = new ChatRequest(model, prompt);

        // call the API
        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "No response";
        }

        // return the first response
        return response.getChoices().get(0).getMessage().getContent();
        // return generateOpenAIText(prompt);
    }

    private String generateOpenAIText(String prompt) {
        // create a request
        ChatRequest request = new ChatRequest(model, prompt);

        return webClient
                .post()
                .uri(apiUrl)
                .header("Authorization", "Bearer "+OPENAI_API_KEY)
                .bodyValue("{\"prompt\": \"" + prompt + "\", \"model\": \"" + model + "\"}")
                .retrieve()
                .bodyToMono(String.class)
                .block(); // block() for simplicity; handle this differently in a reactive application

    }
}