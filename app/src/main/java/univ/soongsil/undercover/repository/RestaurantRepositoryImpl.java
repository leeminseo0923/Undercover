package univ.soongsil.undercover.repository;

import android.util.Pair;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import univ.soongsil.undercover.domain.Place;
import univ.soongsil.undercover.domain.Region;
import univ.soongsil.undercover.domain.Restaurant;
import univ.soongsil.undercover.domain.UpdateUI;

public class RestaurantRepositoryImpl extends PlaceRepositoryImpl {
    public static final String COLLECTION = "restaurant";
    public RestaurantRepositoryImpl() {
        super(COLLECTION, FirebaseFirestore.getInstance().collection(COLLECTION));
    }

    @Override
    public void getBestPlaces(
            Region region,
            List<Boolean> options,
            Integer maxCost,
            Integer count,
            UpdateUI<List<? extends Place>> updateUI
    ) {
        reference.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Pair<Restaurant, Double>> restaurants = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);
                        Double sum = 0.0;
                        for (int i = 0; i < options.size(); i++) {
                            if (options.get(i) && Objects.equals(restaurant.getRegion(), region.name())) {
                                sum += restaurant.getWeights().get(i);
                            }
                        }
                        restaurants.add(new Pair<>(restaurant, sum));
                    }

                    restaurants.sort((o1, o2) -> (int) (o1.second - o2.second));
                    int currentMaxCost = maxCost / 2;
                    List<Place> result = restaurants.stream()
                            .filter(placeDoublePair -> placeDoublePair.first.getMaxCost() <= currentMaxCost)
                            .limit(count)
                            .map(placeDoublePair -> placeDoublePair.first)
                            .collect(Collectors.toList());

                    updateUI.onSuccess(result);
                })
                .addOnFailureListener((e)->updateUI.onFail());
    }

    @Override
    public void updateWeight(String placeName, List<Boolean> options, Double rate) {
        reference.document(placeName)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Restaurant place = documentSnapshot.toObject(Restaurant.class);
                    if (place != null) {
                        List<Double> weights = place.getWeights();
                        Double sum = 0.0;
                        for (int i = 0; i < options.size(); i++) {
                            if (options.get(i)) {
                                sum += weights.get(i);
                            }
                        }
                        for (int i = 0; i < options.size(); i++) {
                            if (options.get(i)) {
                                weights.set(i, weights.get(i) - DELTA * (sum - rate));
                            }
                        }

                        place.setWeights(weights);
                        reference.document(placeName)
                                .set(place);
                    }
                });
    }
}
