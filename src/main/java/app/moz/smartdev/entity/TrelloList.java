package app.moz.smartdev.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trello_lists")
public class TrelloList {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String listId;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @JsonIgnore
    private TrelloBoard board;

    @OneToMany(mappedBy = "trelloList", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrelloCard> cards;
}