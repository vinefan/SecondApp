package cn.swufe.vine.secondapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import cn.swufe.vine.secondapp.entities.Rate;

@Dao
public interface RateDao {
    @Query("SELECT * FROM rate")
    List<Rate> getAll();

    @Insert()
    void insertAllRates(Rate... rates);

    @Query("DELETE FROM rate")
    void deleteAll();
}
