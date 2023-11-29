package univ.soongsil.undercover.repository;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import univ.soongsil.undercover.domain.UpdateUI;
import univ.soongsil.undercover.domain.User;

public class UserRepositoryImpl implements UserRepository {
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();
    public final FirebaseFirestore repository = FirebaseFirestore.getInstance();
    private static final String COLLECTION = "user";

    public UserRepositoryImpl() {
    }

    @Override
    public void register(String email, String password, UpdateUI<FirebaseUser> updateUI) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser != null) {
                        Log.d(COLLECTION, "User register is successful : " + firebaseUser.getUid());
                        updateUI.onSuccess(firebaseUser);
                    } else {
                        Log.e(COLLECTION, "User not found");
                        updateUI.onFail();
                    }
                })
                .addOnFailureListener(e -> {
                    String message = e.getMessage();
                    if (message != null)
                        Log.e(COLLECTION, message);
                    else
                        Log.e(COLLECTION, "Register is failed");
                    updateUI.onFail();
                });
    }

    @Override
    public void login(String email, String password, UpdateUI<FirebaseUser> updateUI) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    if (user != null) {
                        Log.d(COLLECTION, "User login is successful : " + user.getUid());
                        updateUI.onSuccess(user);
                    } else {
                        Log.e(COLLECTION, "User not found");
//                        updateUI.onFail();
                    }
                })
                .addOnFailureListener(e -> {
                    updateUI.onFail();
                    // 이 문장이 addOnSuccessListener의 else문 안에가 아니라
                    // 여기에 와야 정상작동하는 것 같습니다.

                    String message = e.getMessage();
                    if (message != null) {
                        Log.e(COLLECTION, message);
                    } else {
                        Log.e(COLLECTION, "Login is failed");
                    }
                });
    }

    @Override
    public void signOut() {
        auth.signOut();
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    @Override
    public void addUserDocument(String uID, User user) {
        repository.collection(COLLECTION)
                .document(uID)
                .set(user)
                .addOnSuccessListener(
                        z -> Log.d(COLLECTION, "Success to add document")
                );
    }
}
