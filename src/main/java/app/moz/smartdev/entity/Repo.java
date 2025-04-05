package app.moz.smartdev.entity;

import app.moz.smartdev.dtos.Owner;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "repositories")
public class Repo {

    @Id
    @GeneratedValue
    @JsonIgnore
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @JsonProperty("name")
    private String repoName;

    @JsonProperty("html_url")
    private String repoUrl;

    @Column(name = "github_repo_id")
    private Integer githubRepoId;



    @JsonProperty("owner")
    @Embedded
    private Owner owner;



    @JsonProperty("description")
    private String description;

    @JsonProperty("private")
    private boolean isPrivate;

    @JsonProperty("language")
    private String language;

    @JsonProperty("default_branch")
    private String defaultBranch;

    @JsonProperty("open_issues_count")
    private int openIssuesCount;

    @JsonProperty("stargazers_count")
    private int stars;

    @JsonProperty("forks_count")
    private int forks;

    @JsonProperty("size")
    private int size;

    @JsonProperty("updated_at")
    private String updatedAt;

    @CreationTimestamp
    private String createdAt;

    @JsonProperty("license")
    @Embedded
    private License license;

    @Data
    @Embeddable
    public static class License {
        @JsonProperty("name")
        private String name;
    }


    private Date lastSync;

    private  Date pushedAt;
}
