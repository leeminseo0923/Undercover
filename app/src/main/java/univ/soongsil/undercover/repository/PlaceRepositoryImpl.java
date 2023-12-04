package univ.soongsil.undercover.repository;

import android.util.Log;
import android.util.Pair;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import univ.soongsil.undercover.domain.Coordinate;
import univ.soongsil.undercover.domain.Place;
import univ.soongsil.undercover.domain.Region;
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

    @Override
    public void getBestPlaces(Region region, List<Boolean> options, UpdateUI<List<String>> updateUI) {
        reference.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Pair<String, Double>> places = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Place place = documentSnapshot.toObject(Place.class);
                        Double sum = 0.0;
                        for (int i = 0; i < options.size(); i++) {
                            if (options.get(i)) {
                                sum += place.getWeights().get(i);
                            }
                        }
                        places.add(new Pair<>(place.getName(), sum));
                    }

                    places.sort((o1, o2) -> (int) (o1.second - o2.second));

                    updateUI.onSuccess(places.stream().map(stringDoublePair -> stringDoublePair.first).collect(Collectors.toList()));
                })
                .addOnFailureListener((e)->updateUI.onFail());
    }
}
