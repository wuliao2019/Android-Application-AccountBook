package com.cqu.accountbook.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {MyDatabase.class}, version = 1, exportSchema = false)
public abstract class DatabaseAction extends RoomDatabase {
    private static final String DB_NAME = "IncomeExpenseDatabase.db";
    private static volatile DatabaseAction instance;

    public static synchronized DatabaseAction getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static DatabaseAction create(final Context context) {
        return Room.databaseBuilder(
                context,
                DatabaseAction.class,
                DB_NAME).build();
    }

    public abstract DatabaseLists getAllIncomesDao();

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
