package br.edu.ifba.streaming.model;

import java.util.ArrayList;
import java.util.List;

public class Temporada {

    private int numero;
    private List<Episodio> episodios;

    public Temporada(int numero) {
        this.numero = numero;
        this.episodios = new ArrayList<>();
    }

    public int getNumero() {
        return numero;
    }

    public List<Episodio> getEpisodios() {
        return new ArrayList<>(episodios);
    }

    public void adicionarEpisodio(Episodio episodio) {
        episodios.add(episodio);
    }
}