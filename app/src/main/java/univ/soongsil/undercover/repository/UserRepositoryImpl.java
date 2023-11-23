package univ.soongsil.undercover.repository;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import univ.soongsil.undercover.domain.UpdateUI;

public class UserRepositoryImpl implements UserRepository {
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final String COLLECTION = "user";

    public UserRepositoryImpl() {
    }

    @Override
    public void register(String email, String password, UpdateUI<FirebaseUser> updateUI) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    if (user != null) {
                        Log.d(COLLECTION, "User register is successful : " + user.getUid());
                        updateUI.onSuccess(user);
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
                        updateUI.onFail();
                    }
                })
                .addOnFailureListener(e -> {
                    String message = e.getMessage();
                    if (message != null) {
                        Log.e(COLLECTION, message);
                    } else {
                        Log.e(COLLECTION, "Login is failed");
                    }
                });
    }
}
