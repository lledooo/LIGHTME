package com.example.dani.lightme.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.dani.lightme.Scene;

import java.util.List;



@Dao
public interface SceneDao {
    @Query("SELECT * FROM scene")
    LiveData<List<Scene>> getScenes();
    @Query("SELECT * FROM scene WHERE name = :name")
    List<Scene> findScenes(String name);


    @Query("SELECT * FROM scene WHERE name LIKE :name LIMIT 1  ")
    Scene getScenebyName(String name);

    @Query("SELECT * FROM scene WHERE id LIKE :id LIMIT 1  ")
    Scene getScenebyId(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addScene(Scene... scenes);

    @Query("DELETE FROM scene WHERE name LIKE :name")
    void deleteScene(String name);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateScene(Scene... scenes);

    @Query("DELETE FROM scene")
    void deleteScenes();
}
