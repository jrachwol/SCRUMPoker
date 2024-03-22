package hasebo.scrumpoker.service;

import hasebo.scrumpoker.model.Room;
import hasebo.scrumpoker.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
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


}
