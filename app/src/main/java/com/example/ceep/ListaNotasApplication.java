//package com.example.ceep;
//
//import android.app.Application;
//
//import com.example.ceep.dao.NotaDAO;
//import com.example.ceep.model.Nota;
//
//import java.util.List;
//
//public class ListaNotasApplication extends Application {
//
//    private NotaDAO dao = new NotaDAO();
//
//    @Override
//    public void onCreate() {
//        criaNotasDeTeste();
//        super.onCreate();
//    }
//
//    public List<Nota> criaNotasDeTeste() {
//        dao.insere(new Nota("Primeira Nota", "Primeira Descrição"), new Nota(
//                "Segunda Nota", "Descrição um pouco mais longa"
//        ));
//        return dao.todos();
//    }
//}
