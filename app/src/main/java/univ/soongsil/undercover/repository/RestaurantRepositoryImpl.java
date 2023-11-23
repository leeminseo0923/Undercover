package univ.soongsil.undercover.repository;

import com.google.firebase.firestore.FirebaseFirestore;

public class RestaurantRepositoryImpl extends PlaceRepositoryImpl {
    public static final String COLLECTION = "restaurant";
    public RestaurantRepositoryImpl() {
        super(COLLECTION, FirebaseFirestore.getInstance().collection(COLLECTION));
    }
}
