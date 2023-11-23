package univ.soongsil.undercover.domain;

public class Sight extends Place {

    public Sight(String name, Coordinate location) {
        super(name, location);
    }

    public Sight(String name, Double longitude, Double latitude) {
        super(name, longitude, latitude);
    }
}
