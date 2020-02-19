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
    long salva(Nota nota);

    @Query("SELECT * FROM nota ORDER BY posicao DESC")
    List<Nota> todasNotas();

    @Query("SELECT * FROM Nota WHERE id = :id")
    Nota buscaNota(long id);

    @Delete
    void remove(Nota nota);

    @Update
    void edita(Nota nota);
}
