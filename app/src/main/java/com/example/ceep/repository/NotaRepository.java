package com.example.ceep.repository;

import android.content.Context;

import com.example.ceep.asynctask.BaseAsyncTask;
import com.example.ceep.database.CeepDatabase;
import com.example.ceep.database.dao.RoomNotaDAO;
import com.example.ceep.model.Nota;

import java.util.List;

public class NotaRepository {

    private final RoomNotaDAO dao;

    public NotaRepository(Context context) {
        CeepDatabase db = CeepDatabase.getInstance(context);
        dao = db.getRoomNotaDAO();
    }

    public void buscaNotas(DadosCarregadosCallback<List<Nota>>callback) {
        new BaseAsyncTask<>(dao::todasNotas,
                callback::quandoSucesso).execute();
    }

    public void atualizaNota(Nota nota,
                              DadosCarregadosCallback<Nota> callback) {
        new BaseAsyncTask<>(() -> {
            dao.edita(nota);
            return nota;
        }, callback::quandoSucesso)
                .execute();
    }

    public void salvaNota(Nota nota, DadosCarregadosCallback<Nota> callback) {
        new BaseAsyncTask<>(() -> {
            long id = dao.salva(nota);
            return dao.buscaNota(id);
        }, callback::quandoSucesso)
                .execute();
    }

    public void removeNota(Nota nota, DadosCarregadosCallback<Void> callback) {
        new BaseAsyncTask<>(() -> {
            dao.remove(nota);
            return null;
        }, callback::quandoSucesso)
                .execute();
    }

    public interface DadosCarregadosCallback<T> {
        void quandoSucesso(T resposta);
        void quandoErro(String erro);
    }
}
