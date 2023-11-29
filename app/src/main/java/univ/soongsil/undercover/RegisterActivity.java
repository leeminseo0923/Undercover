package univ.soongsil.undercover;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import univ.soongsil.undercover.databinding.ActivityRegisterBinding;
import univ.soongsil.undercover.domain.UpdateUI;
import univ.soongsil.undercover.domain.User;
import univ.soongsil.undercover.repository.UserRepository;
import univ.soongsil.undercover.repository.UserRepositoryImpl;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "REGISTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseAuth auth = FirebaseAuth.getInstance();

        binding.registerButton.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();
            String username = binding.nameEditText.getText().toString();

            User user = new User(username, email);
            UserRepository userRepository = new UserRepositoryImpl();

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");

                            userRepository.addUserDocument(userRepository.getCurrentUser().getUid(), user);

                            Intent intent = new Intent(RegisterActivity.this, BalanceGameActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(task ->
                            Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show());
        });
    }
}