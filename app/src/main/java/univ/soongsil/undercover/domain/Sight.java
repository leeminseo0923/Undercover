package univ.soongsil.undercover.domain;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity
public class Sight extends Place {

    @Ignore
    public Sight() {
        super();
    }

    public Sight(String name, Coordinate location, String region, Long minCost, Long maxCost) {
        super(name, location, region, minCost, maxCost);
    }

    public Sight(String name, Double longitude, Double latitude, String region, Long minCost, Long maxCost) {
        super(name, longitude, latitude, region, minCost, maxCost);
    }
}
