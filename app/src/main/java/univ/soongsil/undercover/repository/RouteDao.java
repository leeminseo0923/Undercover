package univ.soongsil.undercover.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import univ.soongsil.undercover.domain.Route;

@Dao
public interface RouteDao {
    @Insert
    void insertAll(Route... routes);

    @Insert
    Long insert(Route route);

    @Query("SELECT * FROM Route")
    List<Route> getAll();

    @Query("SELECT * FROM Route WHERE isActive")
    Route getActivity();

    @Query("UPDATE Route SET isActive = 0 WHERE isActive")
    void deActivateAll();

    @Query("UPDATE Route SET currentProgress = :progress WHERE id= :idx")
    void updateProgress(Integer progress, Integer idx);
}
