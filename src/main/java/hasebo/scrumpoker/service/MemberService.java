package hasebo.scrumpoker.service;

import hasebo.scrumpoker.model.Member;
import hasebo.scrumpoker.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member getMemberById (Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    public Member getMemberByName (String ownerName) {
        return memberRepository.findByName(ownerName).get();
    }
}
