package univ.soongsil.undercover;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.View;

import univ.soongsil.undercover.databinding.ActivityBalanceGameBinding;

public class BalanceGameActivity extends AppCompatActivity {

    ActivityBalanceGameBinding binding;
    Scene sceneChoice, sceneChoiceUp, sceneChoiceDown;
    Transition choiceUpTransition, choiceDownTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBalanceGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sceneChoice = Scene.getSceneForLayout(binding.balanceGameContainer, R.layout.scene_choice, this);
        sceneChoiceUp = Scene.getSceneForLayout(binding.balanceGameContainer, R.layout.scene_choice_up, this);
        sceneChoiceDown = Scene.getSceneForLayout(binding.balanceGameContainer, R.layout.scene_choice_down, this);

        sceneChoice.enter();

        choiceUpTransition = TransitionInflater.from(this).inflateTransition(R.transition.tran_choice_up);
        choiceDownTransition = TransitionInflater.from(this).inflateTransition(R.transition.tran_choice_down);
    }

    public void clickChoiceUp(View v) {
        TransitionManager.go(sceneChoiceUp, choiceUpTransition);
    }

    public void clickChoiceDown(View v) {
        TransitionManager.go(sceneChoiceDown, choiceDownTransition);
    }
}