
package br.edu.ifba.streaming;

import br.edu.ifba.streaming.model.Filme;
import br.edu.ifba.streaming.model.Serie;
import br.edu.ifba.streaming.service.CatalogoService;

public class CatalogoInicial {

    public static CatalogoService criar() {
        CatalogoService catalogo = new CatalogoService();

        catalogo.cadastrar(
            new Filme(1, "Interestelar", "Ficção científica",
                2014, "10 anos", 169)
        );

        catalogo.cadastrar(
            new Filme(2, "Duna", "Aventura",
                2021, "14 anos", 155)
        );

        catalogo.cadastrar(
            new Serie(3, "Stranger Things", "Suspense",
                2016, "16 anos")
        );

        catalogo.cadastrar(
            new Serie(4, "Dark", "Mistério",
                2017, "16 anos")
        );

        catalogo.cadastrar(
            new Serie(5, "Arcane", "Animação",
                2021, "16 anos")
        );

        return catalogo;
    }
}
