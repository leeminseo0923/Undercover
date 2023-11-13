package univ.soongsil.undercover;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.os.Bundle;
import android.view.animation.PathInterpolator;

import androidx.appcompat.app.AppCompatActivity;

import univ.soongsil.undercover.databinding.ActivityStartBinding;

public class StartActivity extends AppCompatActivity {

    ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Path path_top = new Path();
        path_top.moveTo(0, 0);
        path_top.lineTo(3000, -3000);

        Path path_bottom = new Path();
        path_bottom.moveTo(0, 0);
        path_bottom.lineTo(-3000, 3000);


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ObjectAnimator.ofFloat(binding.bottomLabel, "translationX", "translationY", path_bottom))
                        .with(ObjectAnimator.ofFloat(binding.topLabel, "translationX", "translationY", path_top));
        animatorSet.setDuration(2000);
        animatorSet.setInterpolator(new PathInterpolator(0.5f, 0.3f));
        animatorSet.start();
    }
}