package univ.soongsil.undercover.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import univ.soongsil.undercover.BalanceGameActivity;
import univ.soongsil.undercover.MainActivity;
import univ.soongsil.undercover.R;
import univ.soongsil.undercover.databinding.FragmentBalanceGameBinding;

public class BalanceGameFragment extends Fragment {

    FragmentBalanceGameBinding binding;
    static int count = 0;

    /** 옵션 중 위의 항목을 선택하면 true, 아래의 항목을 선택하면 false로 저장 */
    static boolean[] travelOption = new boolean[5];
    final String choiceUp = "choiceUp";
    final String choiceDown = "choiceDown";


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

        // TODO: 사진 다 찾으면 사진 파일 이름 수정
        binding.choiceUpImage.setImageResource(R.drawable.choice_mountain);
        binding.choiceDownImage.setImageResource(R.drawable.choice_beach);

        binding.choiceUpText.setText(arrayUp[count]);
        binding.choiceDownText.setText(arrayDown[count]);

        binding.choiceUpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimations(choiceUp);

                travelOption[count++] = true;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (count == arrayUp.length) { // 모든 문항 수를 체크했으면 MainActivity로 이동
                            /* TODO: BalanceGameFragment 내에서 값이 잘 받아졌는지 확인하는 토스트 메시지
                             *   임의의 옵션 선택지 5개를 만들어 놓은 뒤 boolean[]으로 받음
                             *   ex) "위, 위, 위, 아래, 아래"를 고르면 [true, true, true, false, false]가 저장됨
                             *   모든 옵션 선택이 끝난 후 Logcat에서 확인할 수 있습니다.
                             * */
                            for (int i = 0; i < travelOption.length; i++) {
                                Log.d("tag", "옵션 " + (i + 1) + ": " + travelOption[i]);
                            }

                            count = 0;

                            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                            // TODO: DB에 해당 boolean[] 넣기
                            // intent.putExtra("travelOption", travelOption);
                            startActivity(intent);
                            return;
                        }

                        Intent intent = new Intent(getActivity().getApplicationContext(), BalanceGameActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        getActivity().overridePendingTransition(0, 0);
                    }
                }, 1000);
            }
        });

        binding.choiceDownImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimations(choiceDown);

                travelOption[count++] = false;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (count == arrayDown.length) {
                            for (int i = 0; i < travelOption.length; i++) {
                                Log.d("tag", "옵션 " + (i + 1) + ": " + travelOption[i]);
                            }

                            count = 0;

                            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                            // intent.putExtra("travelOption", travelOption);
                            startActivity(intent);
                            return;
                        }

                        Intent intent = new Intent(getActivity().getApplicationContext(), BalanceGameActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        getActivity().overridePendingTransition(0, 0);
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /** 위 선택지를 골랐으면 choiceUp, 아래 선택지를 골랐으면 choiceDown을 인자로 주면 됨 */
    public void startAnimations(String chosenOption) {
        int pink = getResources().getColor(R.color.main_color, null);
        int white = getResources().getColor(R.color.white, null);

        // 배경 색 pink -> white
        ValueAnimator backgroundColorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), pink, white);
        backgroundColorAnimation.setDuration(600);
        backgroundColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                binding.balanceGameBackground.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });
        backgroundColorAnimation.start();

        // 제목 글자와 선택한 옵션 글자 색 white -> pink
        ValueAnimator textColorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), white, pink);
        textColorAnimation.setDuration(600);
        textColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                binding.balanceGameTitle.setTextColor((int) animation.getAnimatedValue());

                if (chosenOption.equals("choiceUp")) {
                    binding.choiceUpText.setTextColor((int) animation.getAnimatedValue());
                } else {
                    binding.choiceDownText.setTextColor((int) animation.getAnimatedValue());
                }
            }
        });
        textColorAnimation.start();

        // 선택한 옵션 중심으로 이동하면서 확대
        if (chosenOption.equals("choiceUp")) {
            Animation moveDownAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_move_down);
            binding.choiceUp.startAnimation(moveDownAnim);
        } else {
            Animation moveUpAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_move_up);
            binding.choiceDown.startAnimation(moveUpAnim);
        }

        // 나머지 선택지 fade out
        Animation fadeOutAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_fade_out);
        if (chosenOption.equals("choiceUp")) {
            binding.choiceDown.startAnimation(fadeOutAnim);
        } else {
            binding.choiceUp.startAnimation(fadeOutAnim);
        }

        // VS 글자 fade out
        binding.versus.startAnimation(fadeOutAnim);
    }
}