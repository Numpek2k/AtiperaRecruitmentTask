package com.example.atipera.service;

import com.example.atipera.model.Branch;
import com.example.atipera.model.Commit;
import com.example.atipera.model.GitRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class GitHubService {
    private final String gitHubBaseUrl = "https://api.github.com/";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<GitRepository> listUserRepositories(String username){
        String url = gitHubBaseUrl + "users/" + username + "/repos";
        ResponseEntity<GitRepository[]> responseEntity = restTemplate.getForEntity(url, GitRepository[].class);
        GitRepository[] repos = responseEntity.getBody();
        if (repos != null) {
            List<GitRepository> reposList = Arrays.stream(repos)
                    .filter(repo -> !repo.isFork())
                    .toList();
            reposList.forEach(repo -> repo.setBranches(getBranches(repo.getOwner().getLogin(), repo.getName())));
            return reposList;
        }
        throw new ArrayStoreException("Empty array");
    }

    private List<Branch> getBranches(String ownerLogin, String repoName) {
        List<Branch> branchesList = new ArrayList<>();
        String url = gitHubBaseUrl + "repos/" + ownerLogin + "/" + repoName + "/branches";
        Branch[] branches = restTemplate.getForEntity(url, Branch[].class).getBody();

        assert branches != null;

        for (Branch branch : branches) {
            String commitUrl = gitHubBaseUrl + "/repos/" + ownerLogin + "/" + repoName + "/commits?sha=" + branch.getName();
            ResponseEntity<Commit[]> responseCommits = restTemplate.getForEntity(commitUrl, Commit[].class);
            if (Objects.requireNonNull(responseCommits.getBody()).length > 0) {
                Commit commit = responseCommits.getBody()[0];
                branch.setCommit(commit);
            }
            branchesList.add(branch);
        }
        return branchesList;
    }
}
