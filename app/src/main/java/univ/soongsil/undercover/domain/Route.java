package univ.soongsil.undercover.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Route {
    @PrimaryKey
    private Integer id;
    private boolean isActive;
    private List<String> names;
    private List<Coordinate> coordinates;

    public Route(List<String> names, List<Coordinate> coordinates, boolean isActive) {
        this.names = names;
        this.coordinates = coordinates;
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }
}
