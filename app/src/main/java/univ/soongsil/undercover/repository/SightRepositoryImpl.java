package univ.soongsil.undercover.repository;

import com.google.firebase.firestore.FirebaseFirestore;

public class SightRepositoryImpl extends PlaceRepositoryImpl {

    public static final String COLLECTION = "sight";

    public SightRepositoryImpl() {
        super(COLLECTION, FirebaseFirestore.getInstance().collection(COLLECTION));
    }
}
