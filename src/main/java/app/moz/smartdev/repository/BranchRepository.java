package app.moz.smartdev.repository;

import app.moz.smartdev.entity.Branch;
import app.moz.smartdev.entity.Repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BranchRepository extends JpaRepository<Branch, UUID> {

    boolean existsByBranchNameAndRepo(String branchName, Repo repo);
    Branch findByBranchNameAndRepo(String branchName, Repo repo);


    @Query("SELECT b.branchName FROM Branch b WHERE b.repo = :repo")
    List<String> findAllBranchNamesByRepo(Repo repo);

}
