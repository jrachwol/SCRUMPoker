package hasebo.scrumpoker.repository;

import hasebo.scrumpoker.model.Voting;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface VotingRepository extends CrudRepository<Voting, Long> {

    @Override
    Optional<Voting> findById(Long aLong);
}
