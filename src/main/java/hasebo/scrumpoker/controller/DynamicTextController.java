package hasebo.scrumpoker.controller;

import hasebo.scrumpoker.service.RandomTextService;
import hasebo.scrumpoker.model.TextGenerated;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
        model.addAttribute("member", auth.getName().toString());
        return "randomText";
    }

    @GetMapping("/")
    public String homePageRedirect () {
        return ("redirect:/dyntext");
    }

}

