package univ.soongsil.undercover.fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;

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

        binding.friendRequestButton.setOnClickListener(v -> {
            String myUid = userRepository.getCurrentUser().getUid();
            String myEmail = userRepository.getCurrentUser().getEmail();
            String friendEmail = binding.friendEmail.getText().toString();

            if (friendEmail.equals("")) {
                makeToast("이메일을 입력하세요.");
                return;
            }

            db.collection("user")
                    .whereEqualTo("email", friendEmail)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Log.d(TAG, "User doesn't exist.");
                            makeToast("존재하지 않는 사용자입니다.");
                            return;
                        }

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document.getData().get("email").equals(myEmail)) {
                                Log.d(TAG, "Same as current user.");
                                makeToast("현재 사용자와 동일한 계정입니다.");
                                return;
                            }

                            Log.d(TAG, document.getId() + " => " + document.getData());
                            String friendUid = document.getId();

                            Log.d(TAG, "Friend request complete");
                            makeToast("친구 요청을 보냈습니다.");
                            userRepository.addUserFriendRequest(friendUid, myUid);
                        }
                    })
                    .addOnFailureListener(e ->
                            Log.w(TAG, "Error getting documents: ", e));
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

                userRepository.deleteUserFriendRequest(docId, friendUid);
                userRepository.addUserFriend(docId, friendUid);
                userRepository.addUserFriend(friendUid, docId);

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

    public void makeToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}