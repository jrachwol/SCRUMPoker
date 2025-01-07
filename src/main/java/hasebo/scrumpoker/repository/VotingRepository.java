package hasebo.scrumpoker.repository;

import hasebo.scrumpoker.model.Voting;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingRepository extends CrudRepository<Voting, Long> {

    @Override
    Optional<Voting> findById(Long aLong);
}
