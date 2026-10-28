
package br.edu.ifba.streaming.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import br.edu.ifba.streaming.estrutura.ListaDinamica;
import br.edu.ifba.streaming.model.Conteudo;

public class CatalogoService {

    private final ListaDinamica<Conteudo> catalogo;

    public CatalogoService() {
        catalogo = new ListaDinamica<>();
    }

   
    public String cadastrar(Conteudo conteudo) {

        if (conteudo == null) {
            return "Conteúdo inválido.";
        }

        if (conteudo.getTitulo() == null
                || conteudo.getTitulo().trim().isEmpty()) {
            return "O título é obrigatório.";
        }

        if (conteudo.getAno() <= 0) {
            return "O ano deve ser válido.";
        }

        if (buscarPorId(conteudo.getId()) != null) {
            return "Já existe um conteúdo com esse ID.";
        }

        catalogo.adicionar(conteudo);

        return "Conteúdo cadastrado com sucesso!";
    }


    public List<Conteudo> listarTodos() {

        List<Conteudo> resultado = new ArrayList<>();

        for (int i = 0; i < catalogo.tamanho(); i++) {
            resultado.add(catalogo.obter(i));
        }

        return resultado;
    }

   
    public Conteudo buscarPorId(int id) {

        for (int i = 0; i < catalogo.tamanho(); i++) {

            Conteudo conteudo = catalogo.obter(i);

            if (conteudo.getId() == id) {
                return conteudo;
            }
        }

        return null;
    }

  
    public List<Conteudo> buscarPorTitulo(String titulo) {

        List<Conteudo> encontrados = new ArrayList<>();

        if (titulo == null || titulo.trim().isEmpty()) {
            return encontrados;
        }

        String termo = titulo.trim().toLowerCase(Locale.ROOT);

        for (int i = 0; i < catalogo.tamanho(); i++) {

            Conteudo conteudo = catalogo.obter(i);

            if (conteudo.getTitulo() == null) {
                continue;
            }

            String tituloConteudo =
                    conteudo.getTitulo().toLowerCase(Locale.ROOT);

            if (tituloConteudo.contains(termo)) {
                encontrados.add(conteudo);
            }
        }

        return encontrados;
    }

    public List<Conteudo> ordenarPorTitulo() {

        List<Conteudo> conteudos = listarTodos();

        conteudos.sort(
                Comparator.comparing(
                        Conteudo::getTitulo,
                        Comparator.nullsLast(
                                String.CASE_INSENSITIVE_ORDER
                        )
                )
        );

        return conteudos;
    }

    public int quantidade() {
        return catalogo.tamanho();
    }
}
