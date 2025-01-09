package hasebo.scrumpoker.service;

import com.google.gson.Gson;
import hasebo.scrumpoker.model.Card;
import hasebo.scrumpoker.model.Member;
import hasebo.scrumpoker.model.Room;
import hasebo.scrumpoker.model.Vote;
import hasebo.scrumpoker.model.Voting;
import hasebo.scrumpoker.repository.RoomRepository;
import hasebo.scrumpoker.repository.VoteRepository;
import hasebo.scrumpoker.repository.VotingRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
public class VotingService {

    private final MemberService memberService;
    private final VotingRepository votingRepository;
    private final VoteRepository voteRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RoomRepository roomRepository;

    public void createVote(Member currentMember, Room votingRoom, Optional<Voting> optionalVoting) {
        Vote vote = new Vote(currentMember, votingRoom, null);
        optionalVoting.ifPresent(vote::setVoting);
        voteRepository.save(vote);
    }

    public void saveVote(String roomCode, Card card) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Member voter = memberService.getMemberByName(auth.getName());
        Room votingRoom = roomRepository.findByCode(roomCode).get();
        Optional<Voting> optionalVoting = votingRepository.findById(1L);
        Optional<Vote> existingVoteOptional = voteRepository.findByVoterAndVotingAndRoom(voter, optionalVoting.get(), votingRoom);
        if(existingVoteOptional.isPresent()){
            Vote existingVote = existingVoteOptional.get();
            existingVote.setVote(card);  // set new vote
            voteRepository.save(existingVote);  // update existing vote
        } else {
            // no existing vote, creating new
            Vote vote = new Vote(voter, votingRoom, card);
            optionalVoting.ifPresent(vote::setVoting);
            voteRepository.save(vote);
        }
        List<Vote> votes = voteRepository.findByVotingAndRoom(optionalVoting, votingRoom);
        if (!votes.isEmpty()) {
            sendVotesToClients(votes, roomCode);
        }
    }

    public void sendVotesToClients(List<Vote> votes, String roomCode) {
        Gson gson = new Gson();
        List<String> jsonVotes = new ArrayList<>();
        for (Vote vote : votes) {
            Map<String, Object> map = new HashMap<>();
            map.put("voter", vote.getVoter().getName());
            map.put("voterId", vote.getVoter().getId());
            if (vote.getVote() == null) {
                map.put("vote", String.format("%-25s", "Oczekiwanie na głos"));
            } else {
                map.put("vote", String.format("%-25s", vote.getVote().getFigure()));
            }
            jsonVotes.add(gson.toJson(map));
        }
        simpMessagingTemplate.convertAndSend("/topic/votings/" + roomCode, jsonVotes);
    }

    public void deleteVoter(String roomCode, Long voterId) {
        Room room = roomRepository.findByCode(roomCode).get();

        Optional<Voting> optionalVoting = votingRepository.findById(1L);
        Optional<Vote> existingVoteOptional = voteRepository.findByVoterAndVotingAndRoom(memberService.getMemberById(voterId), optionalVoting.get(), room);
        if (existingVoteOptional.isPresent()) {
            Vote voteToDelete = existingVoteOptional.get();
            voteRepository.delete(voteToDelete);
        }
    }

}
