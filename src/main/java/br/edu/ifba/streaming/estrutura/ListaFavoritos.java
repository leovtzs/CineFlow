package br.edu.ifba.streaming.estrutura;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.streaming.model.Conteudo;

public class ListaFavoritos {

    private final List<Conteudo> favoritos = new ArrayList<>();

    public boolean adicionar(Conteudo conteudo) {
        if (conteudo == null || contem(conteudo.getId())) {
            return false;
        }

        return favoritos.add(conteudo);
    }

    public List<Conteudo> listar() {
        return new ArrayList<>(favoritos);
    }

    public boolean contem(int id) {
        for (Conteudo conteudo : favoritos) {
            if (conteudo.getId() == id) {
                return true;
            }
        }

        return false;
    }

    public boolean remover(int id) {
        return favoritos.removeIf(conteudo -> conteudo.getId() == id);
    }

    public int tamanho() {
        return favoritos.size();
    }

    public boolean estaVazia() {
        return favoritos.isEmpty();
    }

    public void limpar() {
        favoritos.clear();
    }
}