package hasebo.scrumpoker.controller;

import hasebo.scrumpoker.service.RandomTextService;
import hasebo.scrumpoker.model.TextGenerated;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
        return "randomText";
    }

}
