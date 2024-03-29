package hasebo.scrumpoker.repository;

import hasebo.scrumpoker.model.Room;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends  CrudRepository<Room, Long> {
    Optional<Room> findByName(String name);
    Optional<Room> findById(Long id);
    Optional<List<Room>> findByOwnerId(Long id);
    Optional<Room> findByCode(String code);

}