package univ.soongsil.undercover;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import univ.soongsil.undercover.databinding.ActivityAddFriendsBinding;
import univ.soongsil.undercover.databinding.ActivityBalanceGameBinding;
import univ.soongsil.undercover.databinding.AddFriendsItemBinding;
import univ.soongsil.undercover.fragment.AddFriendsFragment;
import univ.soongsil.undercover.fragment.BalanceGameFragment;
import univ.soongsil.undercover.fragment.TravelOptionFragment;
import univ.soongsil.undercover.repository.UserRepository;
import univ.soongsil.undercover.repository.UserRepositoryImpl;

public class AddFriendsActivity extends AppCompatActivity {

    ActivityAddFriendsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.add_friends_frame, new AddFriendsFragment()).commit();
    }
}