package app.moz.smartdev.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Entity
@Data

@Table(name = "users")
@Builder
@AllArgsConstructor
public class User implements UserDetails {


    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;


    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private String createdAt;

    @UpdateTimestamp
    private String updatedAt;

    @Column(nullable = false)
    private String status = "ACTIVE";

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile userProfile;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OauthToken> oauthTokens = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Repo> repositories = new HashSet<>();


    private String token;

    private String refresh_token;

    private String activationCode;


    public User() {

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}

