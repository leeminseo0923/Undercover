package univ.soongsil.undercover.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import univ.soongsil.undercover.domain.Sight;

@Dao
public interface SightDao {
    @Insert
    void insertAll(List<Sight> sights);
    @Query("DELETE FROM Sight")
    void deleteAll();

    @Query("SELECT * FROM Sight")
    List<Sight> getAll();
}
