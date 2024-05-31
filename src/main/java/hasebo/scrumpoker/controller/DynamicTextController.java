package hasebo.scrumpoker.controller;

import hasebo.scrumpoker.service.RandomTextService;
import hasebo.scrumpoker.model.TextGenerated;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DynamicTextController {

    private final RandomTextService randomTextService;

    public DynamicTextController(RandomTextService randomTextService) {

        this.randomTextService = randomTextService;
    }

    @GetMapping("/dyntext")
    public String randomText(Model model) {
        TextGenerated textGenerated = randomTextService.generateRandomText();
        model.addAttribute("randomText", textGenerated);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("member", auth.getName());
        return "randomText";
    }

}

