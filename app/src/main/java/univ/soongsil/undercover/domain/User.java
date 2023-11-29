package univ.soongsil.undercover.domain;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private final String email;
    private final String name;
    private final List<String> friends;
    private List<Boolean> options;
    private List<String> friendRequests;
    public User(String name, String email) {
        this.email = email;
        this.name = name;
        friendRequests = new ArrayList<>();
        friends = new ArrayList<>();
        options = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public List<String> getFriends() {
        return friends;
    }

    public List<Boolean> getOptions() {
        return options;
    }

    public List<String> getFriendRequests() {
        return friendRequests;
    }

    public User addFriendRequest(String uID) {
        friendRequests.add(uID);
        return this;
    }

    public void setFriendRequests(List<String> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public User addFriend(String uID) {
        friends.add(uID);
        return this;
    }

    public User addFriends(List<String> uIDs) {
        friends.addAll(uIDs);
        return this;
    }

    public User deleteFriend(String uID) {
        friends.remove(uID);
        return this;
    }

    public User deleteFriends(List<String> uIDs) {
        friends.removeAll(uIDs);
        return this;
    }

    public void setOptions(List<Boolean> options) {
        // TODO: 개수는 수정 예정
        if (options.size() != 5) throw new IllegalArgumentException("option은 5개여야 합니다.");
        this.options = options;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name) && friends.equals(user.friends) && options.equals(user.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, friends, options);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", friends=" + friends +
                ", options=" + options +
                '}';
    }
}
