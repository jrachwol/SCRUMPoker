package hasebo.scrumpoker.controller;

import hasebo.scrumpoker.model.Card;
import hasebo.scrumpoker.model.Room;
import hasebo.scrumpoker.repository.CardRepository;
import hasebo.scrumpoker.service.RoomService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;

@Controller
public class RoomController {

    private final RoomService roomService;
    private final CardRepository cardRepository;

    public RoomController(RoomService roomService, CardRepository cardRepository) {
        this.roomService = roomService;
        this.cardRepository = cardRepository;
    }

    @GetMapping("/room/{code}")
    public String roomInfo(@PathVariable("code") String code, Model model) {
        Room room = roomService.getRoomInfoByCode(code);
        System.out.println(room.getOwner().getName());
        ArrayList<Card> allCards = new ArrayList<>((Collection) cardRepository.findAll());
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

