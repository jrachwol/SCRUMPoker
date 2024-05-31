package hasebo.scrumpoker.repository;

import hasebo.scrumpoker.model.Member;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long> {

    Optional<Member> findByName(String name);

    Optional<Member> findById(Long id);

    boolean existsByName(String name);
}
