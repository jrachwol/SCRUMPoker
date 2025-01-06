package hasebo.scrumpoker.controller;

import hasebo.scrumpoker.model.Member;
import hasebo.scrumpoker.repository.MemberRepository;
import hasebo.scrumpoker.service.MemberService;
import hasebo.scrumpoker.service.RandomTextService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class MemberController {

    public final MemberService memberService;
    public final RandomTextService randomTextService;
    public final MemberRepository memberRepository;
//    public final PasswordEncoder encoder;

    @GetMapping("/newmember")
    public String newMember (Model model) {
        Member member = new Member();
        model.addAttribute("member", member);
        System.out.println("wyświetlenie widoku NewMemeber");
        return "newmember";
    }

    @PostMapping("/newmember")
    public String saveNewMember(@ModelAttribute Member member,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (memberService.registerNewMember(member, model, redirectAttributes)) {
            return "redirect:/login";
        } else {
            return "newmember";
        }
    }
}
