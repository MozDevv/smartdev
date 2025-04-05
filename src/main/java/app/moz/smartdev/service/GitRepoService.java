package app.moz.smartdev.service;

import app.moz.smartdev.dtos.Owner;
import app.moz.smartdev.entity.*;
import app.moz.smartdev.entity.CustomUserDetails;
import app.moz.smartdev.repository.BranchRepository;
import app.moz.smartdev.repository.CommitRepository;
import app.moz.smartdev.repository.GitRepoRepoistory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@AllArgsConstructor
public class GitRepoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final GitRepoRepoistory gitRepoRepoistory;
    private final BranchRepository branchRepository;
    private final CommitRepository commitRepository;

    @Async
    public CompletableFuture<Void> fetchUserGitHubDataAsync(String accessToken, User user) {

        try {
            log.info("Fetching GitHub repositories for user: {}", user.getUsername());

            List<Repo> repositories = getUserRepositories(accessToken, user);
            log.info("Got {} repositories", repositories.size());

//            for (Repo repo : repositories) {
//                log.info("Repository: {}", repo.getRepoName());
//            }
//
//            for (Repo repo : repositories) {
//                log.info("Fetching branches for repo: {}", repo.getRepoName());
//
//                List<Branch> branches = getBranches(accessToken, repo.getRepoName(), repo.getOwner().getLogin());
//                if(branches.isEmpty()) {
//                    log.warn("No branches found for repository: {}", repo.getRepoName());
//                    continue;
//                } else {
//                    log.info("Got {} branches", branches.size());
//                }
//                for (Branch branch : branches) {
//                    branch.setRepo(repo); // Set the correct Repo object
//                    log.info("Branch: {}", branch.getBranchName());
//                }
//                branchRepository.saveAll(branches); // Save branches to the repository
//
//                for (Branch branch : branches) {
//                    log.info("Fetching commits for branch: {} in repo: {}", branch.getBranchName(), repo.getRepoName());
//
//                    List<Commit> commits = getCommits(accessToken, repo.getRepoName(), branch.getBranchName(), user);
//                    for (Commit commit : commits) {
//                        log.info("Commit: {}", commit.getCommitMessage());
//                    }
//                }
//            }

            log.info("GitHub data fetched successfully for user: {}", user.getUsername());
        } catch (Exception e) {
            log.error("Error fetching GitHub data: {}", e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }


    private List<Repo> getUserRepositories(String accessToken, User user) {
        String url = "https://api.github.com/user/repos";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        log.info("Response: {}", response.getBody());

        List<Repo> repos = new ArrayList<>();
        if (response.getBody() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            try {
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                List<Repo> reposToSave = new ArrayList<>();
                List<Repo> allRepos = new ArrayList<>();

                for (JsonNode repoNode : rootNode) {
                    Repo repo = objectMapper.treeToValue(repoNode, Repo.class);
                    repo.setUser(user);

                    String ownerLogin = repoNode.get("owner").get("login").asText();
                    repo.getOwner().setLogin(ownerLogin);
//                    log.info("Owner: {}", ownerLogin);
//                    log.info("Repository ID: {}", repo.getGithubRepoId());
//                    log.info("Repository Name: {}", repo.getRepoName());
//                    log.info("Repository URL: {}", repo.getRepoUrl());
//                    log.info("Repository Owner: {}", repo.getOwner());

                    if (repo.getRepoName() != null) {
                        allRepos.add(repo);
                        if (!gitRepoRepoistory.existsByRepoName(repo.getRepoName())) {
                            reposToSave.add(repo);
                        } else {
//                            log.info("Repository already exists in the database, skipping: {}", repo.getRepoName());
                        }
                    } else {
                        log.warn("Repository name is null, skipping.");
                    }
                }

                if (!reposToSave.isEmpty()) {
                    gitRepoRepoistory.saveAll(reposToSave);
                }

                Set<String> existingCommitShas = new HashSet<>(commitRepository.findAllCommitShas());

                for (Repo repo : allRepos) {
                    // Save the repo if it is not already saved
                    if (!gitRepoRepoistory.existsByRepoName(repo.getRepoName())) {
                        gitRepoRepoistory.save(repo);

                    }
                    Repo existingRepo = gitRepoRepoistory.findByRepoName(repo.getRepoName());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date repoUpdatedAt = dateFormat.parse(repo.getUpdatedAt());

                    if (existingRepo != null && existingRepo.getLastSync() != null && existingRepo.getLastSync().after(repoUpdatedAt)) {

                        log.info("Repository {} is up-to-date, skipping branches and commits", repo.getRepoName());
                        continue;
                    } else {
                        log.info("Repository {} is not up-to-date, fetching branches and commits", repo.getRepoName());
                    }

                    List<Branch> branches = getBranches(accessToken, repo.getRepoName(), repo.getOwner().getLogin());
                    if (!branches.isEmpty()) {
                        log.warn("Branches are null for repository: lower{}", repo.getRepoName());
                    } else {
                        log.info("Got {} branches lower", branches.size());
                    }
                    if (!branches.isEmpty()) {
                        List<Branch> branchesToSave = new ArrayList<>();
                        for (Branch branch : branches) {
                            log.info("Branch fethced: {}", branch.getBranchName());

                            List<Commit> commits = getCommits(accessToken, repo.getRepoName(), branch.getBranchName(), repo.getOwner(), user);
                            List<Commit> commitsToSave = new ArrayList<>();

                            Repo savedRepo = gitRepoRepoistory.findByRepoName(repo.getRepoName());
                            Branch savedBranch = branchRepository.findByBranchNameAndRepo(branch.getBranchName(), savedRepo);
                            if (!branchRepository.existsByBranchNameAndRepo(branch.getBranchName(), savedRepo)) {
                                //get the branch from the d

                                branch.setRepo(savedRepo);
                                branchesToSave.add(branch);
                                //get the saved branch from db

                                // Fetch all existing commit SHAs from the database
                                for (Commit commit : commits) {
                                    if (!existingCommitShas.contains(commit.getCommitSha())) {
                                        commit.setBranch(savedBranch); // Set the correct Branch object
                                        commitsToSave.add(commit);
                                    } else {
                                        log.info("Commit already exists in the database, skipping: {}", commit.getCommitSha());
                                    }
                                }
                                log.info("Got {} commits saving branch", commitsToSave.size());
                                commitRepository.saveAll(commitsToSave); // Save commits to the repositorycommitsToSave.size());

                            } else {
                                log.info("Branch already exists in the database, skipping: and starting commits {}", branch.getBranchName());

                                for (Commit commit : commits) {
                                    if (!existingCommitShas.contains(commit.getCommitSha())) {
                                        commit.setBranch(savedBranch); // Set the correct Branch object
                                        commitsToSave.add(commit);
                                    } else {
                                        log.info("Commit already exists in the database, skipping: {}", commit.getCommitSha());
                                    }
                                }
                                log.info("Got {} commits saving branch", commitsToSave.size());
                                commitRepository.saveAll(commitsToSave);
                            }
                            Date lastSync = Date.from(Instant.now().atZone(ZoneId.of("UTC")).toInstant());
                            log.info("Setting last sync time for repository: {}", lastSync);
                            existingRepo.setLastSync(lastSync);
                            existingRepo.setUpdatedAt(repo.getUpdatedAt());
                            existingRepo.setPushedAt(repo.getPushedAt());
                            gitRepoRepoistory.save(existingRepo);
                        }
                        branchRepository.saveAll(branchesToSave); // Save branches to the repository
                    } else {
                        log.warn("No branches found for repository: {}", repo.getRepoName());
                    }
                }
            } catch (Exception e) {
                log.error("Error parsing JSON response: {}", e.getMessage());
            }
        }
        return repos;
    }

    private List<Branch> getBranches(String accessToken, String repoName, String githubUsername) {
        String url = String.format("https://api.github.com/repos/%s/%s/branches", githubUsername, repoName);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Branch[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Branch[].class);
        List<Branch> branches = new ArrayList<>();
        if (response.getBody() == null) {
            log.warn("Branches are null for repository Upper method: {}", repoName);
            return branches;
        } else {
            log.info("Got {} branches upper", response.getBody().length);
        }
        Collections.addAll(branches, response.getBody());
        return branches;
    }

    private List<Commit> getCommits(String accessToken, String repoName, String branchName, Owner owner, User user) {
        String url = String.format("https://api.github.com/repos/%s/%s/commits?sha=%s", owner.getLogin(), repoName, branchName);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<JsonNode[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode[].class);
            List<Commit> commits = new ArrayList<>();
            for (JsonNode commitNode : response.getBody()) {
                Commit commit = new Commit();
                commit.setCommitSha(commitNode.get("sha").asText());
                commit.setCommitMessage(commitNode.get("commit").get("message").asText());
                commit.setCommitUrl(commitNode.get("html_url").asText());
                commit.setCommitAuthor(commitNode.get("commit").get("author").get("name").asText());
                commit.setCommitDate(commitNode.get("commit").get("author").get("date").asText());
                commits.add(commit);
            }
            return commits;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.error("Error fetching commits: {}", e.getMessage());
            } else {
                log.error("Unexpected error fetching commits: {}", e.getMessage());
            }
            return Collections.emptyList();
        }
    }
}