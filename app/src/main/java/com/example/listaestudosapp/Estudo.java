package com.example.listaestudosapp;

public class Estudo {
    private String id;
    private String titulo;
    private String prazo;
    private boolean feito;

    public Estudo() {}

    public Estudo(String id, String titulo, String prazo, boolean feito) {
        this.id = id;
        this.titulo = titulo;
        this.prazo = prazo;
        this.feito = feito;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getPrazo() { return prazo; }
    public boolean isFeito() { return feito; }

    public void setId(String id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setPrazo(String prazo) { this.prazo = prazo; }
    public void setFeito(boolean feito) { this.feito = feito; }
}