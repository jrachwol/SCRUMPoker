package hasebo.scrumpoker.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
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
    @JoinColumn(name = "card_id", nullable = true)
    private Card vote;

    @ManyToOne
    @JoinColumn(name = "voting_id", nullable = false)
    private Voting voting;

    public Vote(Member voter, Room room, Card vote) {
        this.voter = voter;
        this.room = room;
        this.vote = vote;
    }

    public Member getVoter() {
        return voter;
    }

    public void setVoter(Member voter) {
        this.voter = voter;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Card getVote() {
        return vote;
    }

    public void setVote(Card vote) {
        this.vote = vote;
    }

    public Voting getVoting() {
        return voting;
    }

    public void setVoting(Voting voting) {
        this.voting = voting;
    }
}
