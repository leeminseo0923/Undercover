package univ.soongsil.undercover.repository;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import univ.soongsil.undercover.domain.UpdateUI;
import univ.soongsil.undercover.domain.User;

public interface UserRepository {
    void register(String email, String password, UpdateUI<FirebaseUser> updateUI);
    void login(String email, String password, UpdateUI<FirebaseUser> updateUI);
    void logout();
    FirebaseUser getCurrentUser();
    void setUserDocument(String uID, User user);
    void updateUserOptions(String uID, List<Boolean> options);
    void addUserFriend(String uID, String friendUid);
    void deleteUserFriend(String uID, String friendUid);
    void addUserFriendRequest(String friendUid, String uID);
    void deleteUserFriendRequest(String uID, String friendUid);
}
