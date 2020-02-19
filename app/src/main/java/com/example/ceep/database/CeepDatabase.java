package com.example.ceep.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ceep.database.dao.RoomNotaDAO;
import com.example.ceep.model.Nota;

@Database(entities = {Nota.class}, version = 1, exportSchema = false)
public abstract class CeepDatabase extends RoomDatabase {

    private static final String NOTA_DB = "nota.db";

    public abstract RoomNotaDAO getRoomNotaDAO();

    public static CeepDatabase getInstance(Context context) {
        return Room.databaseBuilder(context, CeepDatabase.class, NOTA_DB).build();
    }

}
