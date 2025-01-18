package hasebo.scrumpoker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="vote")
@NoArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "voter_id", nullable = false)
    private Member voter;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "vote_id", nullable = true)
    private Card vote;

    @ManyToOne
    @JoinColumn(name = "voting_id", nullable = false)
    private Voting voting;

    public Vote(Member voter, Room room, Card vote) {
        this.voter = voter;
        this.room = room;
        this.vote = vote;
    }

}
