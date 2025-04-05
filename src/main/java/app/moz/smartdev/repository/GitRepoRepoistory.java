package app.moz.smartdev.repository;
import app.moz.smartdev.entity.Branch;
import app.moz.smartdev.entity.Repo;
import app.moz.smartdev.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface GitRepoRepoistory extends JpaRepository<Repo, UUID>{
    boolean existsByRepoName(String repoName);

    Repo findByRepoName(String repoName);
    boolean existsByRepoNameAndUser(String repoName, User user);

    boolean existsByGithubRepoId(int githubRepoId);

    @Query("SELECT r.repoName FROM Repo r")
    List<String> findAllRepoNames();
}
