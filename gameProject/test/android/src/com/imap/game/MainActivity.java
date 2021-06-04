package com.imap.game;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.imap.game.AppRepository.AppDatabase;
import com.imap.game.AppRepository.MaketViewModel;

import lombok.SneakyThrows;


public class MainActivity extends AppCompatActivity implements AndroidFragmentApplication.Callbacks {

    public static boolean isCreate = false;
    private MaketViewModel mViewModel;
    private Fragment_ game;
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;



                    switch (menuItem.getItemId()){
                        case R.id.map:
                            break;

                        case R.id.profile:
                            break;

                        case R.id.game:
                            selectedFragment = game;
                            break;

                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

    @SneakyThrows
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewModel();

        if(isCreate) {
            Thread thread = new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(this.getApplication());
                Log.d("ONCREATE", "Database has been created.");
                Array<BuildMaket> makets = new Array<>();
                makets.add(new BuildMaket("less", new Vector3(2, 2, 2), new Array<Texture>()));
                makets.get(0).getTileset().add(
                        new Texture(2, 0, 0, 6),
                        new Texture(2, 1, 0, 10),
                        new Texture(2, 0, 1, 0),
                        new Texture(2, 1, 1, 8));
                makets.get(0).getTileset().add(
                        new Texture(3, 0, 0, 3),
                        new Texture(3, 1, 0, 7),
                        new Texture(3, 0, 1, 4),
                        new Texture(3, 1, 1, 5));
                db.maketDAO().insertMaket(makets.get(0));
            });
            thread.start();
            thread.join();
        }

        Array<BuildMaket> makets = new Array<>();
        for (BuildMaket maket : mViewModel.getAllMakets()) {
            makets.add(maket);
        }
        game = new Fragment_(makets);

        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(navListener);
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MaketViewModel.class);

    }


    @Override
    public void exit() {

    }


}