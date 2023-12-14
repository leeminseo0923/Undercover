package univ.soongsil.undercover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Toast.makeText(this, "밸런스게임을 먼저 완료해주세요!", Toast.LENGTH_SHORT).show();
        return keyCode == KeyEvent.KEYCODE_BACK;
    }
}