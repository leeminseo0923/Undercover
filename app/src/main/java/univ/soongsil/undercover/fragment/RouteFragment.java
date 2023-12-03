package univ.soongsil.undercover.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import univ.soongsil.undercover.R;
import univ.soongsil.undercover.databinding.FragmentRouteBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RouteFragment extends Fragment {

    private static final String ARG_HISTORY = "history";
    private static final String ARG_PROGRESS = "progress";
    FragmentRouteBinding binding;

    private List<String> histories;
    private List<Integer> progresses;
    public RouteFragment() {
        // Required empty public constructor
    }

    public static RouteFragment newInstance(ArrayList<String> histories, ArrayList<Integer> progresses) {
        RouteFragment fragment = new RouteFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_HISTORY, histories);
        args.putIntegerArrayList(ARG_PROGRESS, progresses);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            histories = getArguments().getStringArrayList(ARG_HISTORY);
            progresses = getArguments().getIntegerArrayList(ARG_PROGRESS);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRouteBinding.inflate(getLayoutInflater());
        binding.bar.setHistories(histories, progresses);
        binding.tempNext.setOnClickListener((v -> binding.bar.next()));
        return binding.getRoot();
    }
}