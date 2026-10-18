
package br.edu.ifba.streaming.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import br.edu.ifba.streaming.model.Conteudo;
import br.edu.ifba.streaming.repository.CatalogoRepository;

public class CatalogoService {

    private final CatalogoRepository repositorio;

    public CatalogoService() {
        repositorio = new CatalogoRepository();
    }

    // Valida e cadastra um conteúdo
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

        boolean adicionado = repositorio.adicionar(conteudo);

        if (!adicionado) {
            return "Já existe um conteúdo com esse ID.";
        }

        return "Conteúdo cadastrado com sucesso!";
    }

    // Lista todos os conteúdos
    public List<Conteudo> listarTodos() {
        return repositorio.listarTodos();
    }

    // Busca conteúdo pelo ID
    public Conteudo buscarPorId(int id) {
        return repositorio.buscarPorId(id);
    }

    // Busca conteúdos pelo título
    public List<Conteudo> buscarPorTitulo(String titulo) {

        List<Conteudo> encontrados = new ArrayList<>();

        if (titulo == null || titulo.trim().isEmpty()) {
            return encontrados;
        }

        String termo = titulo.trim().toLowerCase();

        for (Conteudo conteudo : repositorio.listarTodos()) {

            if (conteudo.getTitulo() != null
                    && conteudo.getTitulo()
                            .toLowerCase()
                            .contains(termo)) {

                encontrados.add(conteudo);
            }
        }

        return encontrados;
    }

    // Retorna os conteúdos ordenados alfabeticamente
    public List<Conteudo> ordenarPorTitulo() {

        List<Conteudo> conteudos = repositorio.listarTodos();

        conteudos.sort(
                Comparator.comparing(
                        Conteudo::getTitulo,
                        String.CASE_INSENSITIVE_ORDER
                )
        );

        return conteudos;
    }

    // Retorna a quantidade de conteúdos cadastrados
    public int quantidade() {
        return repositorio.quantidade();
    }
}
