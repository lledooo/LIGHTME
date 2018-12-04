package com.example.dani.lightme;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dani.lightme.database.SceneRepository;

import java.util.List;

import static android.app.Activity.RESULT_OK;


public class My_scenesFragment extends Fragment implements AdapterCallback {

    public static final String DEBUG_TAG ="AppCompatActivity";

    public static final String EXTRA_UPDATE ="update";
    public static final String EXTRA_DELETE ="update";
    public static final String EXTRA_NAME ="update";
    public static final String EXTRA_CARD ="cardddddsss";
    public static final String EXTRA_INITIAL ="update";
    public static final String EXTRA_ID ="id";



    public static final String TRANSITION_NAME ="name";
    public static final String TRANSITION_DELETE_BUTTON ="borrar";
    public static final String TRANSITION_PLAY_BUTTON ="PLAY";
    public static final String TRANSITION_DEVICES_BUTTON ="devices";
    public static final String TRANSITION_EDIT_BUTTON ="edit";
    public static final String TRANSITION_IMAGE_VIEW ="imagenn";

    private RecyclerView recyclerView;
    private SceneCardAdapter adapter;
    private SceneRepository sceneRepository;
    private SceneViewModel sceneViewModel;


    @Override
    public void onDeleteSceneCallback(String name) {
        sceneViewModel.deleteScene(name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_my_scenes, container, false);
        if (adapter == null) {
            adapter = new SceneCardAdapter(view.getContext());
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter.setInterfaz(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        sceneViewModel = ViewModelProviders.of(this).get(SceneViewModel.class);
        sceneViewModel.getAllScenes().observe(this, new Observer<List<Scene>>() {

            @Override
            public void onChanged(@Nullable List<Scene> scenes) {
                List<Scene> scenes2 = adapter.getSceneList();
                adapter.setScenes(scenes);
                Toast.makeText(view.getContext(), "refreshed", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton btn_reset = (ImageButton) view.findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sceneViewModel.deleteAllScenes();
            }
        });





        //Database
     /*   SceneDatabase sceneDatabase = SceneDatabase.getInstance(getContext());
        SceneRepository sceneRepository = new SceneRepository(ge);

        sceneRepository.getAllScenes().observe(this, new Observer<List<Scene>>() {
            @Override
            public void onChanged(@Nullable List<Scene> scenes) {
                adapter.setScenes(scenes);
                Toast.makeText(getContext(), "Datos refresh", Toast.LENGTH_SHORT).show();
            }
        });

        */
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(DEBUG_TAG, "requestCode is " + requestCode);
        // if adapter.getItemCount() is request code, that means we are adding a new position
        // anything less than adapter.getItemCount() means we are editing a particular position
        if (requestCode == adapter.getItemCount()) {
            if (resultCode == RESULT_OK) {
                String id = data.getStringExtra(EXTRA_NAME);
                // Make sure the Add request was successful
              //  String name= sceneViewModel.g(id).getName();
              //  String image = sceneRepository.getScene(id).getImage();
                // if add name, insert name in list

               // String image = data.getStringExtra(EXTRA_CARD);
              //  adapter.addCard(name, image);
            }
        } else {
            // Anything other than adapter.getItemCount() means editing a particular list item
            // the requestCode is the list item position
            if (resultCode == RESULT_OK) {
                // Make sure the Update request was successful
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(requestCode);
                if (data.getExtras().getBoolean(EXTRA_DELETE, false)) {
                    // if delete user delete
                    // The user deleted a contact
                   // adapter.deleteCard(viewHolder.itemView, requestCode);
                } else if (data.getExtras().getBoolean(EXTRA_UPDATE)) {
                    // if name changed, update user
                    String name = data.getStringExtra(EXTRA_NAME);
                    viewHolder.itemView.setVisibility(View.INVISIBLE);
                   // adapter.updateCard(name, requestCode);
                }
            }
        }


    }

    public void doSmoothScroll(int position) {
        recyclerView.smoothScrollToPosition(position);
    }


}
