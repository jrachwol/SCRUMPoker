package hasebo.scrumpoker.controller;

import hasebo.scrumpoker.model.*;
import hasebo.scrumpoker.repository.CardRepository;
import hasebo.scrumpoker.service.VotingService;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final VotingService votingService;
    private final CardRepository cardRepository;

    @GetMapping("/votingws/{roomcode}")
    public String voting(@PathVariable("roomcode") String roomCode,
                         Model model) {
        votingService.handleVoting(roomCode, model);
        return "votingws";
    }

    @PostMapping("/savevotews/{roomcode}")
    @ResponseBody
    public ResponseEntity<?> saveVoteWs(@PathVariable("roomcode") String roomCode,
                             @RequestBody Map<String, Object> requestBody,
                             Model model) {
        Long cardId = Long.parseLong((String)requestBody.get("content"));
        Optional<Card> cardOptional = cardRepository.findById(cardId);
        if (cardOptional.isPresent()) {
            Card card = cardOptional.get();
            votingService.saveVote(roomCode, card, model);
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

