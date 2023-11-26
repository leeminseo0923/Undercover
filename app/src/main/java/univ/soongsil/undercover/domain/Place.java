package univ.soongsil.undercover.domain;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Place {
    @NonNull
    private final Coordinate location;
    @NonNull
    private final String name;
    @NonNull
    private List<Double> weights;
    private Long minCost;
    private Long maxCost;
    private String region;

    public Place(@NonNull String name, @NonNull Coordinate location) {
        this.name = name;
        this.location = location;
        weights = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            weights.add(0.1);
        }
    }

    public Place(String name, Double longitude, Double latitude) {
        this(name, new Coordinate(longitude, latitude));
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

    public Region getRegion() {
        return Region.valueOf(this.region);
    }

    public void setRegion(Region region) {
        this.region = region.name();
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
