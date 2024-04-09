package hasebo.scrumpoker.service;

import hasebo.scrumpoker.model.Card;
import hasebo.scrumpoker.repository.CardRepository;
import hasebo.scrumpoker.repository.RoomRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final RoomRepository roomRepository;

    public CardService(CardRepository cardRepository, RoomRepository roomRepository) {
        this.cardRepository = cardRepository;
        this.roomRepository = roomRepository;
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

}
