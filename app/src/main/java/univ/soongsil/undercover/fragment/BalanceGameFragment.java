package univ.soongsil.undercover.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import univ.soongsil.undercover.BalanceGameActivity;
import univ.soongsil.undercover.MainActivity;
import univ.soongsil.undercover.R;
import univ.soongsil.undercover.databinding.FragmentBalanceGameBinding;

public class BalanceGameFragment extends Fragment {

    FragmentBalanceGameBinding binding;
    static int count = 0;

    /** 옵션 중 위의 항목을 선택하면 true, 아래의 항목을 선택하면 false로 저장 */
    static boolean[] travelOption = new boolean[5];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBalanceGameBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] arrayUp = getResources().getStringArray(R.array.option_array_up);
        String[] arrayDown = getResources().getStringArray(R.array.option_array_down);

        binding.choiceUp.setText(arrayUp[count]);
        binding.choiceDown.setText(arrayDown[count]);

        binding.choiceUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** 선택 확인용 토스트 메시지 */
                String chosen = binding.choiceUp.getText().toString();
                Toast.makeText(getActivity().getApplicationContext(), chosen, Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        travelOption[count] = true;
                        count++;

                        if (count == arrayUp.length) { // 모든 문항 수를 체크했으면 MainActivity로 이동
                            /* TODO: 옵션 선택 확인용 토스트 메시지
                            *   임의의 옵션 선택지 5개를 만들어 놓은 뒤 boolean[]으로 받음
                            *   indent.putExtra()로 boolean[]을 보내긴 하지만, 아직 정확한 흐름이 나오지 않아서
                            *   일단 BalanceGameFragment 내에서 값이 잘 받아졌는지 확인하는 토스트 메시지를 구성함
                            * */
                            for (int i = 0; i < travelOption.length; i++) {
                                Log.d("tag", "옵션 " + (i + 1) + ": " + travelOption[i]);
                            } // ex) "위, 위, 위, 아래, 아래"를 고르면 [true, true, true, false, false]가 저장됨

                            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                            intent.putExtra("travelOption", travelOption);
                            startActivity(intent);
                            return;
                        }

                        Intent intent = new Intent(getActivity().getApplicationContext(), BalanceGameActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        getActivity().overridePendingTransition(0, 0);
                    }
                }, 500);
            }
        });

        binding.choiceDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** 선택 확인용 토스트 메시지 */
                String chosen = binding.choiceDown.getText().toString();
                Toast.makeText(getActivity().getApplicationContext(), chosen, Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        travelOption[count] = false;
                        count++;

                        for (int i = 0; i < travelOption.length; i++) {
                            Log.d("tag", "옵션 " + (i + 1) + ": " + travelOption[i]);
                        }

                        if (count == arrayDown.length) { // 모든 문항 수를 체크했으면 MainActivity로 이동
                            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                            intent.putExtra("travelOption", travelOption);
                            startActivity(intent);
                            return;
                        }

                        Intent intent = new Intent(getActivity().getApplicationContext(), BalanceGameActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        getActivity().overridePendingTransition(0, 0);
                    }
                }, 500);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}