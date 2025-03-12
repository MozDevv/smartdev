package app.moz.smartdev.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pull_requests")
public class PullRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repo_id", nullable = false)
    private Repo repository;

    @Column(nullable = false)
    private int prNumber;

    @Column(length = 255)
    private String title;

    @Column(length = 255)
    private String author;

    @Column(length = 50)
    private String state;

    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}