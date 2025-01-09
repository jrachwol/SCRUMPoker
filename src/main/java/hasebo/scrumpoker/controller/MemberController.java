package hasebo.scrumpoker.controller;

import hasebo.scrumpoker.model.Member;
import hasebo.scrumpoker.repository.MemberRepository;
import hasebo.scrumpoker.service.MemberService;
import hasebo.scrumpoker.service.RandomTextService;
import lombok.RequiredArgsConstructor;
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
        try {
            memberService.registerNewMember(member);
            redirectAttributes.addFlashAttribute("success", "User " + member.getName() + " registered successfully");
            return "redirect:/login";
        } catch(Exception e) {
            model.addAttribute("error", e.getMessage());
            return "newmember";
        }
    }
}
