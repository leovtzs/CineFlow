
package br.edu.ifba.streaming;

import java.util.List;

import br.edu.ifba.streaming.estrutura.FilaAssistirDepois;
import br.edu.ifba.streaming.model.Conteudo;
import br.edu.ifba.streaming.model.Filme;
import br.edu.ifba.streaming.model.Usuario;
import br.edu.ifba.streaming.service.CatalogoService;
import br.edu.ifba.streaming.service.UsuarioService;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import br.edu.ifba.streaming.estrutura.ListaFavoritos;

public class Main extends Application {


    private CatalogoService servico;
    private final FilaAssistirDepois filaAssistirDepois = new FilaAssistirDepois();
    private final UsuarioService usuarioService = new UsuarioService();

    private Usuario usuarioLogado;
    private Scene cenaCatalogo;

    private GridPane gradeCatalogo;
    private TextField campoPesquisa;
    private String filtroAtual = "Todos";

    private VBox conteudoPrincipal;
    private VBox cabecalhoCatalogo;
    private VBox secaoCatalogo;
    private Label tituloCatalogo;
    private HBox filtros;

private final ListaFavoritos listaFavoritos = new ListaFavoritos();

private void mostrarFavoritos() {
    Label titulo = new Label("★ Meus favoritos");
    titulo.setStyle(
        "-fx-font-size: 24px; -fx-font-weight: bold; " +
        "-fx-text-fill: white;"
    );

    GridPane grade = new GridPane();
    grade.setHgap(12);
    grade.setVgap(18);

    List<Conteudo> favoritos = listaFavoritos.listar();

    if (favoritos.isEmpty()) {
        Label vazio = new Label(
            "Você ainda não tem favoritos.\n" +
            "Volte ao catálogo e escolha suas histórias."
        );
        vazio.setWrapText(true);
        vazio.setStyle("-fx-text-fill: #c3bacf;");
        grade.add(vazio, 0, 0);
    } else {
        int coluna = 0;
        int linha = 0;

        for (Conteudo item : favoritos) {
            grade.add(criarCartao(item), coluna, linha);

            coluna++;

            if (coluna == 2) {
                coluna = 0;
                linha++;
            }
        }
    }

    Button voltar = new Button("← Voltar ao perfil");
    estilizarBotaoSecundario(voltar);
    voltar.setOnAction(e -> {
        Stage stage = (Stage) conteudoPrincipal.getScene()
                .getWindow();
        mostrarPerfil(stage);
    });

    VBox tela = new VBox(18, titulo, grade, voltar);
    tela.setStyle("-fx-background-color: #100e15;");

    conteudoPrincipal.getChildren().setAll(tela);
}

    @Override
    public void start(Stage stage) {
        carregarCatalogo();

        Label logo = new Label("CINEFLOW");
        logo.setStyle(
            "-fx-font-size: 27px; -fx-font-weight: bold; " +
            "-fx-text-fill: #b99aff;"
        );

        Label subtitulo = new Label("SEU UNIVERSO DO ENTRETENIMENTO");
        subtitulo.setStyle("-fx-font-size: 9px; -fx-text-fill: #aaa2bd;");

        VBox marca = new VBox(3, logo, subtitulo);

        Label tituloPrincipal = new Label("Descubra histórias.");
        tituloPrincipal.setStyle(
            "-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: white;"
        );

        Label descricao = new Label(
            "Filmes e séries para sua próxima sessão."
        );
        descricao.setWrapText(true);
        descricao.setStyle("-fx-text-fill: #aaa5b5;");

        VBox destaque = new VBox(12);
        destaque.setPadding(new Insets(22));
        destaque.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, " +
            "#382354, #17121f); -fx-background-radius: 18;"
        );

        Label selo = new Label("✦  SUA PRÓXIMA SESSÃO");
        selo.setStyle(
            "-fx-text-fill: #d5bdff; -fx-font-size: 10px; " +
            "-fx-font-weight: bold;"
        );

        Label chamada = new Label("Grandes histórias.\nUma só tela.");
        chamada.setStyle(
            "-fx-font-size: 23px; -fx-font-weight: bold; " +
            "-fx-text-fill: white;"
        );

