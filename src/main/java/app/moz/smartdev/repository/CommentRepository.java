package app.moz.smartdev.repository;

import app.moz.smartdev.entity.TrelloComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<TrelloComment, UUID> {
}
