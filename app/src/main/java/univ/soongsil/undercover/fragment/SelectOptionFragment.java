package univ.soongsil.undercover.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import univ.soongsil.undercover.R;
import univ.soongsil.undercover.databinding.ActivitySelectOptionBinding;
import univ.soongsil.undercover.domain.Place;
import univ.soongsil.undercover.domain.Region;
import univ.soongsil.undercover.domain.Restaurant;
import univ.soongsil.undercover.domain.Sight;
import univ.soongsil.undercover.repository.AppDatabase;
import univ.soongsil.undercover.repository.PlaceRepository;
import univ.soongsil.undercover.repository.RestaurantDao;
import univ.soongsil.undercover.repository.RestaurantRepositoryImpl;
import univ.soongsil.undercover.repository.SightDao;
import univ.soongsil.undercover.repository.SightRepositoryImpl;
import univ.soongsil.undercover.repository.UserRepository;
import univ.soongsil.undercover.repository.UserRepositoryImpl;

public class SelectOptionFragment extends Fragment {
    ActivitySelectOptionBinding binding;
    private static final int UNIT = 10000;

    PlaceRepository restaurantRepository;
    PlaceRepository sightRepository;
    UserRepository userRepository;

    Integer maxCost;

    Integer days;

    Region region;

    AppDatabase db;

    public static final String TAG = "Travel_option";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepositoryImpl();
        restaurantRepository = new RestaurantRepositoryImpl();
        sightRepository = new SightRepositoryImpl();
        days = 0;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivitySelectOptionBinding.inflate(inflater);
        if (container != null)
            db = Room.databaseBuilder(container.getContext(), AppDatabase.class, "undercover.db").build();
        else
            Log.e(TAG, "no parent error");
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView1 = getActivity().findViewById(R.id.text1);
        TextView textView2 = getActivity().findViewById(R.id.text2);
        TextView textView3 = getActivity().findViewById(R.id.text3);
        TextView textView4 = getActivity().findViewById(R.id.text4);

        SeekBar seekBar1 = getActivity().findViewById(R.id.day_seekbar);
        FrameLayout frameLayout1 = getActivity().findViewById(R.id.frame1);
        TextView seekbar1Text = getActivity().findViewById(R.id.seekbar1_text);

        SeekBar seekBar2 = getActivity().findViewById(R.id.money_seekbar);
        FrameLayout frameLayout2 = getActivity().findViewById(R.id.frame2);
        TextView seekbar2Text = getActivity().findViewById(R.id.seekbar2_text);

        Button getbutton = getActivity().findViewById(R.id.get_button);

        Spinner spinner = getActivity().findViewById(R.id.spn_SPNList);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getActivity(), R.array.location_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setPrompt("여행지 선택");


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();

                ReadyDoneFragment readyDoneFragment = new ReadyDoneFragment();

                if(spinner.getItemAtPosition(position).equals("선택")) {
                    textView1.setText("여행지를\n선택해주세요!");
                }
                if(spinner.getItemAtPosition(position).equals("부산")) {
                    region = Region.BUSAN;
                    bundle.putString("지역", "부산");
                    textView1.setText("여행 일수를\n선택해주세요!");
                    frameLayout1.setVisibility(View.VISIBLE);
                }
                else if(spinner.getItemAtPosition(position).equals("서울")) {
                    region = Region.SEOUL;
                    bundle.putString("지역", "서울");
                    textView1.setText("여행 일수를\n선택해주세요!");
                    frameLayout1.setVisibility(View.VISIBLE);
                }
                else if(spinner.getItemAtPosition(position).equals("제주")) {
                    region = Region.JEJU;
                    bundle.putString("지역", "제주");
                    textView1.setText("여행 일수를\n선택해주세요!");
                    frameLayout1.setVisibility(View.VISIBLE);
                }

                //ReadyDoneFragment로 지역 데이터 전달
                getParentFragmentManager().setFragmentResult("지역requestkey", bundle);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                days = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekbar1Text.setText(String.format(getString(R.string.day_string), days));
                textView1.setText("여행 경비를\n선택해주세요!");
                frameLayout2.setVisibility(View.VISIBLE);
            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            Bundle bundle = new Bundle();

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxCost = seekBar.getProgress() * UNIT;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekbar2Text.setText(String.format(getString(R.string.money_string), maxCost));

                //ReadyDoneFragment로 가격 데이터 전달
                bundle.putInt("가격", maxCost);
                getParentFragmentManager().setFragmentResult("가격requestkey", bundle);

                textView1.setText("모험을\n떠나시겠습니까?");
                getbutton.setVisibility(View.VISIBLE);
            }


        });

        binding.getButton.setOnClickListener(v -> {
            userRepository.getUser(userRepository.getCurrentUser().getUid(),
                    user -> {
                        List<Boolean> options = user.getOptions();

                        RestaurantDao restaurantDao = db.restaurantDao();
                        SightDao sightDao = db.sightDao();

                        restaurantRepository.getBestPlaces(region,
                                options,maxCost,
                                days * 3 + 3,
                                result -> new Thread() {
                                    @Override
                                    public void run() {
                                        restaurantDao.deleteAll();

                                        Log.d(TAG, String.valueOf(restaurantDao.getAll().size()));
                                        restaurantDao.insertAll(result.stream()
                                                .map((Function<Place, Restaurant>) place -> (Restaurant) place)
                                                .collect(Collectors.toList()));
                                        Restaurant restaurant = restaurantDao.getAll().get(0);
                                        Log.d(TAG, restaurant.getName());
                                    }
                                }.start());

                        sightRepository.getBestPlaces(
                                region,
                                options,
                                maxCost,
                                days * 3 + 3,
                                result -> new Thread() {
                                    @Override
                                    public void run() {
                                        sightDao.deleteAll();
                                        Log.d(TAG, String.valueOf(sightDao.getAll().size()));
                                        sightDao.insertAll(result.stream().map((Function<Place, Sight>) place -> (Sight) place).collect(Collectors.toList()));
                                        Sight sight = sightDao.getAll().get(0);
                                        Log.d(TAG, sight.getName());
                                    }
                                }.start());

                    });
            getParentFragmentManager().beginTransaction().replace(R.id.main_frame, new ReadyDoneFragment()).commit();
        });



        binding.backbutton.setOnClickListener(v -> getParentFragmentManager().beginTransaction().replace(R.id.main_frame, new MainPageFragment()).commit());

    }

}
