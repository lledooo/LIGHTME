package com.example.dani.lightme;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dani.lightme.database.SceneDatabase;
import com.example.dani.lightme.database.SceneRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static java.lang.System.out;


public class New_sceneFragment extends Fragment {
    public static int CODE_CAMERA=01;
    public static int OPEN_GALLERY=02;
    public static int SAVE_SCENE = 03;
    public String nombre = UUID.randomUUID().toString();


    private SceneRepository sceneRepository;
    private String EXTENSION_IMAGE=".JPEG";
    private List<Scene> sceneList = new ArrayList<>();
    private SceneViewModel sceneViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_new_scene, container, false);
        ImageButton btn_new_photo= (ImageButton) view.findViewById(R.id.btn_new_photo);
        ImageButton btn_new_disp = (ImageButton) view.findViewById(R.id.btn_new_disp);
        ImageButton btn_gallery = (ImageButton) view.findViewById(R.id.btn_gallery);
        ImageButton btn_delete_disp = (ImageButton) view.findViewById(R.id.btn_delete_disp);
        ImageButton btn_save_scene = (ImageButton) view.findViewById(R.id.btn_saveScene);
        ImageButton btn_test_scene = (ImageButton) view.findViewById(R.id.btn_testScene);
        ImageButton btn_help_scene = (ImageButton) view.findViewById(R.id.btn_helpNewScene);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);


        //Init


        //Database
      //  SceneDatabase sceneDatabase = SceneDatabase.getInstance(getContext());
       // sceneRepository = SceneRepository.getInstance(SceneDataSource.getInstance(sceneDatabase.sceneDao()));


        sceneViewModel = ViewModelProviders.of(this).get(SceneViewModel.class);

        btn_new_photo.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                enviaIntentCam();


             }});
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        OPEN_GALLERY
                );
                abreGaleria();
            }
        });



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageButton btn_save_scene = (ImageButton) getView().findViewById(R.id.btn_saveScene);
        final EditText editText =(EditText) getView().findViewById(R.id.editText);

        btn_save_scene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add new scene
                if(TextUtils.isEmpty(editText.getText())){
                    Toast.makeText(getActivity(), "INTRODUCE NOMBRE", Toast.LENGTH_SHORT).show();
                }else {
                    String name = guardaNombre();


                    Scene scene = new Scene();

                    ImageView imageView = (ImageView) getView().findViewById(R.id.imageView);
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();

                    if(bitmap == null){
                        Toast.makeText(getActivity(), "TOMA UINA IMAGEN", Toast.LENGTH_SHORT).show();
                    } else {
                        String image = saveToInternalStorage(bitmap,name);
                        scene.setName(name);
                        scene.setImage(image);
                        sceneViewModel.insert(scene);
                        Toast.makeText(getActivity(), "Escena guardada", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView imageView = (ImageView) getView().findViewById(R.id.imageView);


        // super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CODE_CAMERA) {
                if (resultCode == RESULT_OK && data != null) {

                    Bitmap bmp = (Bitmap) data.getExtras().get("data");

                    imageView.setImageBitmap(bmp);


                }
            }else if(requestCode== OPEN_GALLERY){

                if(requestCode == OPEN_GALLERY && resultCode == RESULT_OK && data != null){
                    Uri uri = data.getData();

                    try {
                        InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);

                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageView.setImageBitmap(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                }



        }catch(Exception e){
            Toast.makeText(this.getActivity(), e+"Something went wrong", Toast.LENGTH_LONG).show();

        }
    }
    private void enviaIntentCam(){
        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        getActivity().startActivityFromFragment(New_sceneFragment.this, cameraIntent, CODE_CAMERA);
    }
    private void abreGaleria(){
       Intent galIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        getActivity().startActivityFromFragment(New_sceneFragment.this, galIntent, CODE_CAMERA);

    }
    private String saveToInternalStorage(Bitmap bitmapImage,String name){
        ContextWrapper cw = new ContextWrapper(getContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,name + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == OPEN_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, OPEN_GALLERY);
            }
            else {
                Toast.makeText(getContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private String guardaNombre(){

        EditText editText = (EditText) getView().findViewById(R.id.editText);
        String nombre =  editText.getText().toString();
        return nombre;

    }
    private String guardaImagePath(String name, String extension){
        String path =  name + "." + extension ;
        return path;

    }
    public void saveImage(Context context, Bitmap bitmap, String name, String extension){
        name = name + "." + extension;
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            fileOutputStream.close();
            Toast.makeText(context, "Escena guardada", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Fallo al guardar", Toast.LENGTH_SHORT).show();
        }
    }




}
