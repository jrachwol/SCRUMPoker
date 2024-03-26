package hasebo.scrumpoker.controller;

import hasebo.scrumpoker.model.Room;
import hasebo.scrumpoker.repository.MemberRepository;
import hasebo.scrumpoker.repository.RoomRepository;
import hasebo.scrumpoker.service.RoomService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RoomController {

    private final RoomService roomService;
    private final MemberRepository memberRepository;

    public RoomController(RoomService roomService, MemberRepository memberRepository) {
        this.roomService = roomService;
        this.memberRepository = memberRepository;
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
        model.addAttribute("room", room);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("member", auth.getName().toString());
        return "room";
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
