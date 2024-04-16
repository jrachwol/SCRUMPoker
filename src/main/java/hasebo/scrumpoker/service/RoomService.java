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

    public Room getRoomInfoById(Long roomId) {
        return roomRepository.findById(roomId).get();
    }

    public Room getRoomInfoByCode(String code) {
        return roomRepository.findByCode(code).get();
    }

    public List<Room> getRoomsByOwnerId(Long ownerId) {
        return roomRepository.findByOwnerId(ownerId).get();
    }

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

    public void deleteRoom(Room room) {
        roomRepository.delete(room);
    }

}
