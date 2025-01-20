package hasebo.scrumpoker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="voting")
@NoArgsConstructor
public class Voting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private boolean active;

    public Voting(String title, String description, boolean active) {
        this.title = title;
        this.description = description;
        this.active = active;
    }

}
