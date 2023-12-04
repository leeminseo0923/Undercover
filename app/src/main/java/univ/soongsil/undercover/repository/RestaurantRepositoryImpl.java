package univ.soongsil.undercover.repository;

import android.util.Pair;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
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
    public void getBestPlaces(Region region, List<Boolean> options, UpdateUI<List<String>> updateUI) {
        reference.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Pair<String, Double>> places = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Place place = documentSnapshot.toObject(Restaurant.class);
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
