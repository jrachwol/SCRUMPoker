package hasebo.scrumpoker;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import hasebo.scrumpoker.model.Card;
import hasebo.scrumpoker.model.Member;
import hasebo.scrumpoker.model.Room;
import hasebo.scrumpoker.model.Voting;
import hasebo.scrumpoker.repository.CardRepository;
import hasebo.scrumpoker.repository.MemberRepository;
import hasebo.scrumpoker.repository.RoomRepository;
import hasebo.scrumpoker.repository.VotingRepository;
import hasebo.scrumpoker.service.RandomTextService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@SpringBootApplication
public class ScrumPokerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScrumPokerApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner commandLineRunner(MemberRepository members,
                                        PasswordEncoder encoder,
                                        RoomRepository rooms,
                                        CardRepository cards,
                                        VotingRepository votings,
                                        RandomTextService randomTextService) {
        return args -> {
            members.save(new Member("m1", encoder.encode("q"), "ROLE_MEMBER"));
            members.save(new Member("m2", encoder.encode("q"), "ROLE_MEMBER,ROLE_ADMIN"));
            members.save(new Member("m3", encoder.encode("q"), "ROLE_MEMBER"));
            members.save(new Member("q", encoder.encode("q"), "ROLE_MEMBER"));

            // Tworzy domyślny zestaw kart do głosowania
            List<String> figures = Arrays.asList("1", "2", "3", "Sth01", "Sth02");
            for (String figure : figures) {
                Card card = new Card();
                card.setFigure(figure);
                cards.save(card);
            }

            List<Room> roomsList = new ArrayList<>();
            roomsList.add(createRoom("room01", members.findByName("m1").get(), randomTextService, cards));
            roomsList.add(createRoom("room02", members.findByName("m1").get(), randomTextService, cards));
            roomsList.add(createRoom("room03", members.findByName("m2").get(), randomTextService, cards));
            roomsList.add(createRoom("room04", members.findByName("m2").get(), randomTextService, cards));
            roomsList.add(createRoom("room05", members.findByName("m2").get(), randomTextService, cards, 0));
            roomsList.add(createRoom("room06", members.findByName("m2").get(), randomTextService, cards, 1));
            roomsList.add(createRoom("room07", members.findByName("m2").get(), randomTextService, cards, 2));

            rooms.saveAll(roomsList);

            Voting voting = new Voting("Nazwa", "Opis", true);
            votings.save(voting);

        };

    }

    private Room createRoom(String name, Member owner, RandomTextService randomTextService, CardRepository cards, Integer... exclusions) {
        Room room = new Room();
        room.setName(name);
        room.setOwner(owner);
        room.setCode(randomTextService.generateRandomText().getGeneratedText());

        List<Card> availableCards = new ArrayList<>((Collection<Card>) cards.findAll());
        if (exclusions.length > 0) {
            for (int exclusion : exclusions) {
                if (exclusion >= 0 && exclusion < availableCards.size()) {
                    availableCards.remove((int) exclusion);
                }
            }
        }
        room.setCards(availableCards);
        return room;
    }


}
