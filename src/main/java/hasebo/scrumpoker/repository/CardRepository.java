package hasebo.scrumpoker.repository;

import hasebo.scrumpoker.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {


    Optional<Card> findById(Long cardId);

    List<Card> findAll();

}
