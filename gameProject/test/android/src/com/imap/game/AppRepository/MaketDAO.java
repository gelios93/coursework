package com.imap.game.AppRepository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.imap.game.BuildMaket;

import java.util.List;

@Dao
public interface MaketDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMaket(BuildMaket maket);

    @Query("DELETE FROM makets")
    void deleteAll();

    @Query("SELECT * FROM makets")
    List<BuildMaket> getAll();
}