        Label textoDestaque = new Label(
            "Explore o catálogo e encontre seu próximo favorito."
        );
        textoDestaque.setWrapText(true);
        textoDestaque.setStyle("-fx-text-fill: #d0c5df;");

        destaque.getChildren().addAll(selo, chamada, textoDestaque);

        campoPesquisa = new TextField();
        campoPesquisa.setPromptText("⌕  Buscar filme ou série...");
        campoPesquisa.setStyle(
            "-fx-background-color: #211d2a; -fx-text-fill: white; " +
            "-fx-prompt-text-fill: #928a9f; -fx-background-radius: 10; " +
            "-fx-padding: 13;"
        );
        campoPesquisa.textProperty().addListener(
            (obs, antigo, novo) -> atualizarCatalogo()
        );

        tituloCatalogo = new Label("Explore o catálogo");
        tituloCatalogo.setStyle(
            "-fx-font-size: 19px; -fx-font-weight: bold; " +
            "-fx-text-fill: white;"
        );

        filtros = new HBox(8);
        filtros.setAlignment(Pos.CENTER_LEFT);
        filtros.getChildren().addAll(
            criarFiltro("Todos"),
            criarFiltro("Filmes"),
            criarFiltro("Séries")
        );

        gradeCatalogo = new GridPane();
        gradeCatalogo.setHgap(12);
        gradeCatalogo.setVgap(18);

        secaoCatalogo = new VBox(14, tituloCatalogo, filtros, gradeCatalogo);

        cabecalhoCatalogo = new VBox(
            22, marca, tituloPrincipal, descricao,
            destaque, campoPesquisa, secaoCatalogo
        );

        conteudoPrincipal = new VBox(cabecalhoCatalogo);
        conteudoPrincipal.setPadding(new Insets(22));
        conteudoPrincipal.setStyle("-fx-background-color: #100e15;");

        ScrollPane rolagem = new ScrollPane(conteudoPrincipal);
        rolagem.setFitToWidth(true);
        rolagem.setStyle(
            "-fx-background: #100e15; -fx-background-color: #100e15;"
        );
        VBox.setVgrow(rolagem, Priority.ALWAYS);

        Button inicio = criarBotaoNavegacao("⌂ Início");
        Button explorar = criarBotaoNavegacao("⌕ Explorar");
        Button minhaLista = criarBotaoNavegacao("♡ Minha lista");
        Button perfil = criarBotaoNavegacao("☻ Perfil");

        inicio.setOnAction(e -> mostrarCatalogo());
        explorar.setOnAction(e -> mostrarCatalogo());
        minhaLista.setOnAction(e -> mostrarMinhaLista());
        perfil.setOnAction(e -> mostrarPerfil(stage));

        HBox navegacao = new HBox(
            12, inicio, explorar, minhaLista, perfil
        );
        navegacao.setAlignment(Pos.CENTER);
        navegacao.setPadding(new Insets(14, 5, 14, 5));
        navegacao.setStyle("-fx-background-color: #1c1824;");

        VBox raiz = new VBox(rolagem, navegacao);
        raiz.setPrefSize(390, 740);
        raiz.setStyle("-fx-background-color: #100e15;");

        atualizarCatalogo();

        cenaCatalogo = new Scene(raiz, 390, 740);

