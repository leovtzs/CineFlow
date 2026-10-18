
package br.edu.ifba.streaming.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import br.edu.ifba.streaming.model.Usuario;
import br.edu.ifba.streaming.repository.UsuarioRepository;
import br.edu.ifba.streaming.usuario.ConexaoBanco;

public class UsuarioService {

    private final UsuarioRepository repositorio = new UsuarioRepository();

    public UsuarioService() {
        System.out.println("[DIAGNÓSTICO] Inicializando UsuarioService...");
        ConexaoBanco.criarTabela();
        System.out.println("[DIAGNÓSTICO] UsuarioService inicializado.");
    }

    public String cadastrar(String nome, String email,
                            String senha, String confirmarSenha) {

        System.out.println("[CADASTRO] Iniciando cadastro...");

        if (nome == null || nome.isBlank()
                || email == null || email.isBlank()
                || senha == null || senha.isBlank()
                || confirmarSenha == null || confirmarSenha.isBlank()) {
            return "Preencha todos os campos.";
        }

        nome = nome.trim();
        email = email.trim();

        if (!email.matches("^[\\w.+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            return "Digite um e-mail válido.";
        }

        if (senha.length() < 8) {
            return "A senha deve ter pelo menos 8 caracteres.";
        }

        if (!senha.equals(confirmarSenha)) {
            return "As senhas não coincidem.";
        }

        try {
            System.out.println("[CADASTRO] Verificando e-mail...");
            if (repositorio.buscarPorEmail(email) != null) {
                return "Este e-mail já está cadastrado.";
            }

            System.out.println("[CADASTRO] Gerando hash da senha...");
            String senhaHash = gerarHash(senha);

            if (senhaHash == null) {
                return "Não foi possível processar a senha.";
            }

            System.out.println("[CADASTRO] Salvando usuário...");
            if (repositorio.cadastrar(nome, email, senhaHash)) {
                System.out.println("[CADASTRO] Cadastro concluído.");
                return null;
            }

            return "Não foi possível cadastrar o usuário.";

        } catch (Exception e) {
            System.err.println("[CADASTRO] Erro durante o cadastro:");
            e.printStackTrace();
            return "Erro ao cadastrar. Verifique o terminal.";
        }
    }

    public Usuario login(String email, String senha) {
        System.out.println("[LOGIN] Clique recebido pelo serviço.");

        if (email == null || email.isBlank()
                || senha == null || senha.isBlank()) {
            System.out.println("[LOGIN] E-mail ou senha vazios.");
            return null;
        }

        try {
            System.out.println("[LOGIN] Consultando usuário no banco...");
            Usuario usuario = repositorio.buscarPorEmail(email.trim());

            System.out.println("[LOGIN] Consulta ao banco concluída.");

            if (usuario == null) {
                System.out.println("[LOGIN] Usuário não encontrado.");
                return null;
            }

            System.out.println("[LOGIN] Verificando senha...");
            String hashArmazenado = usuario.getSenhaHash();

            if (hashArmazenado == null || !hashArmazenado.contains(":")) {
                System.out.println("[LOGIN] Hash armazenado inválido.");
                return null;
            }

            String hashInformado = gerarHashComSal(
                    senha, obterSal(hashArmazenado)
            );

            if (hashInformado != null
                    && hashInformado.equals(
                            hashArmazenado.substring(
                                    hashArmazenado.indexOf(":") + 1
                            )
                    )) {
                System.out.println("[LOGIN] Login validado.");
                return usuario;
            }

            System.out.println("[LOGIN] Senha incorreta.");
            return null;

        } catch (Exception e) {
            System.err.println("[LOGIN] Erro durante o login:");
            e.printStackTrace();
            return null;
        }
    }

    private String gerarHash(String senha) {
        try {
            byte[] sal = new byte[16];
            new SecureRandom().nextBytes(sal);

            String salTexto = Base64.getEncoder().encodeToString(sal);
            String hash = gerarHashComSal(senha, salTexto);

            if (hash == null) {
                return null;
            }

            return salTexto + ":" + hash;

        } catch (Exception e) {
            System.err.println("[HASH] Erro ao gerar hash:");
            e.printStackTrace();
            return null;
        }
    }

    private String obterSal(String senhaHash) {
        if (senhaHash == null || !senhaHash.contains(":")) {
            return "";
        }

        return senhaHash.substring(0, senhaHash.indexOf(":"));
    }

    private String gerarHashComSal(String senha, String salTexto) {
        try {
            byte[] sal = Base64.getDecoder().decode(salTexto);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(sal);

            byte[] hash = digest.digest(
                    senha.getBytes(StandardCharsets.UTF_8)
            );

            return Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            System.err.println("[HASH] Erro ao calcular hash:");
            e.printStackTrace();
            return null;
        }
    }
}
