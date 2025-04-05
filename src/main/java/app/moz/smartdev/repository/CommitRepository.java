package app.moz.smartdev.repository;

import app.moz.smartdev.entity.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CommitRepository extends JpaRepository<Commit, UUID> {

    boolean existsByCommitSha(String commitSha);

    @Query("SELECT c.commitSha FROM Commit c")
    List<String> findAllCommitShas();
}
