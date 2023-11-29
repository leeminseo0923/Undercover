package univ.soongsil.undercover.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import univ.soongsil.undercover.R;
import univ.soongsil.undercover.databinding.FragmentTravelOptionBinding;
import univ.soongsil.undercover.databinding.TravelOptionItemBinding;
import univ.soongsil.undercover.domain.User;
import univ.soongsil.undercover.repository.UserRepository;
import univ.soongsil.undercover.repository.UserRepositoryImpl;

public class TravelOptionFragment extends Fragment {

    private static final String TAG = "TRAVEL_OPTION";
    FragmentTravelOptionBinding binding;

    private User user;
    private UserRepository userRepository;
    private FirebaseFirestore db;
    private List<Boolean> travelOptions;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTravelOptionBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userRepository = new UserRepositoryImpl();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String docId = userRepository.getCurrentUser().getUid();

        // docId를 통해 Firestore에서 데이터를 가져옴
        DocumentReference docRef = db.collection("user").document(docId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                    Map<String, Object> data = document.getData();
                    travelOptions = (List<Boolean>) data.get("options");
                    for (Boolean item: travelOptions) {
                        Log.d(TAG, "옵션: " + item + ", 사이즈: " + travelOptions.size());
                    }

                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                    binding.recyclerView.setAdapter(new MyAdapter(travelOptions));
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TravelOptionItemBinding binding;

        private MyViewHolder(TravelOptionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<Boolean> list;

        private MyAdapter(List<Boolean> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TravelOptionItemBinding binding = TravelOptionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MyViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String[] arrayUp = getResources().getStringArray(R.array.option_array_up);
            String[] arrayDown = getResources().getStringArray(R.array.option_array_down);
            Boolean travelOption = list.get(position);

            holder.binding.travelOptionUp.setText(arrayUp[position]);
            holder.binding.toggleButton.setChecked(travelOption);
            holder.binding.travelOptionDown.setText(arrayDown[position]);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}