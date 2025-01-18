package hasebo.scrumpoker.controller;

import hasebo.scrumpoker.model.Card;
import hasebo.scrumpoker.model.Room;
import hasebo.scrumpoker.repository.RoomRepository;
import hasebo.scrumpoker.service.CardService;
import hasebo.scrumpoker.service.MemberService;
import hasebo.scrumpoker.service.RandomTextService;
import hasebo.scrumpoker.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@AllArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final CardService cardService;
    private final MemberService memberService;
    private final RandomTextService randomTextService;
    private final RoomRepository roomRepository;

    @GetMapping("/room/{roomcode}")
    public String roomInfo(@PathVariable("roomcode") String roomCode, Model model) {
        Room room = roomRepository.findByCode(roomCode).get();
        List<Card> allCards = cardService.getAllCards();
        model.addAttribute("room", room);
        model.addAttribute("allCards", allCards);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("member", auth.getName());
        return "room";
    }

    @PostMapping("/room/{roomcode}")
    public String saveRoom(@PathVariable("roomcode") String roomCode, @ModelAttribute Room room) {
        Room existingRoom = roomRepository.findByCode(roomCode).get();
        existingRoom.setCards(room.getCards());
        roomService.saveRoom(existingRoom);
        return "redirect:/rooms";
    }

    @GetMapping("/newroom")
    public String newRoom(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("member", auth.getName());
        Room room = new Room();
        model.addAttribute("room", room);
        List<Card> allCards = cardService.getAllCards();
        model.addAttribute("allCards", allCards);
        return "newroom";
    }

    @PostMapping("/newroom")
    public String saveNewRoom(@ModelAttribute Room room) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        room.setOwner(memberService.getMemberByName(auth.getName()));
        room.setCode(randomTextService.generateRandomText().getGeneratedText()); //Jak skorzystać z RandomTextService z main?
        roomService.saveRoom(room);
        return "redirect:/rooms";
    }

    @GetMapping("/deleteroom/{roomcode}")
    public String deleteRoom(@PathVariable("roomcode") String roomCode) {
        Room room = roomRepository.findByCode(roomCode).get();
        roomService.deleteRoom(roomCode);
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

