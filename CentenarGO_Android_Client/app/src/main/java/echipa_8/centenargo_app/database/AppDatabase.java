package echipa_8.centenargo_app.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Image.class}, version=1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ImageDAO imageDAO();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "local-database").allowMainThreadQueries().build();
        }
        return instance;
    }
}
