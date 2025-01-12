package hasebo.scrumpoker.repository;

import hasebo.scrumpoker.model.Voting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingRepository extends JpaRepository<Voting, Long> {

    @Override
    Optional<Voting> findById(Long aLong);
}
