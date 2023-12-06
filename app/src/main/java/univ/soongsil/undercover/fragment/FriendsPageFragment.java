package univ.soongsil.undercover.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

import univ.soongsil.undercover.R;
import univ.soongsil.undercover.databinding.AddFriendsItemBinding;
import univ.soongsil.undercover.databinding.FragmentFriendsPageBinding;
import univ.soongsil.undercover.databinding.FriendsPageItemBinding;
import univ.soongsil.undercover.repository.UserRepository;
import univ.soongsil.undercover.repository.UserRepositoryImpl;

public class FriendsPageFragment extends Fragment {

    private static final String TAG = "FRIENDS_PAGE";
    FragmentFriendsPageBinding binding;
    private MyAdapter myAdapter;

    private UserRepository userRepository;
    private FirebaseFirestore db;
    private List<String> friends;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFriendsPageBinding.inflate(inflater);


        // Inflate the layout for this fragment
        View view = binding.getRoot();

        // Request focus to receive key events
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        // Set up key event listener
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Check if the key event is the Back button
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // Handle the Back button press
                    Toast.makeText(getActivity().getApplicationContext(), "눌렸다는", Toast.LENGTH_SHORT).show();
                    return true; // Consume the event
                }
                return false; // Let the event propagate if not handled
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userRepository = new UserRepositoryImpl();
        String docId = userRepository.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        db.collection("user")
                .document(docId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                            Map<String, Object> data = document.getData();
                            friends = (List<String>) data.get("friends");

                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                            myAdapter = new MyAdapter(friends);
                            binding.recyclerView.setAdapter(myAdapter);
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
        private FriendsPageItemBinding binding;

        private MyViewHolder(FriendsPageItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.deleteButton.setOnClickListener(v -> {
                String docId = userRepository.getCurrentUser().getUid();
                String friendUid = binding.friendUid.getText().toString();

                userRepository.deleteUserFriend(docId, friendUid);
                userRepository.deleteUserFriend(friendUid, docId);

                friends.remove(getBindingAdapterPosition());
                myAdapter.notifyItemRemoved(getBindingAdapterPosition());
            });
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<String> list;
        private String friendName;
        private String friendEmail;

        private MyAdapter(List<String> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            FriendsPageItemBinding binding = FriendsPageItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MyViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String friendsUid = list.get(position);

            db.collection("user")
                    .document(friendsUid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                Map<String, Object> data = document.getData();
                                friendName = (String) data.get("name");
                                friendEmail = (String) data.get("email");

                                holder.binding.friendName.setText(friendName);
                                holder.binding.friendEmail.setText(friendEmail);
                                holder.binding.friendUid.setText(friendsUid); // 화면 출력 x
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}