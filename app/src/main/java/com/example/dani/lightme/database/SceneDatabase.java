package com.example.dani.lightme.database;



import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.dani.lightme.Scene;


@Database(entities = {Scene.class}, version = 2)
public abstract class SceneDatabase extends RoomDatabase {
    public static final String DATABASE_NAME="Scene_Database";

    public abstract SceneDao sceneDao();

    private static volatile SceneDatabase mInstance;

    public static SceneDatabase getInstance(Context context) {

        if (mInstance == null) {
            synchronized (SceneDatabase.class) {
                if (mInstance == null) {
                    mInstance = Room.databaseBuilder(context.getApplicationContext(), SceneDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();

                }
            }
        }
        return mInstance;
    }
}
