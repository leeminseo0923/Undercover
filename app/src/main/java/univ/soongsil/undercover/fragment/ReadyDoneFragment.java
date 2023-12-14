package univ.soongsil.undercover.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.room.Room;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import univ.soongsil.undercover.R;
import univ.soongsil.undercover.databinding.ActivityReadyDoneBinding;
import univ.soongsil.undercover.domain.Coordinate;
import univ.soongsil.undercover.domain.Restaurant;
import univ.soongsil.undercover.domain.Route;
import univ.soongsil.undercover.domain.Sight;
import univ.soongsil.undercover.repository.AppDatabase;
import univ.soongsil.undercover.repository.RestaurantDao;
import univ.soongsil.undercover.repository.RouteDao;
import univ.soongsil.undercover.repository.SightDao;

public class ReadyDoneFragment extends Fragment {
    ActivityReadyDoneBinding binding;

    AppDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityReadyDoneBinding.inflate(inflater);
        if (container != null)
            database = Room.databaseBuilder(container.getContext(), AppDatabase.class, "undercover.db").build();
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView Selected_location = binding.selectedLocation;
        TextView Selected_cost = binding.selectedCost;


        //SelectOptionFragment로 부터 지역 데이터와 가격 데이터를 받아와서 Selected_location, Selected_cost 값을 사용자의 입력에 따라 달라지게 설정
        getParentFragmentManager().setFragmentResultListener("지역requestkey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                String locationText = bundle.getString("지역");
                if(locationText=="서울"){
                    binding.SeoulScroolView.setVisibility(View.VISIBLE);
                }
                else if(locationText=="부산"){
                    binding.BusanScroolView.setVisibility(View.VISIBLE);
                }
                else if(locationText=="제주"){
                    binding.JejuScroolView.setVisibility(View.VISIBLE);
                }
                Selected_location.setText(locationText);
            }
        });

        getParentFragmentManager().setFragmentResultListener("가격requestkey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                int costText = bundle.getInt("가격");
                Selected_cost.setText(String.valueOf(costText));
            }
        });

        binding.redayGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread() {
                    @Override
                    public void run() {
                        Log.d("READY", "clicked");

                        RouteDao routeDao = database.routeDao();

                        RestaurantDao restaurantDao = database.restaurantDao();
                        SightDao sightDao = database.sightDao();

                        List<Restaurant> restaurants = restaurantDao.getAll();
                        List<Sight> sights = sightDao.getAll();

                        ArrayList<String> names = new ArrayList<>();
                        ArrayList<Coordinate> coordinates = new ArrayList<>();

                        for (int i = 0; i < restaurants.size(); i++) {
                            Restaurant restaurant = restaurants.get(i);
                            Sight sight = sights.get(i);

                            names.add(restaurant.getName());
                            names.add(sight.getName());

                            coordinates.add(restaurant.getLocation());
                            coordinates.add(sight.getLocation());
                        }

                        routeDao.insert(new Route(names, coordinates,
                                Selected_location.getText().toString(),
                                new Date().toString(),true));

                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, RouteFragment.newInstance(names, coordinates))
                                .commit();

                    }
                }.start();


            }
        });

        binding.redayNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.main_frame, new SelectOptionFragment()).commit();
            }
        });

        binding.selectHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.readyDone.setVisibility(View.GONE);
                binding.selectHotel1.setVisibility(View.VISIBLE);

            }
        });

        binding.backbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.readyDone.setVisibility(View.VISIBLE);
                binding.selectHotel1.setVisibility(View.GONE);
            }
        });
    }
}
