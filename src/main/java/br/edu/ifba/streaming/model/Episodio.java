package br.edu.ifba.streaming.model;

public class Episodio {

    private String titulo;
    private int numero;
    private int duracao;

    public Episodio(String titulo, int numero, int duracao) {
        this.titulo = titulo;
        this.numero = numero;
        this.duracao = duracao;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getNumero() {
        return numero;
    }

    public int getDuracao() {
        return duracao;
    }
}