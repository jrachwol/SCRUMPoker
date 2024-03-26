package hasebo.scrumpoker.service;

import hasebo.scrumpoker.model.Card;
import hasebo.scrumpoker.repository.CardRepository;
import hasebo.scrumpoker.repository.RoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardService {

    private final CardRepository cardRepository;
    private final RoomRepository roomRepository;

    public CardService(CardRepository cardRepository, RoomRepository roomRepository) {
        this.cardRepository = cardRepository;
        this.roomRepository = roomRepository;
    }


}
