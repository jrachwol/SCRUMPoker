package hasebo.scrumpoker.repository;

import hasebo.scrumpoker.model.Member;
import hasebo.scrumpoker.model.Room;
import hasebo.scrumpoker.model.Vote;
import hasebo.scrumpoker.model.Voting;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VoteRepository extends CrudRepository<Vote, Long> {


    Optional<Vote> findByVoterAndVotingAndRoom(Member voter, Voting voting, Room room);
}
