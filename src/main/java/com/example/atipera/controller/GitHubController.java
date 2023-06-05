package com.example.atipera.controller;

import com.example.atipera.service.GitHubService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class GitHubController {

    private final GitHubService gitHubService;

    @GetMapping(value = "/user/{username}/repos")
    public ResponseEntity<?> getUserRepositories(@PathVariable String username, @RequestHeader(HttpHeaders.ACCEPT) String acceptHeader) {
        return new ResponseEntity<>(gitHubService.listUserRepositories(username), HttpStatus.OK);
    }
}
