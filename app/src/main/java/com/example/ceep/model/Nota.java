package com.example.ceep.model;

import java.io.Serializable;

//@Entity
public class Nota implements Serializable {

//    @PrimaryKey(autoGenerate = true)
//    private int id = 0;
    private String titulo;
    private String descricao;
    private int cor;

    public Nota(String titulo, String descricao, int cor) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.cor = cor;
    }

    public Nota() {

    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getCor() {
        return cor;
    }

    public void setCor(int cor) {
        this.cor = cor;
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}