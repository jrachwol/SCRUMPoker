package hasebo.scrumpoker.service;

import hasebo.scrumpoker.model.Member;
import hasebo.scrumpoker.model.Room;
import hasebo.scrumpoker.repository.MemberRepository;
import hasebo.scrumpoker.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    public List<Room> getRoomsByOwnerName(String ownerName) {
        Optional<Member> member = memberRepository.findByName(ownerName);
        if (member.isPresent()) {
            Optional<List<Room>> rooms = roomRepository.findByOwnerId(member.get().getId());
            if (rooms.isPresent()) {
                return rooms.get();
            }
        }
        return new ArrayList<>();  // zwróć pustą listę, jeśli nie znaleziono pokoi lub właściciela
    }

    public void saveRoom(Room room) {
        roomRepository.save(room);
    }

    public void deleteRoom(String roomCode) {
        Room room = roomRepository.findByCode(roomCode).get();
        roomRepository.delete(room);
    }

}
