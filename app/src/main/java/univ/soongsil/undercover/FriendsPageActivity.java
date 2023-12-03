package univ.soongsil.undercover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import univ.soongsil.undercover.databinding.ActivityFriendsPageBinding;
import univ.soongsil.undercover.fragment.FriendsPageFragment;

public class FriendsPageActivity extends AppCompatActivity {

    ActivityFriendsPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendsPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.friends_page_frame, new FriendsPageFragment()).commit();
    }
}