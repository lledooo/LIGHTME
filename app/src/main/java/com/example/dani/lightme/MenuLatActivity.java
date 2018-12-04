package com.example.dani.lightme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.dani.lightme.database.SceneDatabase;
import com.example.dani.lightme.database.SceneRepository;

import io.reactivex.disposables.CompositeDisposable;

public class MenuLatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CompositeDisposable compositeDisposable;
    private SceneRepository sceneRepository;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_lat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Init
        compositeDisposable = new CompositeDisposable();

        //Database
        SceneDatabase sceneDatabase = SceneDatabase.getInstance(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager fm= getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.contenedor, new My_scenesFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fm= getSupportFragmentManager();

        if (id == R.id.nav_new_scene) {
            fm.beginTransaction().replace(R.id.contenedor, new New_sceneFragment()).commit();
        } else if (id == R.id.nav_my_scene) {
            fm.beginTransaction().replace(R.id.contenedor, new My_scenesFragment()).commit();
        } else if (id == R.id.nav_connect) {
            fm.beginTransaction().replace(R.id.contenedor, new ConnectFragment()).commit();
        } else if (id == R.id.nav_manual) {
            fm.beginTransaction().replace(R.id.contenedor, new ManualFragment()).commit();
        } else if (id == R.id.nav_help) {
            fm.beginTransaction().replace(R.id.contenedor, new HelpFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
