package br.edu.ifba.streaming.model;

import java.util.ArrayList;
import java.util.List;

public class Serie extends Conteudo {

    private List<Temporada> temporadas;

    public Serie(
            int id,
            String titulo,
            String genero,
            int ano,
            String classificacao) {

        super(id, titulo, genero, ano, classificacao);
        this.temporadas = new ArrayList<>();
    }

    public List<Temporada> getTemporadas() {
        return new ArrayList<>(temporadas);
    }

    public void adicionarTemporada(Temporada temporada) {
        temporadas.add(temporada);
    }
}