package hasebo.scrumpoker.repository;

import hasebo.scrumpoker.model.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<Member,Long> {
    Optional<Member> findByName(String name);
}
