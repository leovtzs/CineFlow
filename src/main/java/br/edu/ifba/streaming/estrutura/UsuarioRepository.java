
package br.edu.ifba.streaming.estrutura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.edu.ifba.streaming.model.Usuario;
import br.edu.ifba.streaming.usuario.ConexaoBanco;

public class UsuarioRepository {

    public boolean cadastrar(String nome, String email, String senhaHash) {
        String sql = """
                INSERT INTO usuarios (nome, email, senha_hash)
                VALUES (?, ?, ?)
                """;

        System.out.println("Iniciando cadastro de usuário...");

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, nome);
            comando.setString(2, email);
            comando.setString(3, senhaHash);

            comando.executeUpdate();

            System.out.println("Usuário cadastrado com sucesso.");
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário:");
            e.printStackTrace();
            return false;
        }
    }

    public Usuario buscarPorEmail(String email) {
        String sql = """
                SELECT id, nome, email, senha_hash
                FROM usuarios
                WHERE LOWER(email) = LOWER(?)
                """;

        System.out.println("Abrindo conexão para buscar usuário...");

        try (Connection conexao = ConexaoBanco.conectar()) {

            System.out.println("Conexão obtida.");

            try (PreparedStatement comando = conexao.prepareStatement(sql)) {
                comando.setString(1, email);

                System.out.println("Executando consulta...");

                try (ResultSet resultado = comando.executeQuery()) {
                    if (resultado.next()) {
                        System.out.println("[BANCO] Usuário encontrado.");

                        return new Usuario(
                                resultado.getInt("id"),
                                resultado.getString("nome"),
                                resultado.getString("email"),
                                resultado.getString("senha_hash")
                        );
                    }
                }
            }

            System.out.println("Nenhum usuário encontrado.");
            return null;

     
        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public boolean atualizarNome(int id, String novoNome) {
    String sql = "UPDATE usuarios SET nome = ? WHERE id = ?";

    try (Connection conexao = ConexaoBanco.conectar();
         PreparedStatement comando = conexao.prepareStatement(sql)) {

        comando.setString(1, novoNome);
        comando.setInt(2, id);

        return comando.executeUpdate() > 0;

    } catch (SQLException e) {
        System.err.println("Erro ao atualizar nome: " + e.getMessage());
        return false;
    }
}

public boolean atualizarSenha(int id, String novaSenhaHash) {
    String sql = "UPDATE usuarios SET senha_hash = ? WHERE id = ?";

    try (Connection conexao = ConexaoBanco.conectar();
         PreparedStatement comando = conexao.prepareStatement(sql)) {

        comando.setString(1, novaSenhaHash);
        comando.setInt(2, id);

        return comando.executeUpdate() > 0;

    } catch (SQLException e) {
        System.err.println("Erro ao atualizar senha: " + e.getMessage());
        return false;
    }
}
}


