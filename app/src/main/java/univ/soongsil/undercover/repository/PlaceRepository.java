package univ.soongsil.undercover.repository;

import java.util.List;

import univ.soongsil.undercover.domain.Coordinate;
import univ.soongsil.undercover.domain.Place;
import univ.soongsil.undercover.domain.Region;
import univ.soongsil.undercover.domain.UpdateUI;

public interface PlaceRepository {
    void updateWeight(String placeName, List<Boolean> options, Double rate);

    void addPlace(Place place);

    void getLocation(String placeName, UpdateUI<Coordinate> updateUI);

    void getBestPlaces(Region region, List<Boolean> options, UpdateUI<List<String>> updateUI);
}
