package cn.swufe.vine.secondapp.dataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import cn.swufe.vine.secondapp.dao.RateDao;
import cn.swufe.vine.secondapp.entities.Rate;

@Database(entities = {Rate.class}, version = 1)
public abstract class RateDataBase extends RoomDatabase {
    public abstract RateDao rateDao();
}
