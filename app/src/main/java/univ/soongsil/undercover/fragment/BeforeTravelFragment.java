package univ.soongsil.undercover.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import univ.soongsil.undercover.databinding.ActivityBeforeTravelBinding;

public class BeforeTravelFragment extends Fragment {

    ActivityBeforeTravelBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = ActivityBeforeTravelBinding.inflate(inflater);
        return binding.getRoot();
    }

}
