package com.imap.game.AppRepository;

import android.content.Context;
import android.util.Log;

import com.imap.game.BuildMaket;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;

public class AppRepository {

    private Executor executor = Executors.newSingleThreadExecutor();
    private static AppRepository instance;
    private AppDatabase db;

    public static AppRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AppRepository(context);
        }
        return instance;
    }

    private AppRepository(Context context) {
        db = AppDatabase.getInstance(context);
    }

    @SneakyThrows
    public List<BuildMaket> getAllMakets() {
        List<BuildMaket> m;
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<List<BuildMaket>> result = es.submit(() -> db.maketDAO().getAll());

        m = result.get();
        Log.d("ONLOAD","Data has been load.");

        es.shutdown();
        es.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

//        Thread thread = new Thread(()->{
//            m = db.maketDAO().getAll();
//        });

        return m;
    }

    public void insertMaket(BuildMaket maket) {
        executor.execute(() -> db.maketDAO().insertMaket(maket));
    }
}