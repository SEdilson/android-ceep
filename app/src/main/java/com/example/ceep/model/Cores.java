package com.example.ceep.model;

public enum Cores {

    AZUL("#408EC9"),
    BRANCO("#FFFFFF"),
    VERMELHO("#EC2F4B"),
    VERDE("#9ACD32"),
    AMARELO("#F9F256"),
    LILAS("#F1CBFF"),
    CINZA("#D2D4DC"),
    MARROM("#A47C48"),
    ROXO("#BE29EC");

    private final String cor;

    Cores(final String cor) {
        this.cor = cor;
    }

    public String getCor() {
        return cor;
    }
}
