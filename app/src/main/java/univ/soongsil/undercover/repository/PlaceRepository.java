package univ.soongsil.undercover.repository;

import univ.soongsil.undercover.domain.Coordinate;
import univ.soongsil.undercover.domain.UpdateUI;
import univ.soongsil.undercover.domain.User;

public interface PlaceRepository {
    void updateWeight(String placeName, User user, Integer rate);

    void addPlace(String placeName, Coordinate coordinate);

    void addPlace(String placeName, Double longitude, Double latitude);

    void getLocation(String placeName, UpdateUI<Coordinate> updateUI);
}
