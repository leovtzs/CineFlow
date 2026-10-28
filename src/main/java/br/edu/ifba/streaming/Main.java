
package br.edu.ifba.streaming;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.streaming.estrutura.FilaAssistirDepois;
import br.edu.ifba.streaming.estrutura.ListaFavoritos;
import br.edu.ifba.streaming.estrutura.PilhaDinamica;
import br.edu.ifba.streaming.model.Conteudo;
import br.edu.ifba.streaming.model.Filme;
import br.edu.ifba.streaming.model.Usuario;
import br.edu.ifba.streaming.service.CatalogoService;
import br.edu.ifba.streaming.service.UsuarioService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    private CatalogoService servico;

    private final FilaAssistirDepois filaAssistirDepois =
            new FilaAssistirDepois();

    private final ListaFavoritos listaFavoritos =
            new ListaFavoritos();

    private final PilhaDinamica<Conteudo> historico =
            new PilhaDinamica<>();

    private final UsuarioService usuarioService =
            new UsuarioService();

    private Usuario usuarioLogado;

    private Stage janela;
    private Scene cenaCatalogo;

    private BorderPane layoutPrincipal;
    private VBox conteudoPrincipal;
    private GridPane gradeCatalogo;

    private TextField campoPesquisa;

    private String filtroAtual = "Todos";

    private final String FUNDO = "#100D1C";
    private final String CARTAO = "#1D1830";
    private final String ROXO = "#8B5CF6";
    private final String TEXTO = "#F8F7FC";
    private final String TEXTO_SECUNDARIO = "#B9B3CA";

    @Override
    public void start(Stage stage) {

        janela = stage;

        carregarCatalogo();
        montarTelaCatalogo();

        janela.setTitle("CineFlow");
        janela.setMinWidth(390);
        janela.setMinHeight(650);
        janela.setScene(criarTelaLogin());
        janela.show();
    }

    // =========================================================
    // CATÁLOGO
    // =========================================================

    private void carregarCatalogo() {
        servico = CatalogoInicial.criar();
    }

    private void montarTelaCatalogo() {

        layoutPrincipal = new BorderPane();
        layoutPrincipal.setStyle("-fx-background-color: " + FUNDO + ";");

        conteudoPrincipal = new VBox(18);
        conteudoPrincipal.setPadding(new Insets(22));
        conteudoPrincipal.setStyle("-fx-background-color: " + FUNDO + ";");

        ScrollPane rolagem = new ScrollPane(conteudoPrincipal);
        rolagem.setFitToWidth(true);
        rolagem.setStyle(
                "-fx-background: " + FUNDO + ";" +
                "-fx-background-color: " + FUNDO + ";" +
                "-fx-border-color: transparent;"
        );

        layoutPrincipal.setCenter(rolagem);
        layoutPrincipal.setBottom(criarNavegacao());

        cenaCatalogo = new Scene(layoutPrincipal, 430, 780);

        mostrarCatalogo();
    }

    private void mostrarCatalogo() {

        conteudoPrincipal.getChildren().clear();

        Label marca = new Label("CINEFLOW");
        marca.setFont(Font.font("Arial", 27));
        marca.setStyle(
                "-fx-text-fill: " + ROXO + ";" +
                "-fx-font-weight: bold;"
        );

        Label subtitulo = new Label("Seu próximo universo começa aqui.");
        subtitulo.setStyle("-fx-text-fill: " + TEXTO_SECUNDARIO + ";");

        VBox cabecalho = new VBox(5, marca, subtitulo);

        campoPesquisa = new TextField();
        campoPesquisa.setPromptText("Pesquisar filmes e séries...");
        campoPesquisa.setStyle(estiloCampo());
        campoPesquisa.textProperty().addListener(
                (obs, anterior, atual) -> atualizarCatalogo()
        );

        HBox filtros = new HBox(8);
        filtros.setAlignment(Pos.CENTER_LEFT);

        String[] opcoes = {"Todos", "Filmes", "Séries"};

        for (String opcao : opcoes) {

            Button botao = criarBotao(opcao);

            botao.setOnAction(event -> {
                filtroAtual = opcao;
                atualizarCatalogo();
            });

            filtros.getChildren().add(botao);
        }

        Label tituloSecao = new Label("Explore o catálogo");
        tituloSecao.setFont(Font.font("Arial", 20));
        tituloSecao.setStyle(
                "-fx-text-fill: " + TEXTO + ";" +
                "-fx-font-weight: bold;"
        );

        gradeCatalogo = new GridPane();
        gradeCatalogo.setHgap(12);
        gradeCatalogo.setVgap(12);

        conteudoPrincipal.getChildren().addAll(
                cabecalho,
                campoPesquisa,
                filtros,
                tituloSecao,
                gradeCatalogo
        );

        atualizarCatalogo();
    }

    private void atualizarCatalogo() {

        if (gradeCatalogo == null) {
            return;
        }

        gradeCatalogo.getChildren().clear();

        String pesquisa = campoPesquisa == null
                ? ""
                : campoPesquisa.getText().trim().toLowerCase();

        List<Conteudo> conteudos = servico.listarTodos();

        int coluna = 0;
        int linha = 0;

        for (Conteudo item : conteudos) {

            boolean correspondeAoTexto =
                    item.getTitulo().toLowerCase().contains(pesquisa);

            boolean correspondeAoFiltro =
                    filtroAtual.equals("Todos")
                    || (filtroAtual.equals("Filmes")
                        && item instanceof Filme)
                    || (filtroAtual.equals("Séries")
                        && !(item instanceof Filme));

            if (!correspondeAoTexto || !correspondeAoFiltro) {
                continue;
            }

            VBox cartao = criarCartao(item);

            gradeCatalogo.add(cartao, coluna, linha);

            coluna++;

            if (coluna == 2) {
                coluna = 0;
                linha++;
            }
        }

        if (gradeCatalogo.getChildren().isEmpty()) {
            Label vazio = new Label("Nenhum conteúdo encontrado.");
            vazio.setStyle("-fx-text-fill: " + TEXTO_SECUNDARIO + ";");
            gradeCatalogo.add(vazio, 0, 0);
        }
    }

    // =========================================================
    // CARTÕES DE CONTEÚDO
    // =========================================================

    private VBox criarCartao(Conteudo item) {

        VBox cartao = new VBox(10);
        cartao.setPadding(new Insets(14));
        cartao.setPrefWidth(165);
        cartao.setMinHeight(235);
        cartao.setStyle(
                "-fx-background-color: " + CARTAO + ";" +
                "-fx-background-radius: 14;"
        );

        Label poster = new Label(item instanceof Filme ? "🎬" : "📺");
        poster.setFont(Font.font("Arial", 42));
        poster.setAlignment(Pos.CENTER);
        poster.setMaxWidth(Double.MAX_VALUE);

        Label titulo = new Label(item.getTitulo());
        titulo.setWrapText(true);
        titulo.setFont(Font.font("Arial", 15));
        titulo.setStyle(
                "-fx-text-fill: " + TEXTO + ";" +
                "-fx-font-weight: bold;"
        );

        Label detalhes = new Label(
                item.getAno() + " • " + item.getGenero()
        );
        detalhes.setWrapText(true);
        detalhes.setStyle("-fx-text-fill: " + TEXTO_SECUNDARIO + ";");

        Label classificacao = new Label(item.getClassificacao());
        classificacao.setStyle("-fx-text-fill: " + TEXTO_SECUNDARIO + ";");

        Button assistir = criarBotao("▶ Assistir");
        assistir.setMaxWidth(Double.MAX_VALUE);
        assistir.setOnAction(event -> abrirPlayer(item));

        Button depois = criarBotaoSecundario("＋ Assistir depois");
        depois.setMaxWidth(Double.MAX_VALUE);
        depois.setOnAction(event -> {

            boolean adicionado = filaAssistirDepois.adicionar(item);

            if (adicionado) {
                mostrarAviso(
                        "Minha lista",
                        "Conteúdo adicionado à fila Assistir Depois!"
                );
            } else {
                mostrarAviso(
                        "Minha lista",
                        "Esse conteúdo já está na sua fila."
                );
            }
        });

        Button favorito = criarBotaoSecundario(
                listaFavoritos.contem(item.getId())
                        ? "♥ Favoritado"
                        : "♡ Favoritar"
        );

        favorito.setMaxWidth(Double.MAX_VALUE);

        favorito.setOnAction(event -> {

            if (listaFavoritos.contem(item.getId())) {

                listaFavoritos.remover(item.getId());

                favorito.setText("♡ Favoritar");

                mostrarAviso(
                        "Favoritos",
                        "Conteúdo removido dos favoritos."
                );

            } else {

                boolean adicionado = listaFavoritos.adicionar(item);

                if (adicionado) {
                    favorito.setText("♥ Favoritado");

                    mostrarAviso(
                            "Favoritos",
                            "Conteúdo adicionado aos favoritos!"
                    );
                }
            }
        });

        cartao.getChildren().addAll(
                poster,
                titulo,
                detalhes,
                classificacao,
                assistir,
                depois,
                favorito
        );

        return cartao;
    }

    // =========================================================
    // PLAYER
    // =========================================================

    private void abrirPlayer(Conteudo item) {

        historico.empilhar(item);

        conteudoPrincipal.getChildren().clear();

        Button voltar = criarBotaoSecundario("← Voltar ao catálogo");
        voltar.setOnAction(event -> mostrarCatalogo());

        Label simbolo = new Label("▶");
        simbolo.setFont(Font.font("Arial", 55));
        simbolo.setTextFill(Color.WHITE);

        VBox telaVideo = new VBox(simbolo);
        telaVideo.setAlignment(Pos.CENTER);
        telaVideo.setMinHeight(230);
        telaVideo.setStyle(
                "-fx-background-color: #080710;" +
                "-fx-background-radius: 15;"
        );

        Label titulo = new Label(item.getTitulo());
        titulo.setFont(Font.font("Arial", 25));
        titulo.setWrapText(true);
        titulo.setStyle(
                "-fx-text-fill: " + TEXTO + ";" +
                "-fx-font-weight: bold;"
        );

        Label tipo = new Label(
                (item instanceof Filme ? "Filme" : "Série")
                + " • " + item.getAno()
        );
        tipo.setStyle("-fx-text-fill: " + TEXTO_SECUNDARIO + ";");

        Label genero = new Label("Gênero: " + item.getGenero());
        genero.setStyle("-fx-text-fill: " + TEXTO + ";");

        Label classificacao = new Label(
                "Classificação indicativa: " + item.getClassificacao()
        );
        classificacao.setStyle("-fx-text-fill: " + TEXTO_SECUNDARIO + ";");

        Label aviso = new Label(
                "Área de reprodução demonstrativa do CineFlow."
        );
        aviso.setWrapText(true);
        aviso.setStyle("-fx-text-fill: " + TEXTO_SECUNDARIO + ";");

        conteudoPrincipal.getChildren().addAll(
                voltar,
                telaVideo,
                titulo,
                tipo,
                genero,
                classificacao,
                aviso
        );
    }

    // =========================================================
    // FILA: ASSISTIR DEPOIS
    // =========================================================

    private void mostrarMinhaLista() {

        conteudoPrincipal.getChildren().clear();

        Button voltar = criarBotaoSecundario("← Voltar ao catálogo");
        voltar.setOnAction(event -> mostrarCatalogo());

        Label titulo = criarTitulo("Minha lista");
        Label descricao = criarDescricao(
                "Conteúdos que você quer assistir depois."
        );

        conteudoPrincipal.getChildren().addAll(
                voltar,
                titulo,
                descricao
        );

        List<Conteudo> itens = filaAssistirDepois.listar();

        if (itens.isEmpty()) {
            conteudoPrincipal.getChildren().add(
                    criarDescricao("Sua fila está vazia por enquanto.")
            );
            return;
        }

        for (Conteudo item : itens) {

            VBox linha = criarLinhaConteudo(item);

            Button assistir = criarBotao("▶ Assistir");
            assistir.setOnAction(event -> abrirPlayer(item));

            Button remover = criarBotaoSecundario("Remover");
            remover.setOnAction(event -> {
                filaAssistirDepois.remover(item.getId());
                mostrarMinhaLista();
            });

            linha.getChildren().addAll(assistir, remover);
            conteudoPrincipal.getChildren().add(linha);
        }
    }

    // =========================================================
    // LISTA: FAVORITOS
    // =========================================================

    private void mostrarFavoritos() {

        conteudoPrincipal.getChildren().clear();

        Button voltar = criarBotaoSecundario("← Voltar ao catálogo");
        voltar.setOnAction(event -> mostrarCatalogo());

        Label titulo = criarTitulo("Meus favoritos");
        Label descricao = criarDescricao(
                "Os conteúdos que você marcou com coração."
        );

        conteudoPrincipal.getChildren().addAll(
                voltar,
                titulo,
                descricao
        );

        List<Conteudo> favoritos = listaFavoritos.listar();

        if (favoritos.isEmpty()) {
            conteudoPrincipal.getChildren().add(
                    criarDescricao("Você ainda não tem favoritos.")
            );
            return;
        }

        for (Conteudo item : favoritos) {

            VBox linha = criarLinhaConteudo(item);

            Button assistir = criarBotao("▶ Assistir");
            assistir.setOnAction(event -> abrirPlayer(item));

            Button remover = criarBotaoSecundario("♥ Remover favorito");
            remover.setOnAction(event -> {
                listaFavoritos.remover(item.getId());
                mostrarFavoritos();
            });

            linha.getChildren().addAll(assistir, remover);
            conteudoPrincipal.getChildren().add(linha);
        }
    }

    // =========================================================
    // PILHA: HISTÓRICO
    // =========================================================

    private void mostrarHistorico() {

        conteudoPrincipal.getChildren().clear();

        Button voltar = criarBotaoSecundario("← Voltar ao catálogo");
        voltar.setOnAction(event -> mostrarCatalogo());

        Label titulo = criarTitulo("Histórico");
        Label descricao = criarDescricao(
                "Seus conteúdos abertos recentemente."
        );

        conteudoPrincipal.getChildren().addAll(
                voltar,
                titulo,
                descricao
        );

        if (historico.estaVazia()) {
            conteudoPrincipal.getChildren().add(
                    criarDescricao("Seu histórico está vazio.")
            );
            return;
        }

        /*
         * A pilha é percorrida do topo para a base.
         * Usamos uma pilha auxiliar para não perder o histórico.
         */
        PilhaDinamica<Conteudo> auxiliar = new PilhaDinamica<>();
        List<Conteudo> itens = new ArrayList<>();

        while (!historico.estaVazia()) {
            Conteudo item = historico.desempilhar();

            itens.add(item);
            auxiliar.empilhar(item);
        }

        while (!auxiliar.estaVazia()) {
            historico.empilhar(auxiliar.desempilhar());
        }

        for (Conteudo item : itens) {

            VBox linha = criarLinhaConteudo(item);

            Button assistir = criarBotao("▶ Assistir novamente");
            assistir.setOnAction(event -> abrirPlayer(item));

            linha.getChildren().add(assistir);
            conteudoPrincipal.getChildren().add(linha);
        }

        Button limpar = criarBotaoSecundario("Limpar histórico");
        limpar.setOnAction(event -> {

            while (!historico.estaVazia()) {
                historico.desempilhar();
            }

            mostrarHistorico();
        });

        conteudoPrincipal.getChildren().add(limpar);
    }

    // =========================================================
    // PERFIL
    // =========================================================

    private void mostrarPerfil(Stage stage) {

        conteudoPrincipal.getChildren().clear();

        Button voltar = criarBotaoSecundario("← Voltar ao catálogo");
        voltar.setOnAction(event -> mostrarCatalogo());

        Label titulo = criarTitulo("Meu perfil");

        String nome = usuarioLogado == null
                ? "Usuário"
                : usuarioLogado.getNome();

        String email = usuarioLogado == null
                ? ""
                : usuarioLogado.getEmail();

        Label nomeUsuario = new Label("Nome: " + nome);
        nomeUsuario.setStyle("-fx-text-fill: " + TEXTO + ";");

        Label emailUsuario = new Label("E-mail: " + email);
        emailUsuario.setWrapText(true);
        emailUsuario.setStyle("-fx-text-fill: " + TEXTO_SECUNDARIO + ";");

        Button minhaLista = criarBotao("Minha lista");
        minhaLista.setOnAction(event -> mostrarMinhaLista());

        Button favoritos = criarBotao("Meus favoritos");
        favoritos.setOnAction(event -> mostrarFavoritos());

        Button historicoBotao = criarBotao("Meu histórico");
        historicoBotao.setOnAction(event -> mostrarHistorico());

        Button sair = criarBotaoSecundario("Sair da conta");
        sair.setOnAction(event -> {
            usuarioLogado = null;
            stage.setScene(criarTelaLogin());
        });

        conteudoPrincipal.getChildren().addAll(
                voltar,
                titulo,
                nomeUsuario,
                emailUsuario,
                minhaLista,
                favoritos,
                historicoBotao,
                sair
        );
    }

    // =========================================================
    // LOGIN
    // =========================================================

    private Scene criarTelaLogin() {

        VBox tela = criarTelaAutenticacao();

        Label marca = new Label("CINEFLOW");
        marca.setFont(Font.font("Arial", 32));
        marca.setStyle(
                "-fx-text-fill: " + ROXO + ";" +
                "-fx-font-weight: bold;"
        );

        Label subtitulo = criarDescricao(
                "Entre para explorar filmes e séries."
        );

        TextField email = new TextField();
        email.setPromptText("Seu e-mail");
        email.setStyle(estiloCampo());

        PasswordField senha = new PasswordField();
        senha.setPromptText("Sua senha");
        senha.setStyle(estiloCampo());

        Button entrar = criarBotao("Entrar");
        entrar.setMaxWidth(Double.MAX_VALUE);

        entrar.setOnAction(event -> {

            String emailDigitado = email.getText().trim();
            String senhaDigitada = senha.getText();

            if (emailDigitado.isEmpty() || senhaDigitada.isEmpty()) {
                mostrarAviso(
                        "Campos obrigatórios",
                        "Preencha o e-mail e a senha."
                );
                return;
            }

            try {
                Usuario usuario = usuarioService.login(
                        emailDigitado,
                        senhaDigitada
                );

                if (usuario != null) {
                    usuarioLogado = usuario;
                    mostrarCatalogo();
                    janela.setScene(cenaCatalogo);
                } else {
                    mostrarAviso(
                            "Não foi possível entrar",
                            "Confira seu e-mail e sua senha."
                    );
                }

            } catch (Exception erro) {
                mostrarAviso(
                        "Erro ao entrar",
                        "Não foi possível realizar o login: "
                                + erro.getMessage()
                );
            }
        });

        Button cadastrar = criarBotaoSecundario("Criar conta");
        cadastrar.setMaxWidth(Double.MAX_VALUE);
        cadastrar.setOnAction(event ->
                janela.setScene(criarTelaCadastro())
        );

        tela.getChildren().addAll(
                marca,
                subtitulo,
                email,
                senha,
                entrar,
                cadastrar
        );

        return new Scene(tela, 430, 780);
    }

    // =========================================================
    // CADASTRO
    // =========================================================

    private Scene criarTelaCadastro() {

        VBox tela = criarTelaAutenticacao();

        Label titulo = criarTitulo("Criar conta");

        Label descricao = criarDescricao(
                "Cadastre-se para começar sua experiência."
        );

        TextField nome = new TextField();
        nome.setPromptText("Nome completo");
        nome.setStyle(estiloCampo());

        TextField email = new TextField();
        email.setPromptText("E-mail");
        email.setStyle(estiloCampo());

        PasswordField senha = new PasswordField();
        senha.setPromptText("Senha");
        senha.setStyle(estiloCampo());

        PasswordField confirmarSenha = new PasswordField();
        confirmarSenha.setPromptText("Confirme sua senha");
        confirmarSenha.setStyle(estiloCampo());

        Button cadastrar = criarBotao("Cadastrar");
        cadastrar.setMaxWidth(Double.MAX_VALUE);

        cadastrar.setOnAction(event -> {

            String nomeDigitado = nome.getText().trim();
            String emailDigitado = email.getText().trim();
            String senhaDigitada = senha.getText();
            String confirmacao = confirmarSenha.getText();

            if (nomeDigitado.isEmpty()
                    || emailDigitado.isEmpty()
                    || senhaDigitada.isEmpty()
                    || confirmacao.isEmpty()) {

                mostrarAviso(
                        "Campos obrigatórios",
                        "Preencha todos os campos."
                );
                return;
            }

            try {
                String erro = usuarioService.cadastrar(
                        nomeDigitado,
                        emailDigitado,
                        senhaDigitada,
                        confirmacao
                );

                if (erro == null) {
                    mostrarAviso(
                            "Cadastro concluído",
                            "Sua conta foi criada. Agora faça login."
                    );

                    janela.setScene(criarTelaLogin());

                } else {
                    mostrarAviso("Não foi possível cadastrar", erro);
                }

            } catch (Exception ex) {
                mostrarAviso(
                        "Erro no cadastro",
                        "Não foi possível criar sua conta: "
                                + ex.getMessage()
                );
            }
        });

        Button voltar = criarBotaoSecundario("Voltar para o login");
        voltar.setMaxWidth(Double.MAX_VALUE);
        voltar.setOnAction(event ->
                janela.setScene(criarTelaLogin())
        );

        tela.getChildren().addAll(
                titulo,
                descricao,
                nome,
                email,
                senha,
                confirmarSenha,
                cadastrar,
                voltar
        );

        return new Scene(tela, 430, 780);
    }


    private HBox criarNavegacao() {

        Button inicio = criarBotaoNavegacao("⌂ Início");
        inicio.setOnAction(event -> mostrarCatalogo());

        Button explorar = criarBotaoNavegacao("⌕ Explorar");
        explorar.setOnAction(event -> mostrarCatalogo());

        Button minhaLista = criarBotaoNavegacao("♡ Minha lista");
        minhaLista.setOnAction(event -> mostrarMinhaLista());

        Button historicoBotao = criarBotaoNavegacao("◷ Histórico");
        historicoBotao.setOnAction(event -> mostrarHistorico());

        Button perfil = criarBotaoNavegacao("☻ Perfil");
        perfil.setOnAction(event -> mostrarPerfil(janela));

        HBox navegacao = new HBox(
                5,
                inicio,
                explorar,
                minhaLista,
                historicoBotao,
                perfil
        );

        navegacao.setAlignment(Pos.CENTER);
        navegacao.setPadding(new Insets(10, 5, 10, 5));
        navegacao.setStyle(
                "-fx-background-color: #191429;" +
                "-fx-border-color: #302746 transparent transparent transparent;"
        );

        return navegacao;
    }

    private VBox criarTelaAutenticacao() {

        VBox tela = new VBox(16);
        tela.setAlignment(Pos.CENTER);
        tela.setPadding(new Insets(30));
        tela.setStyle("-fx-background-color: " + FUNDO + ";");

        return tela;
    }

    private VBox criarLinhaConteudo(Conteudo item) {

        VBox linha = new VBox(8);
        linha.setPadding(new Insets(15));
        linha.setStyle(
                "-fx-background-color: " + CARTAO + ";" +
                "-fx-background-radius: 12;"
        );

        Label titulo = new Label(item.getTitulo());
        titulo.setFont(Font.font("Arial", 17));
        titulo.setWrapText(true);
        titulo.setStyle(
                "-fx-text-fill: " + TEXTO + ";" +
                "-fx-font-weight: bold;"
        );

        Label detalhes = new Label(
                item.getAno() + " • " + item.getGenero()
        );
        detalhes.setStyle("-fx-text-fill: " + TEXTO_SECUNDARIO + ";");

        linha.getChildren().addAll(titulo, detalhes);

        return linha;
    }

    private Label criarTitulo(String texto) {

        Label label = new Label(texto);
        label.setFont(Font.font("Arial", 24));
        label.setWrapText(true);
        label.setStyle(
                "-fx-text-fill: " + TEXTO + ";" +
                "-fx-font-weight: bold;"
        );

        return label;
    }

    private Label criarDescricao(String texto) {

        Label label = new Label(texto);
        label.setWrapText(true);
        label.setStyle("-fx-text-fill: " + TEXTO_SECUNDARIO + ";");

        return label;
    }

    private Button criarBotao(String texto) {

        Button botao = new Button(texto);

        botao.setStyle(
                "-fx-background-color: " + ROXO + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 9;" +
                "-fx-padding: 10 12 10 12;" +
                "-fx-cursor: hand;"
        );

        return botao;
    }

    private Button criarBotaoSecundario(String texto) {

        Button botao = new Button(texto);

        botao.setStyle(
                "-fx-background-color: #302746;" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 9;" +
                "-fx-padding: 10 12 10 12;" +
                "-fx-cursor: hand;"
        );

        return botao;
    }

    private Button criarBotaoNavegacao(String texto) {

        Button botao = new Button(texto);

        botao.setMaxWidth(Double.MAX_VALUE);
        botao.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: " + TEXTO + ";" +
                "-fx-font-size: 10px;" +
                "-fx-padding: 8 3 8 3;" +
                "-fx-cursor: hand;"
        );

        HBox.setHgrow(botao, Priority.ALWAYS);

        return botao;
    }

    private String estiloCampo() {

        return
                "-fx-background-color: #211B33;" +
                "-fx-text-fill: white;" +
                "-fx-prompt-text-fill: #A69DBB;" +
                "-fx-background-radius: 9;" +
                "-fx-border-color: #39304F;" +
                "-fx-border-radius: 9;" +
                "-fx-padding: 12;";
    }

    private void mostrarAviso(String titulo, String mensagem) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
