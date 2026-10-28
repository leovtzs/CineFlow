
package br.edu.ifba.streaming.estrutura;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.streaming.model.Conteudo;

public class ListaFavoritos {

    private final ListaDinamica<Conteudo> favoritos;

    public ListaFavoritos() {
        favoritos = new ListaDinamica<>();
    }

    public boolean adicionar(Conteudo conteudo) {
        if (conteudo == null || contem(conteudo.getId())) {
            return false;
        }

        favoritos.adicionar(conteudo);
        return true;
    }

    public List<Conteudo> listar() {
        List<Conteudo> resultado = new ArrayList<>();

        for (int i = 0; i < favoritos.tamanho(); i++) {
            resultado.add(favoritos.obter(i));
        }

        return resultado;
    }

    public boolean contem(int id) {
        for (int i = 0; i < favoritos.tamanho(); i++) {
            Conteudo conteudo = favoritos.obter(i);

            if (conteudo.getId() == id) {
                return true;
            }
        }

        return false;
    }

    public boolean remover(int id) {
        for (int i = 0; i < favoritos.tamanho(); i++) {
            Conteudo conteudo = favoritos.obter(i);

            if (conteudo.getId() == id) {
                favoritos.remover(i);
                return true;
            }
        }

        return false;
    }

    public int tamanho() {
        return favoritos.tamanho();
    }

    public boolean estaVazia() {
        return favoritos.estaVazia();
    }

    public void limpar() {
        while (!favoritos.estaVazia()) {
            favoritos.remover(0);
        }
    }
}
