package hasebo.scrumpoker.controller;

import hasebo.scrumpoker.model.*;
import hasebo.scrumpoker.repository.CardRepository;
import hasebo.scrumpoker.repository.RoomRepository;
import hasebo.scrumpoker.repository.VoteRepository;
import hasebo.scrumpoker.repository.VotingRepository;
import hasebo.scrumpoker.service.MemberService;
import hasebo.scrumpoker.service.VotingService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@AllArgsConstructor
public class VotingWsController {

    private final CardRepository cardRepository;
    private final RoomRepository roomRepository;
    private final MemberService memberService;
    private final VotingRepository votingRepository;
    private final VoteRepository voteRepository;
    private final VotingService votingService;

    @GetMapping("/votingws/{roomcode}")
    public String voting(@PathVariable("roomcode") String roomCode,
                         Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("member", auth.getName());
        Room votingRoom = roomRepository.findByCode(roomCode).get();

        List<Card> roomCards = votingRoom.getCards();
        model.addAttribute("room", votingRoom);
        model.addAttribute("roomCards", roomCards);

        Member currentMember = memberService.getMemberByName(auth.getName());
        model.addAttribute("voter", currentMember.getName());

        Optional<Voting> optionalVoting = votingRepository.findById(1L);
        Optional<Vote> existingVoteOptional = voteRepository.findByVoterAndVotingAndRoom(currentMember, optionalVoting.get(), votingRoom);

        if (!existingVoteOptional.isPresent()) {
            votingService.createVote(currentMember, votingRoom, optionalVoting);
        } else {
            Vote existingVote = existingVoteOptional.get();
            Card cardVote = existingVote.getVote();
            model.addAttribute("selectedCard", cardVote);
        }

        List<Vote> votes = voteRepository.findByVotingAndRoom(optionalVoting, votingRoom);
        if (!votes.isEmpty()) {
            for (Vote vote : votes) {
                if (vote.getVote() != null) {
                    vote.getVote().setFigure(String.format("%-25s", vote.getVote().getFigure()));
                }
            }
            model.addAttribute("votes", votes);
        }
        votingService.sendVotesToClients(votes, votingRoom.getCode());
        return "votingws";
    }

    @PostMapping("/savevotews/{roomcode}")
    @ResponseBody
    public ResponseEntity<?> saveVoteWs(@PathVariable("roomcode") String roomCode,
                             @RequestBody Map<String, Object> requestBody) {
        Long cardId = Long.parseLong((String)requestBody.get("content"));
        Optional<Card> cardOptional = cardRepository.findById(cardId);
        if (cardOptional.isPresent()) {
            Card card = cardOptional.get();
            votingService.saveVote(roomCode, card);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/deletevoterws/{voterid}/{roomcode}")
    public String deleteVoter(@PathVariable("voterid") Long voterId,
                              @PathVariable("roomcode") String roomCode) {
        votingService.deleteVoter(roomCode, voterId);
        return "redirect:/votingws/" + roomCode;
    }
}

