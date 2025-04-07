package app.moz.smartdev.repository;

import app.moz.smartdev.entity.TrelloBoard;
import app.moz.smartdev.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface BoardRepository extends JpaRepository<TrelloBoard, UUID> {
    TrelloBoard findByBoardId(String boardId);

    boolean existsByBoardIdOrName(String boardId, String name);

//    @EntityGraph(attributePaths = {"lists", "lists.cards", "lists.cards.comments"})
    List<TrelloBoard> findByUser(User user);
    boolean existsByBoardId(String boardId);

    void deleteByBoardId(String boardId);
}
