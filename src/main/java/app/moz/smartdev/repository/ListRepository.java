package app.moz.smartdev.repository;

import app.moz.smartdev.entity.TrelloBoard;
import app.moz.smartdev.entity.TrelloList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface ListRepository extends JpaRepository<TrelloList, UUID> {
    TrelloList findByListId(String listId);

    boolean existsByListIdOrName(
            String listId,
            String name
    );

    List<TrelloList> findByBoardId(UUID board);

    void deleteByListId(String listId);

    boolean existsByListId(String listId);
}
