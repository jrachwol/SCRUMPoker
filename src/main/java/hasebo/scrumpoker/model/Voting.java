package hasebo.scrumpoker.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="voting")
public class Voting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private boolean active;

//  dodaj głosy uczestników głosowania nazywanych member zapisywanych w Vote
    @OneToMany(mappedBy = "voting")
    private List<Vote> votes;

    public Voting(String title, String description, boolean active) {
        this.title = title;
        this.description = description;
        this.active = active;
    }

    public Voting() {
    }
}
