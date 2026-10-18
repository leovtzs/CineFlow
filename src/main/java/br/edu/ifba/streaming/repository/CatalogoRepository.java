
package br.edu.ifba.streaming.repository;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.streaming.model.Conteudo;

public class CatalogoRepository {

    private final List<Conteudo> catalogo = new ArrayList<>();

    // Adiciona um conteúdo, impedindo IDs duplicados
    public boolean adicionar(Conteudo conteudo) {

        if (conteudo == null) {
            return false;
        }

        if (buscarPorId(conteudo.getId()) != null) {
            return false;
        }

        catalogo.add(conteudo);
        return true;
    }

    // Busca um conteúdo pelo ID
    public Conteudo buscarPorId(int id) {

        for (Conteudo conteudo : catalogo) {
            if (conteudo.getId() == id) {
                return conteudo;
            }
        }

        return null;
    }

    // Retorna uma cópia da lista de conteúdos
    public List<Conteudo> listarTodos() {
        return new ArrayList<>(catalogo);
    }

    // Remove um conteúdo pelo ID
    public boolean remover(int id) {

        for (int i = 0; i < catalogo.size(); i++) {
            if (catalogo.get(i).getId() == id) {
                catalogo.remove(i);
                return true;
            }
        }

        return false;
    }

    // Informa quantos conteúdos existem no catálogo
    public int quantidade() {
        return catalogo.size();
    }
}
