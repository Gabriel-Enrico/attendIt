package attendIt.controller;

import attendIt.model.Usuario;
import attendIt.service.UsuarioService;
import java.sql.SQLException;
import java.util.List;

public class UsuarioController {

    private final UsuarioService service = new UsuarioService();

    public boolean cadastrar(Usuario u, StringBuilder mensagem) {
        try {
            service.cadastrar(u);
            mensagem.append("Usuário cadastrado com sucesso!");
            return true;
        } catch (IllegalArgumentException e) {
            mensagem.append("Erro de validação: ").append(e.getMessage());
            return false;
        } catch (SQLException e) {
            mensagem.append("Erro ao salvar no banco: ").append(e.getMessage());
            return false;
        }
    }

    public List<Usuario> listarTodos(StringBuilder mensagem) {
        try {
            return service.listarTodos();
        } catch (SQLException e) {
            mensagem.append("Erro ao carregar usuários: ").append(e.getMessage());
            return List.of();
        }
    }

    public boolean atualizar(Usuario u, StringBuilder mensagem) {
        try {
            service.atualizar(u);
            mensagem.append("Usuário #").append(u.getId()).append(" atualizado.");
            return true;
        } catch (IllegalArgumentException e) {
            mensagem.append("Erro de validação: ").append(e.getMessage());
            return false;
        } catch (SQLException e) {
            mensagem.append("Erro no banco: ").append(e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id, StringBuilder mensagem) {
        try {
            service.excluir(id);
            mensagem.append("Usuário excluído.");
            return true;
        } catch (IllegalArgumentException e) {
            mensagem.append(e.getMessage());
            return false;
        } catch (SQLException e) {
            mensagem.append("Erro ao excluir: ").append(e.getMessage());
            return false;
        }
    }
}
