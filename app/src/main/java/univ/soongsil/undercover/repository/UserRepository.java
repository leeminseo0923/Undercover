package univ.soongsil.undercover.repository;


import com.google.firebase.auth.FirebaseUser;

import univ.soongsil.undercover.domain.UpdateUI;

public interface UserRepository {
    void register(String email, String password, UpdateUI<FirebaseUser> updateUI);
    void login(String email, String password, UpdateUI<FirebaseUser> updateUI);
}
