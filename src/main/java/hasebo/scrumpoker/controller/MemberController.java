package hasebo.scrumpoker.controller;

import hasebo.scrumpoker.model.Member;
import hasebo.scrumpoker.repository.MemberRepository;
import hasebo.scrumpoker.service.MemberService;
import hasebo.scrumpoker.service.RandomTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MemberController {

    public final MemberService memberService;
    public final RandomTextService randomTextService;
    public final MemberRepository memberRepository;
    public final PasswordEncoder encoder;

    @Autowired
    public MemberController(MemberService memberService,
                            RandomTextService randomTextService,
                            MemberRepository memberRepository,
                            PasswordEncoder encoder) {
        this.memberService = memberService;
        this.randomTextService = randomTextService;
        this.memberRepository = memberRepository;
        this.encoder = encoder;
    }

    @GetMapping("/newmember")
    public String newMember (Model model) {
        Member member = new Member();
        model.addAttribute("member", member);
        System.out.println("wyświetlenie widoku NewMemeber");
        return "newmember";
    }

    @PostMapping("/savenewmember")
    public String saveNewMember(@ModelAttribute Member member,
                                RedirectAttributes redirectAttributes) {
        if(memberRepository.existsByName(member.getName())) {
            redirectAttributes.addFlashAttribute("error",
                    "Użytkownik o podanej nazwie istnieje.");
            return "redirect:/newmember";
        }

        String password = encoder.encode(member.getPassword());
        member.setPassword(password);
        member.setRoles("ROLE_MEMBER");
        memberRepository.save(member);
        return "redirect:/login";

    }
}
