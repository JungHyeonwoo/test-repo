package org.example.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OpenAIService {
  @Value("${openAI.url}")
  private String openaiApiUrl;
  @Value("${openAI.key}")
  private String openaiApiKey;

  public String getCodeReview(String codeDiff) {
    WebClient client = WebClient.builder()
        .baseUrl(openaiApiUrl)
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openaiApiKey)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();

    String prompt = "다음은 GitHub PR에서 변경된 코드입니다. 코드 리뷰를 작성해주세요:\n" + codeDiff;

    return client.post()
        .bodyValue(Map.of("model", "gpt-4", "messages", List.of(Map.of("role", "user", "content", prompt))))
        .retrieve()
        .bodyToMono(Map.class)
        .map(response -> {
          List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
          Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
          return (String) message.get("content");
        })
        .block();

  }
}