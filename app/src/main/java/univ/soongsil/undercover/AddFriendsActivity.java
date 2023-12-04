package univ.soongsil.undercover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import univ.soongsil.undercover.databinding.ActivityAddFriendsBinding;
import univ.soongsil.undercover.fragment.AddFriendsFragment;

public class AddFriendsActivity extends AppCompatActivity {

    ActivityAddFriendsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.add_friends_frame, new AddFriendsFragment()).commit();
    }
}