package hasebo.scrumpoker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/Login")
    String login() {
        return "rooms";
    }

    @GetMapping("/")
    public String homePageRedirect () {
        return ("redirect:/rooms");
    }

}
