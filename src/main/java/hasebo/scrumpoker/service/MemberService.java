package hasebo.scrumpoker.service;

import hasebo.scrumpoker.model.Member;
import hasebo.scrumpoker.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    public Member getMemberById (Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    public Member getMemberByName (String ownerName) {
        return memberRepository.findByName(ownerName).get();
    }
}
