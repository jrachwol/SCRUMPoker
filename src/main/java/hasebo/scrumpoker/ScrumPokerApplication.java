package hasebo.scrumpoker;

import hasebo.scrumpoker.model.Member;
import hasebo.scrumpoker.model.Room;
import hasebo.scrumpoker.repository.RoomRepository;
import hasebo.scrumpoker.repository.UserRepository;
import hasebo.scrumpoker.service.RandomTextService;
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
    CommandLineRunner commandLineRunner(UserRepository users, PasswordEncoder encoder, RoomRepository rooms) {
        RandomTextService randomTextService = new RandomTextService();
        return args -> {
            users.save(new Member("member01", encoder.encode("pswd"), "ROLE_MEMBER"));
            users.save(new Member("member02", encoder.encode("pswd"), "ROLE_MEMBER,ROLE_ADMIN"));
            rooms.save(new Room(randomTextService.generateRandomText().getGeneratedText(), "room01", users.findByName("member01").get().getId()));
            rooms.save(new Room(randomTextService.generateRandomText().getGeneratedText(), "room04", users.findByName("member01").get().getId()));
            rooms.save(new Room(randomTextService.generateRandomText().getGeneratedText(),  "room02", users.findByName("member02").get().getId()));
            rooms.save(new Room(randomTextService.generateRandomText().getGeneratedText(),  "room03", users.findByName("member02").get().getId()));
        };
    }

}
