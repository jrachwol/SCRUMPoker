package hasebo.scrumpoker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="deck")
public class Deck {

    @Id
    @GeneratedValue
    private Long id;
    private String figure;
}
