package hasebo.scrumpoker.controller;

import hasebo.scrumpoker.model.*;
import hasebo.scrumpoker.repository.CardRepository;
import hasebo.scrumpoker.repository.RoomRepository;
import hasebo.scrumpoker.repository.VoteRepository;
import hasebo.scrumpoker.repository.VotingRepository;
import hasebo.scrumpoker.service.CardService;
import hasebo.scrumpoker.service.MemberService;
import hasebo.scrumpoker.service.RandomTextService;
import hasebo.scrumpoker.service.RoomService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class VotingController {

    private final RoomService roomService;
    private final MemberService memberService;
    private final VoteRepository voteRepository;
    private final VotingRepository votingRepository;
    private final RoomRepository roomRepository;
    private final CardService cardService;
    private final CardRepository cardRepository;

    @GetMapping("/voting/{code}")
    public String voting(@PathVariable("code") String code,
                         HttpSession httpSession,
                         Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("member", auth.getName());
        Room room = roomService.getRoomInfoByCode(code);
        ArrayList<Card> roomCards = new ArrayList<>((Collection) room.getCards());
        model.addAttribute("room", room);
        model.addAttribute("roomCards", roomCards);
        Card card = (Card) httpSession.getAttribute("selectedCard");
        model.addAttribute("selectedCard", card);

        String username = auth.getName();
        Member currentMember = memberService.getMemberByName(username);
        if (!room.getVoters().contains(currentMember)) {
            room.getVoters().add(currentMember);
        }
        roomRepository.save(room);
        List<Member> voters = room.getVoters();
        model.addAttribute("voters", voters);

        Room votingRoom = roomService.getRoomInfoByCode(code);
        Optional<Voting> optionalVoting = votingRepository.findById(1L);
        Optional<Vote> existingVoteOptional = voteRepository.findByVoterAndVotingAndRoom(currentMember, optionalVoting.get(), votingRoom);
        if(!existingVoteOptional.isPresent()){
            Vote vote = new Vote(currentMember, votingRoom, card);
            optionalVoting.ifPresent(vote::setVoting);
            voteRepository.save(vote);
        } else {
            Vote existingVote = existingVoteOptional.get();
            Card cardVote = existingVote.getVote();
            if(cardVote!=null){
                Optional<Card> optionalCard = cardRepository.findById(cardVote.getId());
                if(optionalCard.isPresent()) {
                    Card selectedCard = optionalCard.get();
                    model.addAttribute("selectedCard", selectedCard);
                }
            }
        }

        Optional<List<Vote>> votes = voteRepository.findByVotingAndRoom(optionalVoting, votingRoom);
        if(votes.isPresent()){
            List<Vote> votesList = votes.get();
            model.addAttribute("votes", votesList);
        }
        return "voting";
    }

    @PostMapping("/saveVote/{code}")
    public String saveVorte(@PathVariable("code") String code,
                            @ModelAttribute Card card,
                            Model model) {
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
        return "redirect:/voting/" + code;
    }

    @GetMapping("/deleteVoter/{id}/{code}")
    public String deleteVoter(@PathVariable("id") Long id,
                              @PathVariable("code") String code,
                              HttpSession httpSession,
                              Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Room room = roomService.getRoomInfoByCode(code);
        room.getVoters().removeIf(voter -> voter.getId().equals(id));
        roomRepository.save(room);

        Optional<Voting> optionalVoting = votingRepository.findById(1L);
        Optional<Vote> existingVoteOptional = voteRepository.findByVoterAndVotingAndRoom(memberService.getMemberById(id), optionalVoting.get(), room);
        if (existingVoteOptional.isPresent()) {
            Vote voteToDelete = existingVoteOptional.get();
            voteRepository.delete(voteToDelete);
        }

        return "redirect:/voting/" + code;
    }

}
