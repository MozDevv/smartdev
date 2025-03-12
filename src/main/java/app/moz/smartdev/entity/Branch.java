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
@Table(name = "branches")
public class Branch {

    @Id
    @GeneratedValue
    private UUID id;

    private String branchName;

    private boolean isDefault = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "repo_id", nullable = false)
    private Repo repo;

    @CreationTimestamp
    private Timestamp createdAt;
}
