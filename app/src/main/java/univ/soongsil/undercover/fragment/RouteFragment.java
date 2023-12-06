package univ.soongsil.undercover.fragment;

import android.Manifest;
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

import univ.soongsil.undercover.databinding.FragmentRouteBinding;
import univ.soongsil.undercover.domain.Coordinate;
import univ.soongsil.undercover.receiver.GeofenceBroadcastReceiver;

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

    public class GeofenceResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "get broadcast");
            List<String> names = intent.getStringArrayListExtra("names");

            int action = intent.getIntExtra("action", 0);

            if (names == null || action == 0) {
                Log.e(TAG, "extra is null");
                return;
            }
            for (int i = 0; i < names.size(); i++){
                String name = names.get(i);

                Log.d(TAG, name);
                Log.d(TAG, binding.bar.getNextHistory());
                Log.d(TAG, String.valueOf(name.equals(binding.bar.getNextHistory())));
                Log.d(TAG, String.valueOf(action));

                if (name.equals(binding.bar.getCurrentHistory())
                        && action == Geofence.GEOFENCE_TRANSITION_EXIT) {
                    binding.label.setText("이동중...");
                }
                if (name.equals(binding.bar.getNextHistory()) && action == Geofence.GEOFENCE_TRANSITION_ENTER) {
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
        }

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
        return binding.getRoot();
    }
}