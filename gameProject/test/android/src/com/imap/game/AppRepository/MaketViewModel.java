package com.imap.game.AppRepository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.imap.game.BuildMaket;

import java.util.List;


public class MaketViewModel extends AndroidViewModel {

    private AppRepository repository;

    public MaketViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
    }

    public List<BuildMaket> getAllMakets() {
        return repository.getAllMakets();
    }

    public void insertMaket(BuildMaket maket) {
        repository.insertMaket(maket);
    }

}