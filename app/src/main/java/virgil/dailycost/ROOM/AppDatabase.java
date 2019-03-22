package virgil.dailycost.ROOM;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import java.io.IOException;

import virgil.dailycost.helper.DatabaseHelper;

@Database(entities = {Record.class, Category_Record.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{

    private static AppDatabase assetDatabase;

    public static AppDatabase getInstance(Context context) {
        if (assetDatabase == null) {
            assetDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "asset.db")
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }

        return assetDatabase;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                String sql1 = DatabaseHelper.loadSqlFromSeed("records_1_2.sql");
                database.execSQL(sql1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public abstract RecordDao daoAccess();




}
