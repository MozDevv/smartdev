package app.moz.smartdev.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "commits")
public class Commit {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "repo_id", nullable = false)
    private Repo repo;

    private String commitSha;

    private String commitMessage;

    private String commitUrl;

    private String commitAuthor;

    private String commitDate;

    @CreationTimestamp
    private Timestamp createdAt;
}
