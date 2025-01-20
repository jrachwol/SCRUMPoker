package hasebo.scrumpoker.service;

import hasebo.scrumpoker.model.Room;
import hasebo.scrumpoker.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoomAccessService {

    private final RoomRepository roomRepository;

    public boolean isOwner(String roomcode, String username) {
        Room room = roomRepository.findByCode(roomcode)
            .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        return room.getOwner().getUsername().equals(username);
    }
}
