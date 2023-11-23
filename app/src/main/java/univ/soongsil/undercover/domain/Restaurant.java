package univ.soongsil.undercover.domain;

public class Restaurant extends Place {

    public Restaurant(String name, Coordinate location) {
        super(name, location);
    }

    public Restaurant(String name, Double longitude, Double latitude) {
        super(name, longitude, latitude);
    }
}
