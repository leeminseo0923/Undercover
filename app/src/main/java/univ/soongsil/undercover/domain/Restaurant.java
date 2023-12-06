package univ.soongsil.undercover.domain;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity
public class Restaurant extends Place {

    @Ignore
    public Restaurant() {super();}

    public Restaurant(String name, Coordinate location, String region, Long minCost, Long maxCost) {
        super(name, location, region, minCost, maxCost);
    }

    public Restaurant(String name, Double longitude, Double latitude, String region, Long minCost, Long maxCost) {
        super(name, longitude, latitude, region, minCost, maxCost);
    }
}
