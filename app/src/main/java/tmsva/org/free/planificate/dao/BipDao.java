package tmsva.org.free.planificate.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import tmsva.org.free.planificate.model.Bip;

@Dao
public interface BipDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Bip bip);

    @Query("DELETE FROM bip_table WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM bip_table")
    LiveData<List<Bip>> getAllBips();
}
