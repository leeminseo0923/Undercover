package univ.soongsil.undercover.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import univ.soongsil.undercover.R;
import univ.soongsil.undercover.adapter.PreviousTravelAdapter;
import univ.soongsil.undercover.databinding.FragmentPreviousTravelBinding;
import univ.soongsil.undercover.domain.Route;
import univ.soongsil.undercover.repository.AppDatabase;
import univ.soongsil.undercover.repository.RouteDao;

public class PreviousTravelFragment extends Fragment {
    FragmentPreviousTravelBinding binding;

    AppDatabase database;

    public static final String ROUTE = "route";
    List<Route> routes;

    public static PreviousTravelFragment newInstance(ArrayList<Route> routes) {
        PreviousTravelFragment previousTravelFragment = new PreviousTravelFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ROUTE, routes);
        previousTravelFragment.setArguments(bundle);
        return previousTravelFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            routes = getArguments().getParcelableArrayList(ROUTE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPreviousTravelBinding.inflate(inflater);

        binding.backButton.setOnClickListener((v) -> getParentFragmentManager().beginTransaction()
                .replace(R.id.main_frame, new MainPageFragment())
                .commit());

        binding.container.setAdapter(new PreviousTravelAdapter(routes));
        binding.container.setLayoutManager(new GridLayoutManager(getContext(), 3));

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
