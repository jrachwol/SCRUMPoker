package hasebo.scrumpoker.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public String home() {
        return "Access for everyone";
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping("/member")
    public String member() {
        return "Hello Member!";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "Hello Admin!";
    }
}
