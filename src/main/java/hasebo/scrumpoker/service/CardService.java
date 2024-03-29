package hasebo.scrumpoker.service;

import hasebo.scrumpoker.repository.CardRepository;
import hasebo.scrumpoker.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final RoomRepository roomRepository;

    public CardService(CardRepository cardRepository, RoomRepository roomRepository) {
        this.cardRepository = cardRepository;
        this.roomRepository = roomRepository;
    }

//    public List<Card> getCardsByIds(List<Long> ids) {
//        return cardRepository.findAllById(ids);
//    }

}
