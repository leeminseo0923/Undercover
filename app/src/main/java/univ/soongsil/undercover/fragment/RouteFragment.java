package univ.soongsil.undercover.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.room.Room;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.ArrayList;
import java.util.List;

import univ.soongsil.undercover.R;
import univ.soongsil.undercover.databinding.DialogRatingBinding;
import univ.soongsil.undercover.databinding.FragmentRouteBinding;
import univ.soongsil.undercover.domain.Coordinate;
import univ.soongsil.undercover.domain.Restaurant;
import univ.soongsil.undercover.domain.Route;
import univ.soongsil.undercover.domain.Sight;
import univ.soongsil.undercover.receiver.GeofenceBroadcastReceiver;
import univ.soongsil.undercover.repository.AppDatabase;
import univ.soongsil.undercover.repository.PlaceRepository;
import univ.soongsil.undercover.repository.RestaurantDao;
import univ.soongsil.undercover.repository.RestaurantRepositoryImpl;
import univ.soongsil.undercover.repository.RouteDao;
import univ.soongsil.undercover.repository.SightDao;
import univ.soongsil.undercover.repository.SightRepositoryImpl;
import univ.soongsil.undercover.repository.UserRepository;
import univ.soongsil.undercover.repository.UserRepositoryImpl;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RouteFragment extends Fragment {

    public static final String TAG = "ROUTE";
    private static final String ARG_HISTORY = "history";
    private static final String ARG_LOCATION = "location";
    public FragmentRouteBinding binding;
    GeofencingClient geofencingClient;
    private List<String> histories;
    private List<Coordinate> coordinates;
    private List<Geofence> geofenceList;
    private PendingIntent geofencePendingIntent;
    private AppDatabase database;
    private PlaceRepository sightRepository;
    private PlaceRepository restaurantRepository;
    private UserRepository userRepository;

    public class GeofenceResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<String> names = intent.getStringArrayListExtra("names");

            int action = intent.getIntExtra("action", 0);

            if (names == null || action == 0) {
                Log.e(TAG, "extra is null");
                return;
            }
            for (int i = 0; i < names.size(); i++){
                String name = names.get(i);

                if (name.equals(binding.bar.getCurrentHistory())
                        && action == Geofence.GEOFENCE_TRANSITION_EXIT) {
                    Log.d(TAG, "EXIT " + name);
                    binding.label.setText("이동중...");
                }
                if (name.equals(binding.bar.getNextHistory()) && action == Geofence.GEOFENCE_TRANSITION_ENTER) {
                    Log.d(TAG, "ENTER " + name);
                    binding.bar.next();
                    binding.label.setText("목적지에 도착했습니다!");
                }
            }
        }
    }

    public RouteFragment() {
        // Required empty public constructor
    }

    public static RouteFragment newInstance(ArrayList<String> histories, ArrayList<Coordinate> coordinates) {
        RouteFragment fragment = new RouteFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_HISTORY, histories);
        args.putParcelableArrayList(ARG_LOCATION, coordinates);
        fragment.setArguments(args);
        return fragment;
    }

    public static RouteFragment newInstance(Route route) {
        RouteFragment fragment = new RouteFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_HISTORY, new ArrayList<>(route.getNames()));
        args.putParcelableArrayList(ARG_LOCATION, new ArrayList<>(route.getCoordinates()));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            histories = getArguments().getStringArrayList(ARG_HISTORY);
            coordinates = getArguments().getParcelableArrayList(ARG_LOCATION);
        }
        if (getContext() != null) {
            geofencingClient = LocationServices.getGeofencingClient(getContext());
            BroadcastReceiver broadcastReceiver = new GeofenceResultReceiver();
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,
                    new IntentFilter("geofence_result"));
            database = Room.databaseBuilder(getContext(), AppDatabase.class, "undercover.db").build();
        }

        restaurantRepository = new RestaurantRepositoryImpl();
        sightRepository = new SightRepositoryImpl();
        userRepository = new UserRepositoryImpl();

        geofenceList = new ArrayList<>();
        for (int i = 0; i < histories.size(); i++) {
            String requestId = histories.get(i);
            Coordinate coordinate = coordinates.get(i);
            geofenceList.add(new Geofence.Builder()
                    .setRequestId(requestId)
                    .setCircularRegion(coordinate.getLatitude(), coordinate.getLongitude(), 100)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }

        addGeofence();

    }

    private void resetCurrentProgress() {
        new Thread(() -> {
            RouteDao routeDao = database.routeDao();

            Route activity = routeDao.getActivity();
            binding.bar.setCurrentIndex(activity.getCurrentProgress()-1);
            binding.bar.next();
        }).start();
    }

    @Override
    public void onPause() {
        super.onPause();
        new Thread(() -> {
            RouteDao routeDao = database.routeDao();

            Route activity = routeDao.getActivity();

            routeDao.updateProgress(binding.bar.getCurrentIndex(), activity.getId());
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private LocationRequest initializeLocationRequest() {
        return new LocationRequest.Builder(10000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build();
    }

    private LocationCallback initializeLocationCallback() {
        return new LocationCallback() {
            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
            }
        };
    }

    public void addGeofence() {
        if (getActivity() == null) return;

        boolean flag = false;

        List<String> permissions = new ArrayList<>(List.of(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        ));
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            flag = true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "background location request");
            flag = true;
            permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        }
        if (flag) {
            getActivity().requestPermissions(permissions.toArray(new String[0]), 0);
            return;
        }

        FusedLocationProviderClient testFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            testFusedLocationClient.requestLocationUpdates(initializeLocationRequest()
                            , initializeLocationCallback(), Looper.getMainLooper())
                    .addOnSuccessListener(unused -> Log.d(TAG, "success to add request location"))
                    .addOnFailureListener(e -> Log.e(TAG, "fail to add request location : " + e.getMessage()));
        else {
            LocationRequest locationRequest = new LocationRequest()
                    .setInterval(10000)
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY);
            testFusedLocationClient.requestLocationUpdates(locationRequest,
                            initializeLocationCallback(), Looper.getMainLooper())
                    .addOnSuccessListener(unused -> Log.d(TAG, "success to add request location"))
                    .addOnFailureListener(e -> Log.e(TAG, "fail to add request location : " + e.getMessage()));
        }

        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(unused -> Log.d(TAG, "success to add geofence"))
                .addOnFailureListener(e -> Log.e(TAG, "fail to add geofence" + e.getMessage()));


    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofences(geofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it
        if (geofencePendingIntent != null) return geofencePendingIntent;
        Intent intent = new Intent(getContext(), GeofenceBroadcastReceiver.class);


        geofencePendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        return geofencePendingIntent;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRouteBinding.inflate(getLayoutInflater());
        List<Integer> progress = new ArrayList<>();
        progress.add(0);
        for (double i = 0; i < histories.size(); i++) {
            progress.add((int) (((i+1) / histories.size()) * 100 ));
        }
        progress.add(100);
        histories.add(0, "여행 시작!");

        histories.add("여행 종료!");
        binding.bar.setHistories(histories, progress);

        binding.tempNext.setOnClickListener(v -> binding.bar.next());
        binding.doneButton.setOnClickListener(v -> {
            DialogRatingBinding ratingBinding = DialogRatingBinding.inflate(getLayoutInflater());

            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setView(ratingBinding.getRoot())
                    .setPositiveButton("저장", (dialog, which) -> {
                        double rating = ratingBinding.rating.getRating() * 2;
                        updateWeight(rating);
                        setDeactivateRoute();
                        returnToMain();
                    })
                    .setNegativeButton("취소", (dialog, which) -> {
                        setDeactivateRoute();
                        returnToMain();
                    }).create();
            alertDialog.show();
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetCurrentProgress();
    }

    private void returnToMain() {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame, new MainPageFragment())
                .commit();
    }

    private void setDeactivateRoute() {
        RouteDao routeDao = database.routeDao();
        new Thread() {
            @Override
            public void run() {
                routeDao.deActivateAll();
            }
        }.start();
    }

    private void updateWeight(double rating) {
        SightDao sightDao = database.sightDao();
        RestaurantDao restaurantDao = database.restaurantDao();

        String uid = userRepository.getCurrentUser().getUid();
        userRepository.getUser(uid, result -> {
            List<Boolean> options = result.getOptions();
            new Thread() {
                @Override
                public void run() {
                    List<Sight> sights = sightDao.getAll();
                    List<Restaurant> restaurants = restaurantDao.getAll();

                    for (Sight sight: sights)
                        sightRepository.updateWeight(sight.getName(), options, rating);
                    for (Restaurant restaurant: restaurants)
                        restaurantRepository.updateWeight(restaurant.getName(), options, rating);

                    sightDao.deleteAll();
                    restaurantDao.deleteAll();
                }
            }.start();

        });
    }
}