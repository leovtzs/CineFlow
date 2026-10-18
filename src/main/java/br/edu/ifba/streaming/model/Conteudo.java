package br.edu.ifba.streaming.model;

public abstract class Conteudo {

    private int id;
    private String titulo;
    private String genero;
    private int ano;
    private String classificacao;

    public Conteudo(
            int id,
            String titulo,
            String genero,
            int ano,
            String classificacao) {

        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.ano = ano;
        this.classificacao = classificacao;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getGenero() {
        return genero;
    }

    public int getAno() {
        return ano;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }
}