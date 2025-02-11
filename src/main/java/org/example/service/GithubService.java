package org.example.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubService {

  @Value("${github.api.url}")
  private String githubApiUrl;
  @Value("${github.token}")
  private String githubToken;

  public String fetchPrDiff(String repoName, String prNumber) {
    String url = githubApiUrl + "/repos/" + repoName + "/pulls/" + prNumber;

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    System.out.println("test 출력 추가");
    headers.set("Authorization", "token " + githubToken);
    headers.set("Accept", "application/vnd.github.v3.diff");

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

    return response.getBody();
  }

  public void add(int a, int b) {
    System.out.println(a + b);
  }

  public void subtract(int a, int b) {
    System.out.println(a - b);
  }

  public void postCommentToPR(String repoName, String prNumber, String review) {
    String url = githubApiUrl + "/repos/" + repoName + "/issues/" + prNumber + "/comments";

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "token " + githubToken);
    headers.set("Accept", "application/vnd.github.v3+json");

    Map<String, String> request = Map.of("body", review);
    HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
    restTemplate.postForEntity(url, entity, String.class);
  }
}
