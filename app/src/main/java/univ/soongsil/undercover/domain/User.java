package univ.soongsil.undercover.domain;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private final String name;
    private final List<String> friends;
    private List<Boolean> options;
    public User(String name) {
        this.name = name;
        friends = new ArrayList<>();
        options = new ArrayList<>(10);
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
