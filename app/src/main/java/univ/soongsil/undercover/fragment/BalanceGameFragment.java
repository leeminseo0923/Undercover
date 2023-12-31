package univ.soongsil.undercover.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

import univ.soongsil.undercover.BalanceGameActivity;
import univ.soongsil.undercover.R;
import univ.soongsil.undercover.databinding.FragmentBalanceGameBinding;
import univ.soongsil.undercover.repository.UserRepository;
import univ.soongsil.undercover.repository.UserRepositoryImpl;

public class BalanceGameFragment extends Fragment {

    FragmentBalanceGameBinding binding;

    private static int count = 0;
    private static final String CHOICE_UP = "choiceUp";
    private static final String CHOICE_DOWN = "choiceDown";
    private static final List<Boolean> travelOptions = new ArrayList<>();

    private UserRepository userRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBalanceGameBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userRepository = new UserRepositoryImpl();

        String[] arrayUp = getResources().getStringArray(R.array.option_array_up);
        String[] arrayDown = getResources().getStringArray(R.array.option_array_down);

        TypedArray imageUp = getResources().obtainTypedArray(R.array.image_array_up);
        TypedArray imageDown = getResources().obtainTypedArray(R.array.image_array_down);

        binding.choiceUpText.setText(arrayUp[count]);
        binding.choiceUpText.setShadowLayer(3, 3, 3, R.color.black);
        binding.choiceDownText.setText(arrayDown[count]);
        binding.choiceDownText.setShadowLayer(3, 3, 3, R.color.black);

        binding.choiceUpImage.setImageResource(imageUp.getResourceId(count, 0));
        binding.choiceDownImage.setImageResource(imageDown.getResourceId(count, 0));

        binding.choiceUpImage.setOnClickListener(v -> {
            setButtonClickable(false);
            startAnimations(CHOICE_UP);

            travelOptions.add(true);
            count++;

            new Handler().postDelayed(() -> {
                if (count == arrayUp.length) {
                    count = 0;

                    String docId = userRepository.getCurrentUser().getUid();
                    userRepository.updateUserOptions(docId, travelOptions);

                    getActivity().finish();
                    return;
                }

                Intent intent = new Intent(getActivity().getApplicationContext(), BalanceGameActivity.class);
                startActivity(intent);
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
            }, 1000);
        });

        binding.choiceDownImage.setOnClickListener(v -> {
            setButtonClickable(false);
            startAnimations(CHOICE_DOWN);

            travelOptions.add(false);
            count++;

            new Handler().postDelayed(() -> {
                if (count == arrayDown.length) {
                    count = 0;

                    String docId = userRepository.getCurrentUser().getUid();
                    userRepository.updateUserOptions(docId, travelOptions);

                    getActivity().finish();
                    return;
                }

                Intent intent = new Intent(getActivity().getApplicationContext(), BalanceGameActivity.class);
                startActivity(intent);
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
            }, 1000);
        });
    }

    public void setButtonClickable(boolean clickable) {
        binding.choiceUpImage.setClickable(clickable);
        binding.choiceDownImage.setClickable(clickable);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void startAnimations(String chosenOption) {
        int pink = getResources().getColor(R.color.main_color, null);
        int white = getResources().getColor(R.color.white, null);

        // 배경 색 pink -> white
        ValueAnimator backgroundColorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), pink, white);
        backgroundColorAnimation.setDuration(600);
        backgroundColorAnimation.addUpdateListener(animation ->
                binding.balanceGameBackground.setBackgroundColor((int) animation.getAnimatedValue()));
        backgroundColorAnimation.start();

        // 제목 글자와 선택한 옵션 글자 색 white -> pink
        ValueAnimator textColorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), white, pink);
        textColorAnimation.setDuration(600);
        textColorAnimation.addUpdateListener(animation -> {
            binding.balanceGameTitle.setTextColor((int) animation.getAnimatedValue());

            if (chosenOption.equals(CHOICE_UP)) {
                binding.choiceUpText.setTextColor((int) animation.getAnimatedValue());
            } else {
                binding.choiceDownText.setTextColor((int) animation.getAnimatedValue());
            }
        });
        textColorAnimation.start();

        // 선택한 옵션 중심으로 이동하면서 확대
        if (chosenOption.equals(CHOICE_UP)) {
            Animation moveDownAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_move_down);
            binding.choiceUp.startAnimation(moveDownAnim);
        } else {
            Animation moveUpAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_move_up);
            binding.choiceDown.startAnimation(moveUpAnim);
        }

        // 나머지 선택지 fade out
        Animation fadeOutAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_fade_out);
        fadeOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setButtonClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (chosenOption.equals(CHOICE_UP)) {
            binding.choiceDown.startAnimation(fadeOutAnim);
        } else {
            binding.choiceUp.startAnimation(fadeOutAnim);
        }

        // VS 글자 fade out
        binding.versus.startAnimation(fadeOutAnim);
    }
}