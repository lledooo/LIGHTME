package com.example.dani.lightme;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.dani.lightme.database.SceneRepository;

import java.util.List;

public class SceneViewModel extends AndroidViewModel {


    private SceneRepository sceneRepository;
    private LiveData<List<Scene>> scenesLiveData;
    private MutableLiveData<List<Scene>> searchResults;


    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    public SceneViewModel(Application application) {
        super(application);
        sceneRepository = new SceneRepository(application);
        scenesLiveData = sceneRepository.getAllScenes();
        searchResults = sceneRepository.getSearchResults();
    }
    MutableLiveData<List<Scene>> getSearchResults() {
        return searchResults;
    }

    LiveData<List<Scene>> getAllScenes() {
        return scenesLiveData;
    }

    public void insert(Scene scene) {
        sceneRepository.insertScene(scene);
    }

    public void findProduct(String name) {
        sceneRepository.findScene(name);
    }

    public void deleteScene(String scene) {
        sceneRepository.deleteScene(scene);
    }
    public void deleteAllScenes(){
        sceneRepository.deleteAllScenes();
    }




}
