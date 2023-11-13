package univ.soongsil.undercover;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import univ.soongsil.undercover.fragment.Fragment1;
import univ.soongsil.undercover.fragment.Fragment2;
import univ.soongsil.undercover.fragment.Fragment3;
import univ.soongsil.undercover.fragment.Fragment4;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.navbar);
        //첫 화면
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new Fragment1()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment1()).commit();
                }
                else if(item.getItemId() == R.id.friend) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment2()).commit();
                }
                else if(item.getItemId() == R.id.addfriend){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment3()).commit();
                }
                else if(item.getItemId() == R.id.setting){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment4()).commit();
                }
                return true;
            }
        });
    }

}