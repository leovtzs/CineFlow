package br.edu.ifba.streaming.estrutura;

public class ListaDinamica<T> implements Listavel<T> {
    private class No {
        private T elemento;
        private No proximo;

        public No(T elemento) {
            this.elemento = elemento;
            this.proximo = null;
        }
    }

    private No inicio;
    private int tamanho;

    public ListaDinamica() {
        inicio = null;
        tamanho = 0;
    }

    @Override
    public void adicionar(T elemento) {

        No novo = new No(elemento);

        if (inicio == null) {
            inicio = novo;
        } else {
            No atual = inicio;

            while (atual.proximo != null) {
                atual = atual.proximo;
            }

            atual.proximo = novo;
        }

        tamanho++;
    }

    @Override
    public T obter(int indice) {

        validarIndice(indice);

        No atual = inicio;

        for (int i = 0; i < indice; i++) {
            atual = atual.proximo;
        }

        return atual.elemento;
    }

    @Override
    public T remover(int indice) {

        validarIndice(indice);

        No removido;

        if (indice == 0) {
            removido = inicio;
            inicio = inicio.proximo;
        } else {
            No anterior = inicio;

            for (int i = 0; i < indice - 1; i++) {
                anterior = anterior.proximo;
            }

            removido = anterior.proximo;
            anterior.proximo = removido.proximo;
        }

        tamanho--;

        return removido.elemento;
    }

    @Override
    public int tamanho() {
        return tamanho;
    }

    @Override
    public boolean estaVazia() {
        return tamanho == 0;
    }

    private void validarIndice(int indice) {

        if (indice < 0 || indice >= tamanho) {
            throw new IndexOutOfBoundsException(
                "Índice inválido: " + indice
            );
        }
    }
}
