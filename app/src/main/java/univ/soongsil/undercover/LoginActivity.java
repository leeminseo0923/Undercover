package univ.soongsil.undercover;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

import univ.soongsil.undercover.databinding.ActivityLoginBinding;
import univ.soongsil.undercover.domain.UpdateUI;
import univ.soongsil.undercover.repository.UserRepository;
import univ.soongsil.undercover.repository.UserRepositoryImpl;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private UserRepository userRepository;
    private long prevBackTime = 0;

    private static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String REGEX_PASSWORD = "^.{8,13}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = new UserRepositoryImpl();

        binding.loginButton.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();

            if (email.equals("")) {
                makeToast("이메일을 입력해주세요.");
                return;
            }
            if (password.equals("")) {
                makeToast("비밀번호를 입력해주세요.");
                return;
            }
            if (!Pattern.matches(REGEX_EMAIL, email)) {
                makeToast("이메일 형식이 올바르지 않습니다.");
                return;
            }
            if (!Pattern.matches(REGEX_PASSWORD, password)) {
                makeToast("비밀번호 형식이 올바르지 않습니다.");
                return;
            }

            userRepository.login(email, password, new UpdateUI<FirebaseUser>() {
                @Override
                public void onSuccess(FirebaseUser result) {
                    finish();
                }

                @Override
                public void onFail() {
                    makeToast("로그인에 실패하였습니다.");
                }
            });
        });

        binding.registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = userRepository.getCurrentUser();
        if (currentUser != null) {
            finish();
        }
        binding.emailEditText.setText("");
        binding.passwordEditText.setText("");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - prevBackTime <= 2000) {
                moveTaskToBack(true);
            }
            prevBackTime = System.currentTimeMillis();
            makeToast("'뒤로'버튼 한번 더 누르시면 종료됩니다.");
            return true;
        }
        return false;
    }

    public void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}

