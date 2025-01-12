package hasebo.scrumpoker.service;

import hasebo.scrumpoker.exception.UserAlreadyExistsException;
import hasebo.scrumpoker.model.Member;
import hasebo.scrumpoker.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    public final PasswordEncoder encoder;

    public Member getMemberById (Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    public Member getMemberByName (String ownerName) {
        return memberRepository.findByName(ownerName).get();
    }

    public void registerNewMember(Member member) {
        if(memberRepository.existsByName(member.getName())) {
            throw new UserAlreadyExistsException("User with the given name: " + member.getName() + " already exists.");
        }

        String password = encoder.encode(member.getPassword());
        member.setPassword(password);
        member.setRoles("ROLE_MEMBER");
        memberRepository.save(member);
    }
}
