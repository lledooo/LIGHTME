package com.example.dani.lightme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dani.lightme.database.SceneRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;

public class SceneCardAdapter extends RecyclerView.Adapter<SceneCardAdapter.ViewHolder>  {
    private static final String DEBUG_TAG = "SampleMaterialAdapter";

    public Context context;
    private final LayoutInflater mInflater;
    private List<Scene> sceneList = Collections.emptyList(); // Cached copy of words
    protected LiveData<List<Scene>> listLiveData;
    private AdapterCallback adapterCallback;



    public SceneCardAdapter(Context context) {
        mInflater=LayoutInflater.from(context);
    }
    public void setInterfaz(AdapterCallback interfaz){
        this.adapterCallback = interfaz;
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Scene scene = sceneList.get(position);
        String name = scene.getName();
        viewHolder.name.setText(name);
        String image = scene.getImage();
        try {
            Bitmap bitmap = loadImageFromStorage(image,name);


                viewHolder.imageView.setImageBitmap(bitmap);

        }catch (Exception e){
            e.printStackTrace();

        }
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapterCallback.onDeleteSceneCallback(viewHolder.name.getText().toString());
            }
        });

    }

    public void setScenes(List<Scene> scenes){
        sceneList=scenes;
        notifyDataSetChanged();
    }

    public void removeScene(int position){
        sceneList.remove(position);
        notifyItemRemoved(position);
    }

    /*
    public void addCard(Scene scene) {
        scene.setName(name);
        scene.setImage(image);
        sceneList.add(scene);
        notifyItemInserted(getItemCount());
    }
    */

    public LiveData<List<Scene>> getListLiveData() {
        return listLiveData;
    }




    public List<Scene> getSceneList(){
        return sceneList;
    }



    @Override
    public int getItemCount() {
        if (sceneList.isEmpty()) {
            return 0;
        } else {
            return sceneList.size();
        }
    }

    @Override
    public long  getItemId(int position) {
        return sceneList.indexOf(sceneList.get(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater li = LayoutInflater.from(viewGroup.getContext());
        View v = li.inflate(R.layout.scene_view_holder, viewGroup, false);
        return new ViewHolder(v);
    }
    private Bitmap loadImageFromStorage(String path,String image)
    {

        try {
            File f=new File(path, image);

            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
            return bitmap;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView imageView;
        private ImageButton deleteButton;
        private ImageButton play_scene;
        private ImageButton edit_scene;
        private ImageButton devices;


        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.scene_title);
            imageView = (ImageView) v.findViewById(R.id.vista_previa);
            play_scene = (ImageButton) v.findViewById(R.id.btn_play_scene);
            edit_scene = (ImageButton) v.findViewById(R.id.btn_edit_scene);
            devices = (ImageButton) v.findViewById(R.id.btn_devices);
            deleteButton = (ImageButton) v.findViewById(R.id.btn_delete_scene);

        }


    }

}
