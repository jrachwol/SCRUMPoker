package hasebo.scrumpoker.controller;

import hasebo.scrumpoker.model.Card;
import hasebo.scrumpoker.model.Room;
import hasebo.scrumpoker.repository.CardRepository;
import hasebo.scrumpoker.repository.MemberRepository;
import hasebo.scrumpoker.repository.RoomRepository;
import hasebo.scrumpoker.service.CardService;
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
    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;
    private final CardService cardService;

    public RoomController(RoomService roomService, MemberRepository memberRepository, CardRepository cardRepository, CardService cardService) {
        this.roomService = roomService;
        this.memberRepository = memberRepository;
        this.cardRepository = cardRepository;
        this.cardService = cardService;
    }

//    @GetMapping("/room/{id}")
//    public String roomInfo(@PathVariable("id") Long id, Model model) {
//        Room room = new Room();
//        room = roomService.getRoomInfoById(id);
//        model.addAttribute("room", room);
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        model.addAttribute("member", auth.getName().toString());
//        return "room";
//    }


    @GetMapping("/room/{code}")
    public String roomInfo(@PathVariable("code") String code, Model model) {
        Room room = new Room();
        room = roomService.getRoomInfoByCode(code);
        System.out.println(room.getOwner().getName());
        ArrayList<Card> allCards = new ArrayList<>((Collection) cardRepository.findAll());
        model.addAttribute("room", room);
        model.addAttribute("allCards", allCards);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("member", auth.getName().toString());
        return "room";
    }


    @PostMapping("/saveRoom/{code}")
    public String saveRoom(@PathVariable("code") String code, @ModelAttribute Room room, Model model) { // @ModelAttribute Room room,
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Room existingRoom = roomService.getRoomInfoByCode(code);
        existingRoom.setCards(room.getCards());
        roomService.saveRoom(existingRoom);
        return "redirect:/rooms";
    }

    @GetMapping("/rooms")
    public String roomsList(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("member", auth.getName().toString());
        List<Room> rooms = new ArrayList<>();
        rooms = roomService.getRoomsByOwnerName(auth.getName().toString());
//        rooms = roomService.getRoomsByOwnerId(memberRepository.findByName(auth.getName().toString()).get().getId());
//        Optional<List<Room>> rooms;
//        rooms = roomService.getRoomsByOwnerName(auth.getName().toString());
        model.addAttribute("rooms", rooms);

        return "rooms";
    }

}
