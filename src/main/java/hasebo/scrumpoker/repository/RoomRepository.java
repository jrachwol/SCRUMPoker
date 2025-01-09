package hasebo.scrumpoker.repository;

import hasebo.scrumpoker.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByName(String name);
    Optional<Room> findById(Long id);
    Optional<List<Room>> findByOwnerId(Long id);
    Optional<Room> findByCode(String code);

}