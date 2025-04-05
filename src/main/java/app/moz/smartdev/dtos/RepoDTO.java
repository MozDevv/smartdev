package app.moz.smartdev.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepoDTO {

    private UUID id;
    private UUID userId;
    private String repoName;
    private String repoUrl;
    private int githubRepoId;
    private String createdAt;
}