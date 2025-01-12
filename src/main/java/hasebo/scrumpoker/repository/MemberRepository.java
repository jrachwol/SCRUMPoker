package hasebo.scrumpoker.repository;

import hasebo.scrumpoker.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByName(String name);

    Optional<Member> findById(Long id);

    boolean existsByName(String name);
}
