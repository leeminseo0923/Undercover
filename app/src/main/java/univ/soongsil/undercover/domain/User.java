package univ.soongsil.undercover.domain;

import androidx.annotation.NonNull;
import androidx.room.Ignore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private String email;
    private String name;
    private List<String> friends;
    private List<Boolean> options;
    private List<String> friendRequests;

    @Ignore
    public User() {
        email = "";
        name = "";
        friends = new ArrayList<>();
        options = new ArrayList<>();
        friendRequests = new ArrayList<>();
    }

    @Ignore
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<Boolean> getOptions() {
        return options;
    }

    public void setOptions(List<Boolean> options) {
        // TODO: 개수는 수정 예정
        if (options.size() != 5) throw new IllegalArgumentException("option은 5개여야 합니다.");
        this.options = options;
    }

    public List<String> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<String> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public User addFriendRequest(String uID) {
        friendRequests.add(uID);
        return this;
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
        if (options.size() != 6) throw new IllegalArgumentException("option은 6개여야 합니다.");
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
