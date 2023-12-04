package univ.soongsil.undercover;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import univ.soongsil.undercover.databinding.ActivityRegisterBinding;
import univ.soongsil.undercover.domain.UpdateUI;
import univ.soongsil.undercover.domain.User;
import univ.soongsil.undercover.repository.UserRepository;
import univ.soongsil.undercover.repository.UserRepositoryImpl;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.registerButton.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();
            String username = binding.nameEditText.getText().toString();

            User user = new User(username, email);
            UserRepository userRepository = new UserRepositoryImpl();

            userRepository.register(email, password, new UpdateUI<FirebaseUser>() {
                @Override
                public void onSuccess(FirebaseUser result) {
                    userRepository.setUserDocument(userRepository.getCurrentUser().getUid(), user);
                    Intent intent = new Intent(RegisterActivity.this, BalanceGameActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFail() {
                    Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}