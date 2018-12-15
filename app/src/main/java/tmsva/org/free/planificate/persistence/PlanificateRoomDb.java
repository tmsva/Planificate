package tmsva.org.free.planificate.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import tmsva.org.free.planificate.dao.BipDao;
import tmsva.org.free.planificate.model.Bip;

@Database(entities = {Bip.class},
        version = 1)
public abstract class PlanificateRoomDb extends RoomDatabase {

    public abstract BipDao bipDao();

    private static volatile PlanificateRoomDb INSTANCE;

    public static PlanificateRoomDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlanificateRoomDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlanificateRoomDb.class, "planificate_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
