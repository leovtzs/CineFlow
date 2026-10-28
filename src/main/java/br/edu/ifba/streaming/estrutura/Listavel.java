
package br.edu.ifba.streaming.estrutura;

public interface Listavel<T> {

    void adicionar(T elemento);

    T obter(int indice);

    T remover(int indice);

    int tamanho();

    boolean estaVazia();
}
