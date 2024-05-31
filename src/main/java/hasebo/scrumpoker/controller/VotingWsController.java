package hasebo.scrumpoker.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hasebo.scrumpoker.model.*;
import hasebo.scrumpoker.repository.CardRepository;
import hasebo.scrumpoker.repository.RoomRepository;
import hasebo.scrumpoker.repository.VoteRepository;
import hasebo.scrumpoker.repository.VotingRepository;
import hasebo.scrumpoker.service.MemberService;
import hasebo.scrumpoker.service.RoomService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
    private final RoomRepository roomRepository;
    private final CardRepository cardRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello")
    @SendTo("/topic/votes")
    public String voting(String vote) throws Exception {
        return vote;
    }

    @GetMapping("/votingws/{code}")
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
        model.addAttribute("voter", currentMember.getName());
//        if (!room.getVoters().contains(currentMember)) {
//            room.getVoters().add(currentMember);
//        }
        roomRepository.save(room);
//        List<Member> voters = room.getVoters();
//        model.addAttribute("voters", voters);

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
        if (votes.isPresent()){
            List<Vote> votesList = votes.get();
            model.addAttribute("votes", votesList);
            Gson gson = new Gson();
            List<String> jsonVotes = new ArrayList<>();
            for (Vote vote : votesList) {
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

//                String json = gson.toJson(vote.getVoter().getName());
//                vote.getVote().getFigure();
//                jsonList.add(json);
//            }
//            model.addAttribute("votes", votesList);
//            Gson gson = new Gson();
//            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//            String jsonVotes = gson.toJson(votesList);
//            System.out.println(jsonVotes);
//            simpMessagingTemplate.convertAndSend("/topic/votes", jsonVotes);

        }

        return "votingws";
    }

    @PostMapping("/saveVoteWs/{code}")
    public String saveVoteWs(@PathVariable("code") String code,
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
        return "redirect:/votingws/" + code;
    }

    @GetMapping("/deleteVoterws/{id}/{code}")
    public String deleteVoter(@PathVariable("id") Long id,
                              @PathVariable("code") String code,
                              HttpSession httpSession,
                              Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Room room = roomService.getRoomInfoByCode(code);
//        room.getVoters().removeIf(voter -> voter.getId().equals(id));
//        roomRepository.save(room);

        Optional<Voting> optionalVoting = votingRepository.findById(1L);
        Optional<Vote> existingVoteOptional = voteRepository.findByVoterAndVotingAndRoom(memberService.getMemberById(id), optionalVoting.get(), room);
        if (existingVoteOptional.isPresent()) {
            Vote voteToDelete = existingVoteOptional.get();
            voteRepository.delete(voteToDelete);
        }

        return "redirect:/votingws/" + code;
    }

}

