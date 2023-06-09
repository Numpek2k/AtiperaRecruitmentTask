package com.example.atipera.controller;

import com.example.atipera.model.GitRepository;
import com.example.atipera.service.GitHubService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class GitHubController {

    private final GitHubService gitHubService;

    @GetMapping(value = "/user/{username}/repos")
    public ResponseEntity<Mono<List<GitRepository>>> getUserRepositories(@PathVariable String username) {
        return new ResponseEntity<>(gitHubService.listUserRepositories(username), HttpStatus.OK);
    }
}
