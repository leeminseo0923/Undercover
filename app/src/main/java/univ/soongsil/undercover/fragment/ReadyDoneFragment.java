package univ.soongsil.undercover.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import org.w3c.dom.Text;

import univ.soongsil.undercover.R;
import univ.soongsil.undercover.databinding.ActivityReadyDoneBinding;
import univ.soongsil.undercover.domain.Region;

public class ReadyDoneFragment extends Fragment {
    ActivityReadyDoneBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityReadyDoneBinding.inflate(inflater);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView Selected_location = (TextView) getActivity().findViewById(R.id.selected_location);
        TextView Selected_cost = (TextView) getActivity().findViewById(R.id.selected_cost);


        //SelectOptionFragment로 부터 지역 데이터와 가겨 데이터를 받아와서 Selected_location, Selected_cost 값을 사용자의 입력에 따라 달라지게 설정
        getParentFragmentManager().setFragmentResultListener("지역requestkey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                String locationText = bundle.getString("지역");
                Selected_location.setText(locationText);
            }
        });

        getParentFragmentManager().setFragmentResultListener("가격requestkey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                String costText = bundle.getString("가격");
                Selected_cost.setText(costText);
            }
        });

        binding.redayGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.main_frame, new SelectHotelFragment()).commit();
            }
        });

        binding.redayNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.main_frame, new SelectOptionFragment()).commit();
            }
        });





    }
}
