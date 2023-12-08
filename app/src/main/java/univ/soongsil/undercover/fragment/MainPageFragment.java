package univ.soongsil.undercover.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import univ.soongsil.undercover.R;
import univ.soongsil.undercover.databinding.ActivityMainPageBinding;
import univ.soongsil.undercover.domain.Route;
import univ.soongsil.undercover.repository.AppDatabase;
import univ.soongsil.undercover.repository.RouteDao;

public class MainPageFragment extends Fragment {
    ActivityMainPageBinding binding;
    AppDatabase database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() != null)
            database = Room.databaseBuilder(getContext(), AppDatabase.class, "undercover.db").build();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = ActivityMainPageBinding.inflate(inflater);
        return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.gotravle.setOnClickListener(v -> {
            RouteDao routeDao = database.routeDao();
            new Thread() {
                @Override
                public void run() {
                    Route activity = routeDao.getActivity();
                    if (activity == null)
                        getParentFragmentManager().beginTransaction().replace(R.id.main_frame, new SelectOptionFragment()).commit();
                    else
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_frame, RouteFragment.newInstance(activity))
                                .commit();
                }
            }.start();
        });

        binding.beforetravel.setOnClickListener(
                v -> getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frame, new BeforeTravelFragment())
                        .commit());


    }

}