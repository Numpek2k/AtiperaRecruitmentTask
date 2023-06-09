package com.example.atipera.service;

import com.example.atipera.model.GitRepository;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GitHubServiceTest {
    private MockWebServer mockWebServer;
    private GitHubService gitHubService;

    @BeforeEach
    public void setup() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();
        WebClient.Builder webClientBuilder = WebClient.builder().baseUrl(mockWebServer.url("/").toString());
        this.gitHubService = new GitHubService(webClientBuilder);
    }

    @Test
    public void listUserRepositoriesIntegrationTest() {
        String testUser = "Numpek2k";

        // Define the mock response
        mockWebServer.enqueue(
                new MockResponse()
                        .setBody("[{\"name\":\"testRepo\",\"owner\":{\"login\":\"" + testUser + "\"},\"branches\":[],\"fork\":false}]")
                        .addHeader("Content-Type", "application/json"));

        // Execute the service method
        Mono<List<GitRepository>> result = gitHubService.listUserRepositories(testUser);

        // Validate the response
        StepVerifier.create(result)
                .consumeNextWith(repositories -> {
                    assertEquals(3, repositories.size());
                    assertTrue(repositories.stream().anyMatch(repo -> testUser.equals(repo.owner().login())));
                    assertTrue(repositories.stream().anyMatch(repo -> "AtiperaRecruitmentTask".equals(repo.name())));
                    assertFalse(repositories.get(0).fork());
                })
                .verifyComplete();
    }

    @AfterEach
    public void tearDown() throws IOException {
        this.mockWebServer.shutdown();
    }
}
