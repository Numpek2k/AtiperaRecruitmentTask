package com.example.atipera.service;

import com.example.atipera.model.Branch;
import com.example.atipera.model.Commit;
import com.example.atipera.model.GitRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GitHubService {
    private final String gitHubBaseUrl = "https://api.github.com/";
    private final WebClient webClient;

    public Mono<List<GitRepository>> listUserRepositories(String username){
        String url = gitHubBaseUrl + "users/" + username + "/repos";
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToFlux(GitRepository.class)
                .filter(repo -> !repo.fork())
                .flatMap(repo -> getBranches(repo.owner().login(), repo.name())
                        .collectList()
                        .map(branches -> new GitRepository(
                                repo.name(),
                                repo.owner(),
                                branches,
                                repo.fork())))
                .collectList();
    }

    private Flux<Branch> getBranches(String ownerLogin, String repoName) {
        String url = gitHubBaseUrl + "repos/" + ownerLogin + "/" + repoName + "/branches";
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Branch.class)
                .flatMap(branch -> getCommit(ownerLogin, repoName, branch.name())
                        .map(commit -> new Branch(
                                branch.name(),
                                commit)));
    }

    private Mono<Commit> getCommit(String ownerLogin, String repoName, String branchName){
        String commitUrl = gitHubBaseUrl + "/repos/" + ownerLogin + "/" + repoName + "/commits?sha=" + branchName;
        return webClient.get()
                .uri(commitUrl)
                .retrieve()
                .bodyToFlux(Commit.class)
                .next();
    }

    public GitHubService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(gitHubBaseUrl).build();
    }

}
