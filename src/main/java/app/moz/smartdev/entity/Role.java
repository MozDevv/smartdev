package app.moz.smartdev.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private  String name;

    private  String description;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users ;
}
