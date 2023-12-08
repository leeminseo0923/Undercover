package univ.soongsil.undercover.domain;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public abstract class Place {
    /**
     * location of the place. <br/>
     * Consist of longitude and latitude
     * @see Coordinate
     */
    @NonNull
    private Coordinate location;

    public void setLocation(@NonNull Coordinate location) {
        this.location = location;
    }

    /**
     * name of the place
     */
    @NonNull
    @PrimaryKey
    private String name;

    public void setName(@NonNull String name) {
        this.name = name;
    }

    /**
     * recommendation weights of the place <br/>
     * Its size is 10
     */
    @NonNull
    private List<Double> weights;
    /**
     * minimum cost of the place
     */
    private Long minCost;
    /**
     * maximum cost of the place
     */
    private Long maxCost;
    /**
     * region's name of the place
     */
    private String region;

    public void setRegion(String region) {
        this.region = region;
    }

    @Ignore
    public Place() {
        location = new Coordinate(0.0, 0.0);
        weights = new ArrayList<>();
        name = "";
    }

    /**
     * @param name a name of the place
     * @param location a coordinates of the place, it have longitude and latitude.
     */
    @Ignore
    public Place(
            @NonNull String name,
            @NonNull Coordinate location,
            @NonNull String region,
            @NonNull Long minCost,
            @NonNull Long maxCost) {
        this.name = name;
        this.location = location;
        this.region = region;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.weights = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            weights.add(0.1);
        }
    }

    public Place(
            @NonNull String name,
            @NonNull Coordinate location,
            @NonNull String region,
            @NonNull Long minCost,
            @NonNull Long maxCost,
            @NonNull List<Double> weights) {
        this.name = name;
        this.location = location;
        this.region = region;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.weights = weights;
    }

    /**
     * @param name a name of the place
     * @param longitude a longitude of the place
     * @param latitude a latitude of the place
     */
    @Ignore
    public Place(String name, Double longitude, Double latitude, String region, Long minCost, Long maxCost) {
        this(name, new Coordinate(longitude, latitude), region, minCost, maxCost);
    }

    @NonNull
    public String getName() {
        return name;
    }

    public Long getMinCost() {
        return minCost;
    }

    public void setMinCost(Long minCost) {
        this.minCost = minCost;
    }

    public Long getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(Long maxCost) {
        this.maxCost = maxCost;
    }

    public String getRegion() {
        return this.region;
    }

    @NonNull
    public Coordinate getLocation() {
        return location;
    }

    @NonNull
    public List<Double> getWeights() {
        return weights;
    }

    public void setWeights(@NonNull List<Double> weights) {
        this.weights = weights;
    }

    public void setLocation(Double longitude, Double latitude) {
        location.setLongitude(longitude);
        location.setLatitude(latitude);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return weights.equals(place.weights) && location.equals(place.location) && name.equals(place.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weights, location, name);
    }
}
