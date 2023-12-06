package univ.soongsil.undercover;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

import univ.soongsil.undercover.databinding.ActivityRegisterBinding;
import univ.soongsil.undercover.domain.UpdateUI;
import univ.soongsil.undercover.domain.User;
import univ.soongsil.undercover.repository.UserRepository;
import univ.soongsil.undercover.repository.UserRepositoryImpl;

public class RegisterActivity extends AppCompatActivity {

    private static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String REGEX_PASSWORD = "^.{8,13}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.registerButton.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();
            String name = binding.nameEditText.getText().toString();

            if (email.equals("")) {
                makeToast("이메일을 입력해주세요.");
                return;
            }
            if (password.equals("")) {
                makeToast("비밀번호를 입력해주세요.");
                return;
            }
            if (name.equals("")) {
                makeToast("이름을 입력해주세요.");
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

            User user = new User(name, email);
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
                    Toast.makeText(RegisterActivity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}