package hasebo.scrumpoker.repository;

import hasebo.scrumpoker.model.Card;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends CrudRepository <Card, Long> {


    Optional<Card> findById(Long cardId);

    List<Card> findAll();
//    Optional<List<Card>> findAll();

}
