
package br.edu.ifba.streaming.estrutura;

public class PilhaDinamica<T> {

    private class No {
        private T elemento;
        private No proximo;

        private No(T elemento) {
            this.elemento = elemento;
            this.proximo = null;
        }
    }

    private No topo;
    private int tamanho;

    public PilhaDinamica() {
        topo = null;
        tamanho = 0;
    }

    public void empilhar(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException(
                "Não é possível empilhar um elemento nulo."
            );
        }

        No novo = new No(elemento);
        novo.proximo = topo;
        topo = novo;
        tamanho++;
    }

    public T topo() {
        if (estaVazia()) {
            return null;
        }

        return topo.elemento;
    }

    public T desempilhar() {
        if (estaVazia()) {
            return null;
        }

        T elementoRemovido = topo.elemento;
        topo = topo.proximo;
        tamanho--;

        return elementoRemovido;
    }

    public int tamanho() {
        return tamanho;
    }

    public boolean estaVazia() {
        return topo == null;
    }

    public void limpar() {
        while (!estaVazia()) {
            desempilhar();
        }
    }
}
