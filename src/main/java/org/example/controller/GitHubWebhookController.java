package org.example.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/github")
public class GitHubWebhookController {

  @PostMapping("/webhook")
  public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload) {
    System.out.println("🔔 GitHub Webhook Received!");
    System.out.println(payload);

    return ResponseEntity.ok("Webhook received!");
  }
}