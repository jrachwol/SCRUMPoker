package hasebo.scrumpoker.controller;

import hasebo.scrumpoker.model.Card;
import hasebo.scrumpoker.model.Room;
import hasebo.scrumpoker.repository.VoteRepository;
import hasebo.scrumpoker.service.CardService;
import hasebo.scrumpoker.service.MemberService;
import hasebo.scrumpoker.service.RandomTextService;
import hasebo.scrumpoker.service.RoomService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class RoomController {

    private final RoomService roomService;
    private final CardService cardService;
    private final MemberService memberService;
    private final RandomTextService randomTextService;
    private final VoteRepository voteRepository;

    public RoomController(RoomService roomService,
                          MemberService memberService,
                          CardService cardService,
                          RandomTextService randomTextService,
                          VoteRepository voteRepository) {
        this.roomService = roomService;
        this.memberService = memberService;
        this.cardService = cardService;
        this.randomTextService = randomTextService;
        this.voteRepository = voteRepository;
    }

    @GetMapping("/room/{code}")
    public String roomInfo(@PathVariable("code") String code, Model model) {
        Room room = roomService.getRoomInfoByCode(code);
        List<Card> allCards = new ArrayList<>(cardService.getAllCards());
        model.addAttribute("room", room);
        model.addAttribute("allCards", allCards);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("member", auth.getName());
        return "room";
    }

    @PostMapping("/saveRoom/{code}")
    public String saveRoom(@PathVariable("code") String code, @ModelAttribute Room room) {
        Room existingRoom = roomService.getRoomInfoByCode(code);
        existingRoom.setCards(room.getCards());
        roomService.saveRoom(existingRoom);
        return "redirect:/rooms";
    }

    @GetMapping("/newroom")
    public String newRoom(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("member", auth.getName());
        Room room = new Room();
//        room.setName("room");
        model.addAttribute("room", room);
        ArrayList<Card> allCards = new ArrayList<>((Collection) cardService.getAllCards());
        model.addAttribute("allCards", allCards);
        return "newroom";
    }

    @PostMapping("/saveNewRoom")
    public String saveNewRoom(@ModelAttribute Room room) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        room.setOwner(memberService.getMemberByName(auth.getName()));
        room.setCode(randomTextService.generateRandomText().getGeneratedText()); //Jak skorzystać z RandomTextService z main?
        roomService.saveRoom(room);
        return "redirect:/rooms";
    }

//    usuwanie pokoju /deleteRoom/{code}
    @GetMapping("/deleteRoom/{code}")
    public String deleteRoom(@PathVariable("code") String code) {
        Room room = roomService.getRoomInfoByCode(code);
        roomService.deleteRoom(room);
        return "redirect:/rooms";
    }

    @GetMapping("/rooms")
    public String rooms(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("member", auth.getName());
        model.addAttribute("rooms", roomService.getRoomsByOwnerName(auth.getName()));
        return "rooms";
    }

}

