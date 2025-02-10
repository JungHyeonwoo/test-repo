package org.example.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.service.GithubService;
import org.example.service.OpenAIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webhook")
public class WebhookController {

  private GithubService githubService;
  private OpenAIService openAIService;

  @PostMapping
  public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload) {
    String action = (String) payload.get("action");

    if ("opened".equals(action) || "synchronize".equals(action)) {
      Map<String, Object> pullRequest = (Map<String, Object>) payload.get("pull_request");
      String prUrl = (String) pullRequest.get("html_url");
      String repoName = (String) ((Map<String, Object>) payload.get("repository")).get("full_name");

      System.out.println("PR 이벤트 감지: " + prUrl);

      String diff = githubService.fetchPrDiff(repoName, prUrl);

      String review = openAIService.getCodeReview(diff);

      githubService.postCommentToPR(repoName, prUrl, review);
    }

    return ResponseEntity.ok("Webhook received");
  }

  public void setGithubService(GithubService githubService) {
    this.githubService = githubService;
  }

  public void setOpenAIService(OpenAIService openAIService) {
    this.openAIService = openAIService;
  }
}