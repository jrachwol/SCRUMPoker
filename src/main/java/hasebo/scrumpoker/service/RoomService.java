package hasebo.scrumpoker.service;

import hasebo.scrumpoker.model.Member;
import hasebo.scrumpoker.model.Room;
import hasebo.scrumpoker.repository.MemberRepository;
import hasebo.scrumpoker.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
//    czy zamiast MemberRepository powinno być MemberService?
    private final MemberRepository memberRepository;

    public RoomService(RoomRepository roomRepository, MemberRepository memberRepository) {
        this.roomRepository = roomRepository;
        this.memberRepository = memberRepository;
    }

    public Room getRoomInfoById(Long roomId) {
        return roomRepository.findById(roomId).get();
    }

    public Room getRoomInfoByCode(String code) {
        return roomRepository.findByCode(code).get();
    }

    public List<Room> getRoomsByOwnerId(Long ownerId) {
        return roomRepository.findByOwnerId(ownerId).get();
    }

//    Bez Optional<> błąd w return
    public List<Room> getRoomsByOwnerName(String ownerName) {
//        działą, choć zwracana lista bez sprawdzania czy zwraca null
//        return getRoomsByOwnerId(memberRepository.findByName(ownerName).get().getId());

//        błąd z Optional<>
//        return roomRepository.findByOwnerId(memberRepository.findByName(ownerName).get().getId());

//        Czy dla Optional<> zawsze sprawdzać czy nie zwraca null?
        Optional<Member> member = memberRepository.findByName(ownerName);
        if (member.isPresent()) {
            Optional<List<Room>> rooms = roomRepository.findByOwnerId(member.get().getId());
            if (rooms.isPresent()) {
                return rooms.get();
            }
        }
        return new ArrayList<>();  // zwróć pustą listę, jeśli nie znaleziono pokoi lub właściciela
    }



}
