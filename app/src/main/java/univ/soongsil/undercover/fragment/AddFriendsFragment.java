package univ.soongsil.undercover.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

import univ.soongsil.undercover.databinding.AddFriendsItemBinding;
import univ.soongsil.undercover.databinding.FragmentAddFriendsBinding;
import univ.soongsil.undercover.repository.UserRepository;
import univ.soongsil.undercover.repository.UserRepositoryImpl;

public class AddFriendsFragment extends Fragment {

    private static final String TAG = "ADD_FRIENDS";
    FragmentAddFriendsBinding binding;

    private UserRepository userRepository;
    private FirebaseFirestore db;
    private List<String> friendRequests;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddFriendsBinding.inflate(inflater);
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
                            friendRequests = (List<String>) data.get("friendRequests");

                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                            binding.recyclerView.setAdapter(new MyAdapter(friendRequests));
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
        private AddFriendsItemBinding binding;

        private MyViewHolder(AddFriendsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
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
            AddFriendsItemBinding binding = AddFriendsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MyViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String friendRequestUid = list.get(position);

            db.collection("user")
                    .document(friendRequestUid)
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
                                holder.binding.friendUid.setText(friendRequestUid); // 화면 출력 x
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    });

            holder.binding.acceptButton.setOnClickListener(v -> {
                String docId = userRepository.getCurrentUser().getUid();
                String friendUid = holder.binding.friendUid.getText().toString();
                userRepository.addUserFriend(docId, friendUid);
                userRepository.deleteUserFriendRequest(docId, friendUid);

                friendRequests.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            });

            holder.binding.declineButton.setOnClickListener(v -> {
                String docId = userRepository.getCurrentUser().getUid();
                String friendUid = holder.binding.friendUid.getText().toString();
                userRepository.deleteUserFriendRequest(docId, friendUid);

                friendRequests.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}