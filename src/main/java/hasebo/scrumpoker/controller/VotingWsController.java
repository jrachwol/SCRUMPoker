package hasebo.scrumpoker.controller;

import com.google.gson.Gson;
import hasebo.scrumpoker.model.*;
import hasebo.scrumpoker.repository.VoteRepository;
import hasebo.scrumpoker.repository.VotingRepository;
import hasebo.scrumpoker.service.MemberService;
import hasebo.scrumpoker.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
@AllArgsConstructor
public class VotingWsController {

    private final RoomService roomService;
    private final MemberService memberService;
    private final VoteRepository voteRepository;
    private final VotingRepository votingRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/votingws/{code}")
    public String voting(@PathVariable("code") String code,
                         Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("member", auth.getName());
        Room votingRoom = roomService.getRoomInfoByCode(code);
        List<Card> roomCards = votingRoom.getCards();
        model.addAttribute("room", votingRoom);
        model.addAttribute("roomCards", roomCards);

        String username = auth.getName();
        Member currentMember = memberService.getMemberByName(username);
        model.addAttribute("voter", currentMember.getName());

        Optional<Voting> optionalVoting = votingRepository.findById(1L);
        Optional<Vote> existingVoteOptional = voteRepository.findByVoterAndVotingAndRoom(currentMember, optionalVoting.get(), votingRoom);
        if(!existingVoteOptional.isPresent()){
            Vote vote = new Vote(currentMember, votingRoom, null);
            optionalVoting.ifPresent(vote::setVoting);
            voteRepository.save(vote);
        } else {
            Vote existingVote = existingVoteOptional.get();
            Card cardVote = existingVote.getVote();
            model.addAttribute("selectedCard", cardVote);
        }

        List<Vote> votes = voteRepository.findByVotingAndRoom(optionalVoting, votingRoom);
        if (!votes.isEmpty()){
            model.addAttribute("votes", votes);
            Gson gson = new Gson();
            List<String> jsonVotes = new ArrayList<>();
            for (Vote vote : votes) {
                Map<String, Object> map = new HashMap<>();
                map.put("voter", vote.getVoter().getName());
                map.put("voterId", vote.getVoter().getId());
                if (vote.getVote() == null) {
                    map.put("vote", "Oczekiwanie na głos");
                } else {
                    map.put("vote", vote.getVote().getFigure());
                }
                jsonVotes.add(gson.toJson(map));
            }
            simpMessagingTemplate.convertAndSend("/topic/votings", jsonVotes);
        }
        return "votingws";
    }

    @PostMapping("/savevotews/{code}")
    public String saveVoteWs(@PathVariable("code") String code,
                             @ModelAttribute Card card) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member voter = memberService.getMemberByName(auth.getName());
        Room votingRoom = roomService.getRoomInfoByCode(code);
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
        return "redirect:/votingws/" + code;
    }

    @GetMapping("/deletevoterws/{id}/{code}")
    public String deleteVoter(@PathVariable("id") Long id,
                              @PathVariable("code") String code) {
        Room room = roomService.getRoomInfoByCode(code);

        Optional<Voting> optionalVoting = votingRepository.findById(1L);
        Optional<Vote> existingVoteOptional = voteRepository.findByVoterAndVotingAndRoom(memberService.getMemberById(id), optionalVoting.get(), room);
        if (existingVoteOptional.isPresent()) {
            Vote voteToDelete = existingVoteOptional.get();
            voteRepository.delete(voteToDelete);
        }
        return "redirect:/votingws/" + code;
    }
}

