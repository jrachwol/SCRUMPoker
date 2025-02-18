package hasebo.scrumpoker.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "card")
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String figure;

    @ManyToMany(mappedBy = "cards")
    private List<Room> rooms = new ArrayList<>();

    public Card(Long id, String figure) {
        this.id = id;
        this.figure = figure;
    }

    public Long getId() {
        return id;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
