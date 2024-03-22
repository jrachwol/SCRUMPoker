package hasebo.scrumpoker.service;

import hasebo.scrumpoker.model.TextGenerated;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RandomTextService {
   public TextGenerated generateRandomText () {
        return new TextGenerated(UUID.randomUUID().toString().replace("-", ""));
    }

}
