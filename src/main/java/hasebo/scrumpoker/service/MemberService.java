package hasebo.scrumpoker.service;

import hasebo.scrumpoker.model.Member;
import hasebo.scrumpoker.repository.MemberRepository;
import hasebo.scrumpoker.repository.RoomRepository;

public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member getMemberById (Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    Member findByName (String ownerName) {
        return memberRepository.findByName(ownerName).get();
    }
}
