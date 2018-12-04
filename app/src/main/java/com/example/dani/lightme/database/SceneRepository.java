package com.example.dani.lightme.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.example.dani.lightme.Scene;

import java.util.List;

import io.reactivex.Flowable;

public class SceneRepository implements AsyncResult{

    private LiveData<List<Scene>> sceneList= new LiveData<List<Scene>>() {
    };
    private SceneDao sceneDao;
    private static SceneRepository mInstance;
    private MutableLiveData<List<Scene>> searchResults =
            new MutableLiveData<>();

    public SceneRepository(Application application){
        SceneDatabase sceneDatabase = SceneDatabase.getInstance(application);
        sceneDao = sceneDatabase.sceneDao();
        sceneList = sceneDao.getScenes();

    }
    public void insertScene (Scene scene) {
        new queryAsyncTask.insertAsyncTask(sceneDao).execute(scene);
    }

    public void deleteScene(String scene) {
        new queryAsyncTask.deleteAsyncTask(sceneDao).execute(scene);
    }
    public void deleteAllScenes(){
        new queryAsyncTask.deleteAllAsyncTask(sceneDao).execute();
    }

    public void findScene(String name) {
        queryAsyncTask task = new queryAsyncTask(sceneDao);
        task.delegate = this;
        task.execute(name);
    }
    public LiveData<List<Scene>> getAllScenes() {
        return sceneList;
    }

    public MutableLiveData<List<Scene>> getSearchResults() {
        return searchResults;
    }
/*
    public static SceneRepository getInstance(ISceneDataSource mLocalDataSource){
        if(mInstance==null){
            mInstance= new SceneRepository(mLocalDataSource);
        }
        return mInstance;
    }
    */
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.

    @Override
    public void asyncFinished(List<Scene> results) {
        searchResults.setValue(results);
    }



    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.


    private static class queryAsyncTask extends
            AsyncTask<String, Void, List<Scene>> {

        private SceneDao asyncTaskDao;
        private SceneRepository delegate = null;

        queryAsyncTask(SceneDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected List<Scene> doInBackground(final String... params) {
            return asyncTaskDao.findScenes(params[0]);
        }

        @Override
        protected void onPostExecute(List<Scene> result) {
            delegate.asyncFinished(result);
        }

        //Proceso para agregar escenas
        private static class insertAsyncTask extends AsyncTask<Scene, Void, Void> {

            private SceneDao asyncTaskDao;

            insertAsyncTask(SceneDao dao) {
                asyncTaskDao = dao;
            }

            @Override
            protected Void doInBackground(final Scene... params) {
                asyncTaskDao.addScene(params[0]);
                return null;
            }
        }


        private static class deleteAsyncTask extends AsyncTask<String, Void, Void> {

            private SceneDao asyncTaskDao;

            deleteAsyncTask(SceneDao dao) {
                asyncTaskDao = dao;
            }

            @Override
            protected Void doInBackground(final String... params) {
                asyncTaskDao.deleteScene(params[0]);
                return null;
            }
        }
        private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

            private SceneDao asyncTaskDao;

            deleteAllAsyncTask(SceneDao dao) {
                asyncTaskDao = dao;
            }

            @Override
            protected Void doInBackground(Void... voids) {
                asyncTaskDao.deleteScenes();
                return null;
            }
        }
    }



}
