package attendIt.service;

import attendIt.model.Usuario;
import attendIt.dao.UsuarioDAO;
import java.sql.SQLException;
import java.util.List;

public class UsuarioService {

    private final UsuarioDAO dao = new UsuarioDAO();

    public void cadastrar(Usuario u) throws SQLException {
        if (u.getNome() == null || u.getNome().isBlank())
            throw new IllegalArgumentException("O nome do usuário é obrigatório.");
        if (u.getEmail() == null || u.getEmail().isBlank())
            throw new IllegalArgumentException("O e-mail é obrigatório.");
        if (!u.getEmail().contains("@"))
            throw new IllegalArgumentException("E-mail inválido.");
        dao.inserir(u);
    }

    public List<Usuario> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    public void atualizar(Usuario u) throws SQLException {
        if (u.getNome() == null || u.getNome().isBlank())
            throw new IllegalArgumentException("O nome não pode ser vazio.");
        if (!u.getEmail().contains("@"))
            throw new IllegalArgumentException("E-mail inválido.");
        dao.atualizar(u);
    }

    public void excluir(int id) throws SQLException {
        int linhas = dao.excluir(id);
        if (linhas == 0)
            throw new IllegalArgumentException("Usuário #" + id + " não encontrado.");
    }
}
