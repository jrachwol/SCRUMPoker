package hasebo.scrumpoker;

import hasebo.scrumpoker.model.Member;
import hasebo.scrumpoker.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ScrumPokerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScrumPokerApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository users, PasswordEncoder encoder) {
        return args -> {
            users.save(new Member("user01", encoder.encode("pswd"), "ROLE_MEMBER"));
            users.save(new Member("user02", encoder.encode("pswd"), "ROLE_MEMBER,ROLE_ADMIN"));
        };
    }



}
