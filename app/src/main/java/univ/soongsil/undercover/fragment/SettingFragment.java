package univ.soongsil.undercover.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import univ.soongsil.undercover.LoginActivity;
import univ.soongsil.undercover.R;
import univ.soongsil.undercover.databinding.ActivitySettingBinding;
import univ.soongsil.undercover.repository.UserRepository;
import univ.soongsil.undercover.repository.UserRepositoryImpl;

public class SettingFragment extends Fragment {

    private static final String TAG = "EmailPassword";
    ActivitySettingBinding binding;
    UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Fragment에서 viewBinding 이용하기
        binding = ActivitySettingBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.editInformationButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_frame, new EditInfoFragment()).commit();
        });

        binding.travelOptionButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_frame, new TravelOptionFragment()).commit();
        });

        binding.logoutButton.setOnClickListener(v -> {
            signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            assert getActivity() != null;
            getActivity().startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // binding 객체를 Garbage Collector가 회수하도록 하기 위해 참조를 끊음
    }

    private void signOut() {
        userRepository.signOut();

        // Check if there is no current user.
        if (userRepository.getCurrentUser() == null)
            Log.d(TAG, "signOut:success");
        else
            Log.d(TAG, "signOut:failure");
    }
}