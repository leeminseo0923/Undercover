package univ.soongsil.undercover.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import univ.soongsil.undercover.domain.Restaurant;

@Dao
public interface RestaurantDao {
    @Insert
    void insertAll(List<Restaurant> restaurants);
    @Query("DELETE FROM Restaurant")
    void deleteAll();

    @Query("SELECT * FROM Restaurant")
    List<Restaurant> getAll();
}
