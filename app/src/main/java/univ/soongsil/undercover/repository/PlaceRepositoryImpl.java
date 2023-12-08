package univ.soongsil.undercover.repository;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;

import java.util.List;

import univ.soongsil.undercover.domain.Coordinate;
import univ.soongsil.undercover.domain.Place;
import univ.soongsil.undercover.domain.Restaurant;
import univ.soongsil.undercover.domain.UpdateUI;

public abstract class PlaceRepositoryImpl implements PlaceRepository{

    public final String COLLECTION;
    protected final CollectionReference reference;
    protected final double DELTA = 0.00001;

    protected PlaceRepositoryImpl(String collection, CollectionReference reference) {
        this.COLLECTION = collection;
        this.reference = reference;
    }

    @Override
    public abstract void updateWeight(String placeName, List<Boolean> options, Double rate);


    @Override
    public void addPlace(Place place) {
        reference.document(place.getName())
                .set(place);
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
