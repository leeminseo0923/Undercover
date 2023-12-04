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

    ActivitySettingBinding binding;
    private UserRepository userRepository;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
            userRepository = new UserRepositoryImpl();
            userRepository.logout();

            Intent intent = new Intent(getContext(), LoginActivity.class);
            assert getActivity() != null;
            getActivity().startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}