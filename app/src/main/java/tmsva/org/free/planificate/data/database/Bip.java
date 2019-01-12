package tmsva.org.free.planificate.data.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bip_table")
public class Bip {

    @PrimaryKey @ColumnInfo(name = "id")
    private int id;

    public Bip(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
