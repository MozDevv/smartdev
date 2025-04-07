package app.moz.smartdev.repository;

import app.moz.smartdev.entity.TrelloCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CardRepository extends JpaRepository<TrelloCard, UUID> {
    TrelloCard findByCardId(String cardId);

    List<TrelloCard> findByTrelloListId(UUID listId);

    void deleteByCardId(String cardId);

    boolean existsByCardId(String cardId);

    boolean existsByName(String name);
}
