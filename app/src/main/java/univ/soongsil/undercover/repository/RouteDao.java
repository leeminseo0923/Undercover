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
}
