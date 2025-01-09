package hasebo.scrumpoker.controller;

import hasebo.scrumpoker.model.*;
import hasebo.scrumpoker.service.VotingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class VotingWsController {

    private final VotingService votingService;

    @GetMapping("/votingws/{roomcode}")
    public String voting(@PathVariable("roomcode") String roomCode,
                         Model model) {
        votingService.handleVoting(roomCode, model);
        return "votingws";
    }

    @PostMapping("/savevotews/{roomcode}")
    public String saveVoteWs(@PathVariable("roomcode") String roomCode,
                             @ModelAttribute Card card,
                             Model model) {
        votingService.saveVote(roomCode, card, model);
        return "redirect:/votingws/" + roomCode;
    }

    @GetMapping("/deletevoterws/{voterid}/{roomcode}")
    public String deleteVoter(@PathVariable("voterid") Long voterId,
                              @PathVariable("roomcode") String roomCode) {
        votingService.deleteVoter(roomCode, voterId);
        return "redirect:/votingws/" + roomCode;
    }
}

