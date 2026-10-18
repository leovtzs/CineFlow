
package br.edu.ifba.streaming.estrutura;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import br.edu.ifba.streaming.model.Conteudo;

public class FilaAssistirDepois {

    private final Queue<Conteudo> fila = new LinkedList<>();

    public boolean adicionar(Conteudo conteudo) {

        if (conteudo == null || contem(conteudo.getId())) {
            return false;
        }

        return fila.offer(conteudo);
    }

    public List<Conteudo> listar() {
        return new LinkedList<>(fila);
    }

    public boolean contem(int id) {

        for (Conteudo conteudo : fila) {
            if (conteudo.getId() == id) {
                return true;
            }
        }

        return false;
    }

    public boolean contem(Conteudo conteudo) {
        return conteudo != null && contem(conteudo.getId());
    }

    public boolean remover(int id) {
        return fila.removeIf(conteudo -> conteudo.getId() == id);
    }

    public Conteudo proximo() {
        return fila.poll();
    }

    public int tamanho() {
        return fila.size();
    }

    public boolean estaVazia() {
        return fila.isEmpty();
    }
}
