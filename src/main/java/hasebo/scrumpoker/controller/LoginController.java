package hasebo.scrumpoker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @PostMapping("/Login")
    String login() {
        return "login";
    }
}
