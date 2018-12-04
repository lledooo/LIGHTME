package com.example.dani.lightme.database;

import com.example.dani.lightme.Scene;

import java.util.List;

public interface AsyncResult {
        void asyncFinished(List<Scene> results);

}
