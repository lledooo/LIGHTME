package com.example.dani.lightme;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.UUID;

@Entity(tableName = "scene",indices = {@Index(value = "name", unique = true)})
public class Scene {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;
    //private String mId;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "image")
    private String image;

    public Scene(){
        this.name=null;
        this.image=null;
    }

    @Ignore
    public Scene(String name, String image) {
        this.name = name;
        this.image = image;
    }

   public int getId() {
        return id;
    }

    @NonNull
    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @NonNull
    public String getImage() {
        return image;
    }
    @NonNull
    public void setImage(String image) {
        this.image = image;
    }
    public String toString(){
        return getName();
    }


}
