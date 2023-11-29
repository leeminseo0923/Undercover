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

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import univ.soongsil.undercover.BalanceGameActivity;
import univ.soongsil.undercover.R;
import univ.soongsil.undercover.databinding.FragmentBalanceGameBinding;
import univ.soongsil.undercover.domain.User;
import univ.soongsil.undercover.repository.UserRepository;
import univ.soongsil.undercover.repository.UserRepositoryImpl;

public class BalanceGameFragment extends Fragment {

    private static final String TAG = "BALANCE_GAME";
    FragmentBalanceGameBinding binding;

    private static int count = 0;
    private static final String CHOICE_UP = "choiceUp";
    private static final String CHOICE_DOWN = "choiceDown";

    private User user;
    private UserRepository userRepository;
    private static final List<Boolean> travelOptions = new ArrayList<>();

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

        // TODO: 이미지 다 찾으면 이미지 이름 수정
        binding.choiceUpImage.setImageResource(R.drawable.choice_mountain);
        binding.choiceDownImage.setImageResource(R.drawable.choice_beach);

        binding.choiceUpText.setText(arrayUp[count]);
        binding.choiceDownText.setText(arrayDown[count]);

        binding.choiceUpImage.setOnClickListener(v -> {
            startAnimations(CHOICE_UP);

            travelOptions.add(true);
            count++;

            new Handler().postDelayed(() -> {
                if (count == arrayUp.length) {
                    count = 0;
                    // 옵션 확인용 Log 메시지
                    for (Boolean item: travelOptions) {
                        Log.d(TAG, "옵션: " + item);
                    }
                    addTravelOptionsToFireStore();

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
            startAnimations(CHOICE_DOWN);

            travelOptions.add(false);
            count++;

            new Handler().postDelayed(() -> {
                if (count == arrayDown.length) {
                    count = 0;
                    // 옵션 확인용 Log 메시지
                    for (Boolean item: travelOptions) {
                        Log.d(TAG, "옵션: " + item);
                    }
                    addTravelOptionsToFireStore();

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

            if (chosenOption.equals("choiceUp")) {
                binding.choiceUpText.setTextColor((int) animation.getAnimatedValue());
            } else {
                binding.choiceDownText.setTextColor((int) animation.getAnimatedValue());
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

    public void addTravelOptionsToFireStore() {
        userRepository = new UserRepositoryImpl();
        FirebaseUser currentUser = userRepository.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String docId = currentUser.getUid();

        DocumentReference docRef = db.collection("user").document(docId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                if (document.exists()) {
                    Map<String, Object> data = document.getData();
                    String name = (String) data.get("name");
                    String email = (String) data.get("email");

                    user = new User(name, email);
                    user.setOptions(travelOptions);

                    userRepository.addUserDocument(currentUser.getUid(), user);
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                    travelOptions.clear();
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }
}