package univ.soongsil.undercover.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.regex.Pattern;

import univ.soongsil.undercover.LoginActivity;
import univ.soongsil.undercover.R;
import univ.soongsil.undercover.SplashActivity;
import univ.soongsil.undercover.databinding.FragmentEditInfoBinding;
import univ.soongsil.undercover.repository.UserRepository;
import univ.soongsil.undercover.repository.UserRepositoryImpl;

public class EditInfoFragment extends Fragment {

    private static final String TAG = "EDIT_INFO";
    FragmentEditInfoBinding binding;

    private FirebaseUser user;
    private UserRepository userRepository;

    private static final String REGEX_PASSWORD = "^.{8,13}$";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditInfoBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userRepository = new UserRepositoryImpl();
        user = userRepository.getCurrentUser();
        String email = userRepository.getCurrentUser().getEmail();
        binding.userEmail.setText(email);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());

                            String name = (String) document.getData().get("name");
                            binding.editName.setHint(name);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        binding.backButton.setOnClickListener(v ->
                getParentFragmentManager().beginTransaction().replace(R.id.main_frame, new SettingFragment()).commit());

        binding.newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String confirmPassword = binding.confirmPassword.getText().toString();
                int red = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.red);
                int green = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.green);

                if (confirmPassword.equals("")) {
                    binding.passwordMatch.setText("");
                } else if (s.toString().equals(confirmPassword)) {
                    binding.passwordMatch.setText("새 비밀번호와 일치합니다.");
                    binding.passwordMatch.setTextColor(green);
                } else {
                    binding.passwordMatch.setText("새 비밀번호와 일치하지 않습니다.");
                    binding.passwordMatch.setTextColor(red);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newPassword = binding.newPassword.getText().toString();

                if (s.toString().equals("")) {
                    binding.passwordMatch.setText("");
                } else if (s.toString().equals(newPassword)) {
                    binding.passwordMatch.setText("새 비밀번호와 일치합니다.");
                    binding.passwordMatch.setTextColor(Color.parseColor("#0AAC50")); // green
                } else {
                    binding.passwordMatch.setText("새 비밀번호와 일치하지 않습니다.");
                    binding.passwordMatch.setTextColor(Color.parseColor("#F44039")); // red
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.editCompleteButton.setOnClickListener(v -> {
            String editName = binding.editName.getText().toString();
            String currentPassword = binding.currentPassword.getText().toString();
            String newPassword = binding.newPassword.getText().toString();
            String confirmPassword = binding.confirmPassword.getText().toString();

            if (currentPassword.equals("")) {
                makeToast("현재 비밀번호를 입력해주세요.");
                return;
            } else if (newPassword.equals("")) {
                makeToast("새 비밀번호를 입력해주세요.");
                return;
            } else if (confirmPassword.equals("")) {
                makeToast("새 비밀번호를 한 번 더 입력해주세요.");
                return;
            }

            if (!Pattern.matches(REGEX_PASSWORD, newPassword)) {
                makeToast("비밀번호 형식이 올바르지 않습니다.");
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                makeToast("새 비밀번호와 일치하지 않습니다.");
                return;
            }

            AuthCredential credential = EmailAuthProvider.getCredential(email, currentPassword);

            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User re-authenticated.");

                            if (currentPassword.equals(newPassword)) {
                                makeToast("현재 비밀번호와 새 비밀번호가 일치하여 수정할 수 없습니다.");
                                return;
                            }

                            if (!editName.equals("")) {
                                userRepository.updateUserName(user.getUid(), editName);
                            }

                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(t -> {
                                        if (t.isSuccessful()) {
                                            Log.d(TAG, "User password updated.");
                                            makeToast("회원 정보가 변경되었습니다.\n로그아웃합니다.");

                                            userRepository.logout();
                                            Intent intent = new Intent(getContext(), LoginActivity.class);
                                            assert getActivity() != null;
                                            getActivity().startActivity(intent);
                                        }
                                    });
                        } else {
                            Log.e(TAG, "Re-authentication failed.", task.getException());
                            makeToast("현재 비밀번호가 일치하지 않습니다.");
                        }
                    });
        });

        binding.deleteAccountButton.setOnClickListener(v -> {
            EditText editText = new EditText(getContext());
            editText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            AlertDialog deleteAccountDialog = new AlertDialog.Builder(getContext())
                    .setTitle("정말 탈퇴하시겠어요?\uD83E\uDD7A")
                    .setMessage("탈퇴 시 계정은 삭제되며 복구되지 않습니다.\n정말 탈퇴하시려면 비밀번호를 입력해주세요.")
                    .setView(editText)
                    .setPositiveButton("네", (dialog, which) -> {
                        String myEmail = user.getEmail();
                        String myPassword = editText.getText().toString();
                        if (myPassword.equals("")) {
                            makeToast("탈퇴가 취소되었습니다.");
                            return;
                        }

                        AuthCredential credential = EmailAuthProvider.getCredential(myEmail, myPassword);

                        user.reauthenticate(credential)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User re-authenticated.");

                                        user.delete()
                                                .addOnCompleteListener(t -> {
                                                    if (t.isSuccessful()) {
                                                        Log.d(TAG, "User account deleted.");
                                                        makeToast("UNDER COVER\n계정 탈퇴가 완료되었습니다.");
                                                        userRepository.deleteUserDocument(user.getUid());

                                                        Intent intent = new Intent(getContext(), SplashActivity.class);
                                                        assert getActivity() != null;
                                                        getActivity().startActivity(intent);
                                                    } else {
                                                        Log.e(TAG, "Account delete failure: " + t.getException());
                                                        makeToast("탈퇴가 취소되었습니다.");
                                                    }
                                                });
                                    } else {
                                        Log.e(TAG, "Re-authentication failed.", task.getException());
                                        makeToast("비밀번호가 일치하지 않습니다.");
                                    }
                                });
                    })
                    .setNegativeButton("아니요", (dialog, which) -> makeToast("탈퇴가 취소되었습니다."))
                    .create();

            deleteAccountDialog.show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void makeToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}