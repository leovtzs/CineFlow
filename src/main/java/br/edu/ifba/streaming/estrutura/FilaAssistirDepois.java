package br.edu.ifba.streaming.estrutura;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.streaming.model.Conteudo;

public class FilaAssistirDepois {

    private class No {
        private Conteudo conteudo;
        private No proximo;

        private No(Conteudo conteudo) {
            this.conteudo = conteudo;
            this.proximo = null;
        }
    }

    private No inicio;
    private No fim;
    private int tamanho;

    public FilaAssistirDepois() {
        inicio = null;
        fim = null;
        tamanho = 0;
    }

   
    public boolean adicionar(Conteudo conteudo) {
        if (conteudo == null || contem(conteudo.getId())) {
            return false;
        }

        No novo = new No(conteudo);

        if (estaVazia()) {
            inicio = novo;
            fim = novo;
        } else {
            fim.proximo = novo;
            fim = novo;
        }

        tamanho++;
        return true;
    }

    public List<Conteudo> listar() {
        List<Conteudo> resultado = new ArrayList<>();
        No atual = inicio;

        while (atual != null) {
            resultado.add(atual.conteudo);
            atual = atual.proximo;
        }

        return resultado;
    }

    public boolean contem(int id) {
        No atual = inicio;

        while (atual != null) {
            if (atual.conteudo.getId() == id) {
                return true;
            }

            atual = atual.proximo;
        }

        return false;
    }

    public boolean contem(Conteudo conteudo) {
        return conteudo != null && contem(conteudo.getId());
    }

    public boolean remover(int id) {
        No atual = inicio;
        No anterior = null;

        while (atual != null) {
            if (atual.conteudo.getId() == id) {
                if (anterior == null) {
                    inicio = atual.proximo;
                } else {
                    anterior.proximo = atual.proximo;
                }

                if (atual == fim) {
                    fim = anterior;
                }

                tamanho--;

                if (tamanho == 0) {
                    inicio = null;
                    fim = null;
                }

                return true;
            }

            anterior = atual;
            atual = atual.proximo;
        }

        return false;
    }

    public Conteudo proximo() {
        if (estaVazia()) {
            return null;
        }

        Conteudo removido = inicio.conteudo;
        inicio = inicio.proximo;
        tamanho--;

        if (inicio == null) {
            fim = null;
        }

        return removido;
    }

    public int tamanho() {
        return tamanho;
    }

    public boolean estaVazia() {
        return inicio == null;
    }
}
