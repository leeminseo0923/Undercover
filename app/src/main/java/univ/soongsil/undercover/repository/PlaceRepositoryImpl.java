package univ.soongsil.undercover.repository;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;

import univ.soongsil.undercover.domain.Coordinate;
import univ.soongsil.undercover.domain.Restaurant;
import univ.soongsil.undercover.domain.Sight;
import univ.soongsil.undercover.domain.UpdateUI;
import univ.soongsil.undercover.domain.User;

public abstract class PlaceRepositoryImpl implements PlaceRepository{

    public final String COLLECTION;
    private final CollectionReference reference;

    protected PlaceRepositoryImpl(String collection, CollectionReference reference) {
        this.COLLECTION = collection;
        this.reference = reference;
    }

    @Override
    public void updateWeight(String placeName, User user, Integer rate) {

    }

    @Override
    public void addPlace(String placeName, Coordinate coordinate) {
        Sight sight = new Sight(placeName, coordinate);
        reference.document(placeName)
                .set(sight);
    }

    @Override
    public void addPlace(String placeName, Double longitude, Double latitude) {
        Restaurant restaurant = new Restaurant(placeName, longitude, latitude);
        reference.document(placeName)
                .set(restaurant);
    }

    @Override
    public void getLocation(String placeName, UpdateUI<Coordinate> updateUI) {
        reference.document(placeName).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);
                    if (restaurant != null) {
                        Log.d(COLLECTION, "Success to get location : " + placeName + "-" + restaurant.getLocation());
                        updateUI.onSuccess(restaurant.getLocation());
                    } else {
                        Log.e(COLLECTION, "Fail to get location : " + placeName);
                        updateUI.onFail();
                    }
                })
                .addOnFailureListener(e -> Log.e(COLLECTION, "Fail to get location in " + placeName + " " + e.getMessage()));
    }
}
