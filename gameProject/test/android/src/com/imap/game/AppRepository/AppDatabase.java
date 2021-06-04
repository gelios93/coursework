package com.imap.game.AppRepository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.imap.game.BuildMaket;
import com.imap.game.MainActivity;

import lombok.SneakyThrows;

@Database(entities = {BuildMaket.class}, version = 1)
@TypeConverters({TilesetConverter.class, Vector3Converter.class})
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "app_db.db";
    private static volatile AppDatabase instance;
    private static final Object LOCK = new Object();

    public abstract MaketDAO maketDAO();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null)
                    instance = buildDatabase(context);
            }
        }

        return instance;
    }

    public static AppDatabase buildDatabase(final Context context) {


        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).addCallback(new Callback() {
            @SneakyThrows
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                MainActivity.isCreate = true;
            }
        }).build();

        db.query("select 1", null);

        return db;
    }
}