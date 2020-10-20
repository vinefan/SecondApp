package cn.swufe.vine.secondapp.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Rate {
    // 货币名唯一，直接用货币名做key
    @PrimaryKey
    public int uid;
    @ColumnInfo(name = "currency")
    public String currency;

    @ColumnInfo(name = "rate")
    public double rate;
}
