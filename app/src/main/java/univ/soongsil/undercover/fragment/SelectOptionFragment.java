package univ.soongsil.undercover.fragment;

import android.os.Bundle;
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

import androidx.activity.BackEventCompat;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.FragmentNavigatorExtrasKt;

import org.w3c.dom.Text;

import java.lang.reflect.Array;

import kotlin.Unit;
import univ.soongsil.undercover.R;
import univ.soongsil.undercover.databinding.ActivitySelectOptionBinding;
import univ.soongsil.undercover.domain.Region;

public class SelectOptionFragment extends Fragment {
    ActivitySelectOptionBinding binding;
    private static final int UNIT = 10000;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivitySelectOptionBinding.inflate(inflater);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView1 = (TextView) getActivity().findViewById(R.id.text1);
        TextView textView2 = (TextView) getActivity().findViewById(R.id.text2);
        TextView textView3 = (TextView) getActivity().findViewById(R.id.text3);
        TextView textView4 = (TextView) getActivity().findViewById(R.id.text4);

        SeekBar seekBar1 = (SeekBar) getActivity().findViewById(R.id.day_seekbar);
        FrameLayout frameLayout1 = (FrameLayout) getActivity().findViewById(R.id.frame1);
        TextView seekbar1Text = (TextView) getActivity().findViewById(R.id.seekbar1_text);

        SeekBar seekBar2 = (SeekBar) getActivity().findViewById(R.id.money_seekbar);
        FrameLayout frameLayout2 = (FrameLayout) getActivity().findViewById(R.id.frame2);
        TextView seekbar2Text = (TextView) getActivity().findViewById(R.id.seekbar2_text);

        Button getbutton = (Button) getActivity().findViewById(R.id.get_button);

        Spinner spinner = (Spinner) getActivity().findViewById(R.id.spn_SPNList);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getActivity(), R.array.location_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setPrompt("여행지 선택");


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Region region;
                Bundle bundle = new Bundle();

                ReadyDoneFragment readyDoneFragment = new ReadyDoneFragment();

                if(spinner.getItemAtPosition(position).equals("선택")==true) {
                    textView1.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    textView3.setVisibility(View.INVISIBLE);
                    textView4.setVisibility(View.INVISIBLE);
                }
                if(spinner.getItemAtPosition(position).equals("부산")==true) {
                    region = Region.BUSAN;
                    bundle.putString("지역", "부산");
                    textView1.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                    textView3.setVisibility(View.INVISIBLE);
                    textView4.setVisibility(View.INVISIBLE);
                    frameLayout1.setVisibility(View.VISIBLE);
                }
                else if(spinner.getItemAtPosition(position).equals("서울")==true) {
                    region = Region.SEOUL;
                    bundle.putString("지역", "서울");
                    textView1.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                    textView3.setVisibility(View.INVISIBLE);
                    textView4.setVisibility(View.INVISIBLE);
                    frameLayout1.setVisibility(View.VISIBLE);
                }
                else if(spinner.getItemAtPosition(position).equals("제주")==true) {
                    region = Region.JEJU;
                    bundle.putString("지역", "제주");
                    textView1.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                    textView3.setVisibility(View.INVISIBLE);
                    textView4.setVisibility(View.INVISIBLE);
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

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekbar1Text.setText(String.format("%dDays", seekBar.getProgress()));
                textView1.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                textView3.setVisibility(View.VISIBLE);
                textView4.setVisibility(View.INVISIBLE);
                frameLayout2.setVisibility(View.VISIBLE);
            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            Bundle bundle = new Bundle();

            ReadyDoneFragment readyDoneFragment = new ReadyDoneFragment();

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekbar2Text.setText(String.format("%d원", seekBar.getProgress()*UNIT));

                //ReadyDoneFragment로 가격 데이터 전달
                bundle.putString("가격", String.format("%d", seekBar.getProgress()*UNIT));
                getParentFragmentManager().setFragmentResult("가격requestkey", bundle);

                textView1.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                textView3.setVisibility(View.INVISIBLE);
                textView4.setVisibility(View.VISIBLE);
                getbutton.setVisibility(View.VISIBLE);
            }


        });

        binding.getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.main_frame, new ReadyDoneFragment()).commit();
            }
        });



        binding.backbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.main_frame, new MainPageFragment()).commit();
            }
        });

    }





}
