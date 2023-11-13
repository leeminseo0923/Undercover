package univ.soongsil.undercover;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import univ.soongsil.undercover.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "REGISTER";

    private ActivityRegisterBinding binding;
    private FirebaseAuth auth;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.registerButton.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();
            String username = binding.nameEditText.getText().toString();

            Map<String, Object> user = new HashMap<>();
            user.put("name", username);

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        db.collection("users")
                                .add(user)
                                .addOnSuccessListener(documentReference -> Log.d(TAG, "success creating user"))
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "error creating user" + e.getMessage());
                                });
                        // TODO: move to main page
                    })
                    .addOnFailureListener(task -> Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show());
        });
    }

}