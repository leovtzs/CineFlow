package br.edu.ifba.streaming.model;

public class Filme extends Conteudo {

    private int duracao;

    public Filme(
            int id,
            String titulo,
            String genero,
            int ano,
            String classificacao,
            int duracao) {

        super(id, titulo, genero, ano, classificacao);
        this.duracao = duracao;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }
}