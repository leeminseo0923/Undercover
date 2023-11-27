package univ.soongsil.undercover;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import univ.soongsil.undercover.databinding.ActivityLoginBinding;
import univ.soongsil.undercover.domain.UpdateUI;
import univ.soongsil.undercover.repository.UserRepository;
import univ.soongsil.undercover.repository.UserRepositoryImpl;

public class LoginActivity extends AppCompatActivity {

    private UserRepository userRepository;
    ActivityLoginBinding binding;
    private long prevBackTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - prevBackTime <= 2000) {
                moveTaskToBack(true);
            }
            prevBackTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUserRepository();

        binding.loginButton.setOnClickListener(v -> {
            if (binding.emailEditText.getText().toString().equals("")) {
                Toast.makeText(LoginActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binding.passwordEditText.getText().toString().equals("")) {
                Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            String email = binding.emailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();

            signIn(email, password);
        });

        binding.registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = userRepository.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }

        binding.emailEditText.setText("");
        binding.passwordEditText.setText("");
    }

    private void initUserRepository() {
        // Initialize User repository
        userRepository = new UserRepositoryImpl();
    }

    private void signIn(String email, String password) {
        userRepository.login(email, password, new UpdateUI<FirebaseUser>() {
            @Override
            public void onSuccess(FirebaseUser result) {
                updateUI(result);
            }

            @Override
            public void onFail() {
                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            finish();
        }
    }
}

