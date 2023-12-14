package univ.soongsil.undercover;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationBarView;

import univ.soongsil.undercover.databinding.ActivityMainBinding;
import univ.soongsil.undercover.fragment.AddFriendsFragment;
import univ.soongsil.undercover.fragment.FriendsPageFragment;
import univ.soongsil.undercover.fragment.MainPageFragment;
import univ.soongsil.undercover.fragment.SettingFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int grantResult :
                    grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) return;
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean("permission", true);
            getSupportFragmentManager().setFragmentResult("locale_permission", bundle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        // 첫 화면
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new MainPageFragment()).commit();

        binding.navbar.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MainPageFragment()).commit();
            }
            else if(item.getItemId() == R.id.friend) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FriendsPageFragment()).commit();
            }
            else if(item.getItemId() == R.id.addfriend){
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new AddFriendsFragment()).commit();
            }
            else if(item.getItemId() == R.id.setting){
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new SettingFragment()).commit();
            }
            return true;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 다른 곳에서 왔을 때 첫 화면으로 오도록 설정
        binding.navbar.setSelectedItemId(R.id.home);
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new MainPageFragment()).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog exitDialog = new AlertDialog.Builder(this)
                    .setTitle("UNDER COVER")
                    .setMessage("앱을 종료하시겠습니까?")
                    .setPositiveButton("네", (dialog, which) -> finish())
                    .setNegativeButton("아니요", null)
                    .create();

            exitDialog.show();
            return true;
        }
        return false;
    }
}