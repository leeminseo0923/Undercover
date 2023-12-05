package univ.soongsil.undercover.repository;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

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
                    if (message != null) {
                        Log.e(COLLECTION, message);
                    } else {
                        Log.e(COLLECTION, "Register is failed");
                    }
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
                    updateUI.onFail();
                });
    }

    @Override
    public void logout() {
        auth.signOut();
        if (auth.getCurrentUser() == null)
            Log.d(COLLECTION, "User logout is successful");
        else
            Log.d(COLLECTION, "Logout is failed");
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    @Override
    public void setUserDocument(String uID, User user) {
        repository.collection(COLLECTION)
                .document(uID)
                .set(user)
                .addOnSuccessListener(
                        z -> Log.d(COLLECTION, "Success to add document")
                );
    }

    @Override
    public void updateUserOptions(String uID, List<Boolean> options) {
        repository.collection(COLLECTION)
                .document(uID)
                .update("options", options)
                .addOnSuccessListener(aVoid ->
                        Log.d(COLLECTION, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e ->
                        Log.w(COLLECTION, "Error updating document", e));
    }

    @Override
    public void addUserFriend(String uID, String friendUid) {
        repository.collection(COLLECTION)
                .document(uID)
                .update("friends", FieldValue.arrayUnion(friendUid))
                .addOnSuccessListener(aVoid ->
                        Log.d(COLLECTION, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e ->
                        Log.w(COLLECTION, "Error updating document", e));
    }

    @Override
    public void deleteUserFriend(String uID, String friendUid) {
        repository.collection(COLLECTION)
                .document(uID)
                .update("friends", FieldValue.arrayRemove(friendUid))
                .addOnSuccessListener(aVoid ->
                        Log.d(COLLECTION, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e ->
                        Log.w(COLLECTION, "Error updating document", e));
    }

    @Override
    public void addUserFriendRequest(String friendUid, String uID) {
        repository.collection(COLLECTION)
                .document(friendUid)
                .update("friendRequests", FieldValue.arrayUnion(uID))
                .addOnSuccessListener(aVoid ->
                        Log.d(COLLECTION, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e ->
                        Log.w(COLLECTION, "Error updating document", e));
    }

    @Override
    public void deleteUserFriendRequest(String uID, String friendUid) {
        repository.collection(COLLECTION)
                .document(uID)
                .update("friendRequests", FieldValue.arrayRemove(friendUid))
                .addOnSuccessListener(aVoid ->
                        Log.d(COLLECTION, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e ->
                        Log.w(COLLECTION, "Error updating document", e));
    }

    @Override
    public void updateUserName(String uID, String name) {
        repository.collection(COLLECTION)
                .document(uID)
                .update("name", name)
                .addOnSuccessListener(aVoid ->
                        Log.d(COLLECTION, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e ->
                        Log.w(COLLECTION, "Error updating document", e));
    }
}
