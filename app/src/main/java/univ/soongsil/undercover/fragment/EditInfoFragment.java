package univ.soongsil.undercover.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import univ.soongsil.undercover.LoginActivity;
import univ.soongsil.undercover.databinding.FragmentEditInfoBinding;
import univ.soongsil.undercover.domain.UpdateUI;
import univ.soongsil.undercover.repository.UserRepository;
import univ.soongsil.undercover.repository.UserRepositoryImpl;

public class EditInfoFragment extends Fragment {

    private static final String TAG = "EDIT_INFO";
    FragmentEditInfoBinding binding;

    UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditInfoBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String email = userRepository.getCurrentUser().getEmail();
        binding.userEmail.setText(email);

        binding.newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String confirmPassword = binding.confirmPassword.getText().toString();

                if (confirmPassword.equals("")) {
                    binding.passwordMatch.setText("");
                } else if (s.toString().equals(confirmPassword)) {
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

            if (!newPassword.equals(confirmPassword)) {
                makeToast("새 비밀번호와 일치하지 않습니다.");
                return;
            }

            // 기존 비밀번호 확인을 위해 기존 비밀번호로 로그인 실행
            userRepository.login(email, currentPassword, new UpdateUI<FirebaseUser>() {
                @Override
                public void onSuccess(FirebaseUser result) {
                    FirebaseUser user = userRepository.getCurrentUser();
                    if (currentPassword.equals(newPassword)) {
                        makeToast("현재 비밀번호와 새 비밀번호가 일치하여 수정할 수 없습니다.");
                        return;
                    }

                    user.updatePassword(newPassword)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User password updated.");
                                    makeToast("비밀번호가 변경되었습니다.\n로그아웃합니다.");

                                    userRepository.logout();
                                    Intent intent = new Intent(getContext(), LoginActivity.class);
                                    assert getActivity() != null;
                                    getActivity().startActivity(intent);
                                }
                            });
                }

                @Override
                public void onFail() {
                    makeToast("현재 비밀번호가 일치하지 않습니다.");
                }
            });
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