        stage.setTitle("CineFlow | Streaming");
        stage.setScene(criarTelaLogin(stage));
        stage.setResizable(false);
        stage.show();
    }

    private Scene criarTelaLogin(Stage stage) {
        Label logo = new Label("CINEFLOW");
        logo.setStyle(
            "-fx-font-size: 30px; -fx-font-weight: bold; " +
            "-fx-text-fill: #A855F7;"
        );

        Label subtitulo = new Label("Seu universo de entretenimento");
        subtitulo.setStyle("-fx-text-fill: #BBBBBB;");

        TextField campoEmail = new TextField();
        campoEmail.setPromptText("Seu e-mail");
        campoEmail.setMaxWidth(280);

        PasswordField campoSenha = new PasswordField();
        campoSenha.setPromptText("Sua senha");
        campoSenha.setMaxWidth(280);

        Label mensagem = new Label();
        mensagem.setWrapText(true);
        mensagem.setStyle("-fx-text-fill: #FF6B6B;");

        Button botaoEntrar = new Button("Entrar");
        botaoEntrar.setMaxWidth(280);
        botaoEntrar.setStyle(
            "-fx-background-color: #9333EA; -fx-text-fill: white; " +
            "-fx-font-weight: bold;"
        );

        botaoEntrar.setOnAction(event -> {
            String email = campoEmail.getText();
            String senha = campoSenha.getText();

            if (email == null || email.isBlank()
                    || senha == null || senha.isBlank()) {
                mensagem.setText("Preencha o e-mail e a senha.");
                return;
            }

            mensagem.setText("Verificando sua conta...");
            botaoEntrar.setDisable(true);

            Task<Usuario> tarefaLogin = new Task<>() {
                @Override
                protected Usuario call() {
                    return usuarioService.login(email, senha);
                }
            };

            tarefaLogin.setOnSucceeded(e -> {
                botaoEntrar.setDisable(false);
                Usuario usuario = tarefaLogin.getValue();

                if (usuario != null) {
                    usuarioLogado = usuario;
                    stage.setScene(cenaCatalogo);
                } else {
                    mensagem.setText("E-mail ou senha incorretos.");
                }
            });

            tarefaLogin.setOnFailed(e -> {
                botaoEntrar.setDisable(false);
                mensagem.setText("Erro ao entrar. Confira o terminal.");
                tarefaLogin.getException().printStackTrace();
            });

            Thread thread = new Thread(tarefaLogin);
            thread.setDaemon(true);
            thread.setName("cineflow-login");
            thread.start();
        });

        Button botaoCadastro = new Button("Criar conta");
        botaoCadastro.setMaxWidth(280);
        botaoCadastro.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: #C084FC;"
        );
        botaoCadastro.setOnAction(
            event -> stage.setScene(criarTelaCadastro(stage))
        );

        VBox layout = new VBox(
            18, logo, subtitulo, campoEmail, campoSenha,
            botaoEntrar, botaoCadastro, mensagem
        );
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #121018;");

        return new Scene(layout, 390, 740);
    }

    private Scene criarTelaCadastro(Stage stage) {
        Label titulo = new Label("Criar sua conta");
        titulo.setStyle(
            "-fx-font-size: 25px; -fx-font-weight: bold; " +
            "-fx-text-fill: #C084FC;"
        );

        Label subtitulo = new Label("Entre para o universo CineFlow");
        subtitulo.setStyle("-fx-text-fill: #BBBBBB;");

        TextField campoNome = new TextField();
        campoNome.setPromptText("Seu nome");
        campoNome.setMaxWidth(280);

        TextField campoEmail = new TextField();
        campoEmail.setPromptText("Seu e-mail");
        campoEmail.setMaxWidth(280);

        PasswordField campoSenha = new PasswordField();
        campoSenha.setPromptText("Crie uma senha (mínimo 8 caracteres)");
        campoSenha.setMaxWidth(280);

        PasswordField campoConfirmarSenha = new PasswordField();
        campoConfirmarSenha.setPromptText("Confirme sua senha");
        campoConfirmarSenha.setMaxWidth(280);

        Label mensagem = new Label();
        mensagem.setWrapText(true);
        mensagem.setStyle("-fx-text-fill: #FF6B6B;");

        Button botaoCadastrar = new Button("Cadastrar");
        botaoCadastrar.setMaxWidth(280);
        botaoCadastrar.setStyle(
            "-fx-background-color: #9333EA; -fx-text-fill: white; " +
            "-fx-font-weight: bold;"
        );

        botaoCadastrar.setOnAction(event -> {
            String resultado = usuarioService.cadastrar(
                campoNome.getText(),
                campoEmail.getText(),
                campoSenha.getText(),
                campoConfirmarSenha.getText()
            );

            if (resultado == null) {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("CineFlow");
                alerta.setHeaderText("Conta criada!");
                alerta.setContentText(
                    "Seu cadastro foi realizado. Agora você pode entrar."
                );
                alerta.showAndWait();
                stage.setScene(criarTelaLogin(stage));
            } else {
                mensagem.setText(resultado);
            }
        });

        Button botaoVoltar = new Button("Já tenho uma conta");
        botaoVoltar.setMaxWidth(280);
        botaoVoltar.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: #C084FC;"
        );
        botaoVoltar.setOnAction(
            event -> stage.setScene(criarTelaLogin(stage))
        );

        VBox layout = new VBox(
            16, titulo, subtitulo, campoNome, campoEmail,
            campoSenha, campoConfirmarSenha, botaoCadastrar,
            botaoVoltar, mensagem
        );
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: #121018;");

        return new Scene(layout, 390, 740);
    }

    private Button criarBotaoNavegacao(String texto) {
        Button botao = new Button(texto);
        botao.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: #e5def0; " +
            "-fx-font-size: 11px; -fx-padding: 5 3 5 3;"
        );
        return botao;
    }

    private void carregarCatalogo() {
        servico = CatalogoInicial.criar();
    }

    private Button criarFiltro(String nome) {
        Button botao = new Button(nome);
        estilizarFiltro(botao, nome.equals(filtroAtual));

        botao.setOnAction(evento -> {
            filtroAtual = nome;

            for (Node node : filtros.getChildren()) {
                if (node instanceof Button filtro) {
                    estilizarFiltro(
                        filtro, filtro.getText().equals(filtroAtual)
                    );
                }
            }

            atualizarCatalogo();
        });

        return botao;
    }

    private void estilizarFiltro(Button botao, boolean selecionado) {
        if (selecionado) {
            botao.setStyle(
                "-fx-background-color: #a78bfa; -fx-text-fill: #17121f; " +
                "-fx-font-weight: bold; -fx-background-radius: 20; " +
                "-fx-padding: 8 15 8 15;"
            );
        } else {
            botao.setStyle(
                "-fx-background-color: #211d2a; -fx-text-fill: #ddd4eb; " +
                "-fx-background-radius: 20; -fx-padding: 8 15 8 15;"
            );
        }
    }

    private void mostrarCatalogo() {
        conteudoPrincipal.getChildren().setAll(cabecalhoCatalogo);
        atualizarCatalogo();
    }

    private void mostrarMinhaLista() {
        Label titulo = new Label("♡ Minha lista");
        titulo.setStyle(
            "-fx-font-size: 24px; -fx-font-weight: bold; " +
            "-fx-text-fill: white;"
        );

        Label descricao = new Label(
            "Seus filmes e séries guardados para depois."
        );
        descricao.setWrapText(true);
        descricao.setStyle("-fx-text-fill: #aaa5b5;");

        GridPane grade = new GridPane();
        grade.setHgap(12);
        grade.setVgap(18);

        List<Conteudo> itens = filaAssistirDepois.listar();

        if (itens.isEmpty()) {
            Label vazio = new Label(
                "Sua lista está vazia.\nAdicione conteúdos pelo catálogo!"
            );
            vazio.setWrapText(true);
            vazio.setStyle("-fx-text-fill: #c3bacf; -fx-font-size: 14px;");
            grade.add(vazio, 0, 0);
        } else {
            int coluna = 0;
            int linha = 0;

            for (Conteudo item : itens) {
                grade.add(criarCartao(item), coluna, linha);
                coluna++;

                if (coluna == 2) {
                    coluna = 0;
                    linha++;
                }
            }
        }

        Button voltar = new Button("← Voltar ao catálogo");
        voltar.setStyle(
            "-fx-background-color: #a78bfa; -fx-text-fill: #17121f; " +
            "-fx-background-radius: 10; -fx-padding: 10 14;"
        );
        voltar.setOnAction(e -> mostrarCatalogo());

        VBox telaLista = new VBox(18, titulo, descricao, grade, voltar);
        telaLista.setStyle("-fx-background-color: #100e15;");

        conteudoPrincipal.getChildren().setAll(telaLista);
    }

    private void mostrarPerfil(Stage stage) {
        Label titulo = new Label("Meu perfil");
        titulo.setStyle(
            "-fx-font-size: 26px; -fx-font-weight: bold; " +
            "-fx-text-fill: white;"
        );

        String nome = usuarioLogado != null
            ? usuarioLogado.getNome()
            : "Usuário";

        String email = usuarioLogado != null
            ? usuarioLogado.getEmail()
            : "E-mail não disponível";

        Label avatar = new Label("☻");
        avatar.setAlignment(Pos.CENTER);
        avatar.setMinSize(90, 90);
        avatar.setStyle(
            "-fx-background-color: #382354; -fx-background-radius: 50; " +
            "-fx-text-fill: #d5bdff; -fx-font-size: 42px;"
        );

        Label nomeLabel = new Label(nome);
        nomeLabel.setStyle(
            "-fx-text-fill: white; -fx-font-size: 20px; " +
            "-fx-font-weight: bold;"
        );

        Label emailLabel = new Label(email);
        emailLabel.setWrapText(true);
        emailLabel.setStyle("-fx-text-fill: #aaa5b5;");

        Button botaoLista = new Button("♡ Minha lista");
        botaoLista.setMaxWidth(Double.MAX_VALUE);
        botaoLista.setOnAction(e -> mostrarMinhaLista());
        estilizarBotaoSecundario(botaoLista);
        Button botaoFavoritos = new Button("★ Meus favoritos");
        botaoFavoritos.setMaxWidth(Double.MAX_VALUE);
        estilizarBotaoSecundario(botaoFavoritos);
        botaoFavoritos.setOnAction(e -> mostrarFavoritos());    

        Button botaoSair = new Button("Sair da conta");
        botaoSair.setMaxWidth(Double.MAX_VALUE);
        botaoSair.setStyle(
            "-fx-background-color: #9333EA; -fx-text-fill: white; " +
            "-fx-background-radius: 10; -fx-padding: 11;"
        );
        botaoSair.setOnAction(e -> {
            usuarioLogado = null;
            stage.setScene(criarTelaLogin(stage));
        });

        VBox telaPerfil = new VBox(
    18, avatar, nomeLabel, emailLabel,
    botaoLista, botaoFavoritos, botaoSair);
        
        telaPerfil.setAlignment(Pos.TOP_CENTER);
        telaPerfil.setPadding(new Insets(25));
        telaPerfil.setStyle("-fx-background-color: #100e15;");

        conteudoPrincipal.getChildren().setAll(telaPerfil);
    }

    private void estilizarBotaoSecundario(Button botao) {
        botao.setStyle(
            "-fx-background-color: #292235; -fx-text-fill: #e8ddff; " +
            "-fx-background-radius: 10; -fx-padding: 11;"
        );
    }

    private void atualizarCatalogo() {
        if (gradeCatalogo == null || campoPesquisa == null || servico == null) {
            return;
        }

        gradeCatalogo.getChildren().clear();

        String pesquisa = campoPesquisa.getText() == null
            ? ""
            : campoPesquisa.getText().trim().toLowerCase();

        List<Conteudo> conteudos = servico.listarTodos();

        int coluna = 0;
        int linha = 0;
        int encontrados = 0;

        for (Conteudo item : conteudos) {
            String tipo = item instanceof Filme ? "Filme" : "Série";

            boolean correspondeAoFiltro =
                filtroAtual.equals("Todos")
                || (filtroAtual.equals("Filmes") && tipo.equals("Filme"))
                || (filtroAtual.equals("Séries") && tipo.equals("Série"));

            boolean correspondeAPesquisa =
                item.getTitulo().toLowerCase().contains(pesquisa);

            if (correspondeAoFiltro && correspondeAPesquisa) {
                gradeCatalogo.add(criarCartao(item), coluna, linha);

                coluna++;
                if (coluna == 2) {
                    coluna = 0;
                    linha++;
                }

                encontrados++;
            }
        }

        if (encontrados == 0) {
            Label vazio = new Label("Nenhum conteúdo encontrado.");
            vazio.setStyle("-fx-text-fill: #c3bacf;");
            gradeCatalogo.add(vazio, 0, 0);
        }
    }

    private VBox criarCartao(Conteudo item) {
        VBox cartao = new VBox(7);
        cartao.setPrefWidth(145);
        cartao.setMaxWidth(Double.MAX_VALUE);
        cartao.setStyle("-fx-cursor: hand;");

        String tipo = item instanceof Filme ? "Filme" : "Série";
        String simbolo = item instanceof Filme ? "✦" : "✧";
        String cor = item instanceof Filme ? "#30234d" : "#3c202b";

        Label poster = new Label(simbolo);
        poster.setAlignment(Pos.CENTER);
        poster.setMaxWidth(Double.MAX_VALUE);
        poster.setPrefHeight(145);
        poster.setStyle(
            "-fx-background-color: " + cor + "; " +
            "-fx-background-radius: 12; -fx-text-fill: #f4eaff; " +
            "-fx-font-size: 43px;"
        );

        Label tipoLabel = new Label(tipo.toUpperCase());
        tipoLabel.setStyle(
            "-fx-text-fill: #b99aff; -fx-font-size: 9px; " +
            "-fx-font-weight: bold;"
        );

        Label titulo = new Label(item.getTitulo());
        titulo.setWrapText(true);
        titulo.setStyle(
            "-fx-text-fill: white; -fx-font-size: 13px; " +
            "-fx-font-weight: bold;"
        );

        Label detalhes = new Label(item.getAno() + "  •  " + item.getGenero());
        detalhes.setWrapText(true);
        detalhes.setStyle("-fx-text-fill: #aaa2b5; -fx-font-size: 10px;");

        boolean jaAdicionado = filaAssistirDepois.contem(item);

        Button adicionar = new Button(
            jaAdicionado ? "✓ Na minha lista" : "♡ Assistir depois"
        );
        adicionar.setMaxWidth(Double.MAX_VALUE);
        adicionar.setStyle(
            "-fx-background-color: #292235; -fx-text-fill: #e8ddff; " +
            "-fx-background-radius: 9; -fx-padding: 8 5 8 5; " +
            "-fx-font-size: 10px;"
        );
        adicionar.setDisable(jaAdicionado);

        adicionar.setOnAction(e -> {
            boolean adicionado = filaAssistirDepois.adicionar(item);

            if (adicionado) {
                mostrarAviso(
                    "Adicionado à sua lista",
                    item.getTitulo() + " foi guardado para assistir depois."
                );
                atualizarCatalogo();
            }
        });

        cartao.getChildren().addAll(
            poster, tipoLabel, titulo, detalhes, adicionar
        );

        cartao.setOnMouseClicked(event -> {
            Node alvo = (Node) event.getTarget();

            while (alvo != null && alvo != cartao) {
                if (alvo == adicionar) {
                    return;
                }
                alvo = alvo.getParent();
            }

            abrirPlayer(item);
        });

        return cartao;
    }

    private void abrirPlayer(Conteudo item) {
        Label titulo = new Label(item.getTitulo());
        titulo.setWrapText(true);
        titulo.setStyle(
            "-fx-text-fill: white; -fx-font-size: 24px; " +
            "-fx-font-weight: bold;"
        );

        Label tipo = new Label(
            (item instanceof Filme ? "Filme" : "Série")
            + " • " + item.getAno()
        );
        tipo.setStyle("-fx-text-fill: #b99aff;");

        Label telaVideo = new Label("▶");
        telaVideo.setAlignment(Pos.CENTER);
        telaVideo.setMaxWidth(Double.MAX_VALUE);
        telaVideo.setPrefHeight(230);
        telaVideo.setStyle(
            "-fx-background-color: #21182f; -fx-background-radius: 14; " +
            "-fx-text-fill: #c084fc; -fx-font-size: 55px;"
        );

        Label mensagem = new Label(
            "Área de reprodução do conteúdo"
        );
        mensagem.setWrapText(true);
        mensagem.setStyle("-fx-text-fill: #aaa5b5;");

        Button voltar = new Button("← Voltar ao catálogo");
        voltar.setStyle(
            "-fx-background-color: #a78bfa; -fx-text-fill: #17121f; " +
            "-fx-background-radius: 10; -fx-padding: 10 14;"
        );
        voltar.setOnAction(e -> mostrarCatalogo());

        VBox tela = new VBox(20, titulo, tipo, telaVideo, mensagem, voltar);
        tela.setAlignment(Pos.TOP_CENTER);
        tela.setPadding(new Insets(28, 22, 22, 22));
        tela.setStyle("-fx-background-color: #100e15;");

        conteudoPrincipal.getChildren().setAll(tela);
    }

    private void mostrarAviso(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("CineFlow");
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
