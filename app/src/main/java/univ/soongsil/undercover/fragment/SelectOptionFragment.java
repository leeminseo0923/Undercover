package univ.soongsil.undercover.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import univ.soongsil.undercover.databinding.ActivitySelectOptionBinding;

public class SelectOptionFragment extends Fragment {
    ActivitySelectOptionBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivitySelectOptionBinding.inflate(inflater);
        return binding.getRoot();
    }


}
