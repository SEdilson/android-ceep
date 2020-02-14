package com.example.ceep.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ceep.model.Nota;

import java.util.List;

@Dao
public interface RoomNotaDAO {

    @Insert
    Long salva(Nota nota);

    @Query("SELECT * FROM nota")
    List<Nota> todasNotas();

    @Delete
    void remove(Nota nota);

    @Update
    void edita(Nota nota);
}
