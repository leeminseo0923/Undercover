package univ.soongsil.undercover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import univ.soongsil.undercover.databinding.ActivityBalanceGameBinding;
import univ.soongsil.undercover.fragment.BalanceGameFragment;

public class BalanceGameActivity extends AppCompatActivity {

    ActivityBalanceGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBalanceGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.balance_game_frame, new BalanceGameFragment()).commit();
    }
}