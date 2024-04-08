package hasebo.scrumpoker;

import hasebo.scrumpoker.model.Card;
import hasebo.scrumpoker.model.Member;
import hasebo.scrumpoker.model.Room;
import hasebo.scrumpoker.repository.CardRepository;
import hasebo.scrumpoker.repository.MemberRepository;
import hasebo.scrumpoker.repository.RoomRepository;
import hasebo.scrumpoker.service.RandomTextService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@SpringBootApplication
public class ScrumPokerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScrumPokerApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(MemberRepository members, PasswordEncoder encoder, RoomRepository rooms, CardRepository cards) {
        RandomTextService randomTextService = new RandomTextService();
        return args -> {
            members.save(new Member("member01", encoder.encode("pswd"), "ROLE_MEMBER"));
            members.save(new Member("member02", encoder.encode("pswd"), "ROLE_MEMBER,ROLE_ADMIN"));
            members.save(new Member("member03", encoder.encode("pswd"), "ROLE_MEMBER"));


//            rooms.save(new Room(randomTextService.generateRandomText().getGeneratedText(), "room01", members.findByName("member01").get().getId()));
//            rooms.save(new Room(randomTextService.generateRandomText().getGeneratedText(), "room04", members.findByName("member01").get().getId()));
//            rooms.save(new Room(randomTextService.generateRandomText().getGeneratedText(),  "room02", members.findByName("member02").get().getId()));
//            rooms.save(new Room(randomTextService.generateRandomText().getGeneratedText(),  "room03", members.findByName("member02").get().getId()));

            // Utwórz karty
            List<String> figures = Arrays.asList("1", "2", "3", "Sth01", "Sth02");
            for (String figure : figures) {
                Card card = new Card();
                card.setFigure(figure);
                cards.save(card);
            }

            // Utwórz pokój i przypisz do niego wszystkie karty
            Room room01 = new Room();
            room01.setName("room01");
            room01.setOwner(members.findByName("member01").get());
            room01.setCode(randomTextService.generateRandomText().getGeneratedText());
            room01.setCards(new ArrayList<>((Collection) cards.findAll()));
            rooms.save(room01);


            // Utwórz pokój i przypisz do niego wszystkie karty
            Room room02 = new Room();
            room02.setName("room02");
            room02.setOwner(members.findByName("member01").get());
            room02.setCode(randomTextService.generateRandomText().getGeneratedText());
            room02.setCards(new ArrayList<>((Collection) cards.findAll()));
            rooms.save(room02);


            // Utwórz pokój i przypisz do niego wszystkie karty
            Room room03 = new Room();
            room03.setName("room03");
            room03.setOwner(members.findByName("member02").get());
            room03.setCode(randomTextService.generateRandomText().getGeneratedText());
            room03.setCards(new ArrayList<>((Collection) cards.findAll()));
            rooms.save(room03);


            // Utwórz pokój i przypisz do niego wszystkie karty
            Room room04 = new Room();
            room04.setName("room04");
            room04.setOwner(members.findByName("member02").get());
            room04.setCode(randomTextService.generateRandomText().getGeneratedText());
            room04.setCards(new ArrayList<>((Collection) cards.findAll()));
            rooms.save(room04);


            // Utwórz pokój i przypisz do niego wszystkie karty

            Room room05 = new Room();
            room05.setName("room05");
            room05.setOwner(members.findByName("member02").get());
            room05.setCode(randomTextService.generateRandomText().getGeneratedText());
            room05.setCards(new ArrayList<>((Collection) cards.findAll()));
            rooms.save(room05);

            Room room06 = new Room();
            room06.setName("room06");
            room06.setOwner(members.findByName("member02").get());
            room06.setCode(randomTextService.generateRandomText().getGeneratedText());
            List<Card> cardsList = new ArrayList<>((Collection) cards.findAll());
            cardsList.remove(cardsList.size() - 1);
            room06.setCards(cardsList);
            rooms.save(room06);

        };
    }

}
