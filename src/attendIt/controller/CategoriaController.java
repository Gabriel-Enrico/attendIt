package attendIt.controller;

import attendIt.model.Categoria;
import attendIt.service.CategoriaService;
import java.sql.SQLException;
import java.util.List;

public class CategoriaController {

    private final CategoriaService service = new CategoriaService();

    public boolean cadastrar(Categoria c, StringBuilder mensagem) {
        try {
            service.cadastrar(c);
            mensagem.append("Categoria cadastrada com sucesso!");
            return true;
        } catch (IllegalArgumentException e) {
            mensagem.append("Erro de validação: ").append(e.getMessage());
            return false;
        } catch (SQLException e) {
            mensagem.append("Erro ao salvar no banco: ").append(e.getMessage());
            return false;
        }
    }

    public List<Categoria> listarTodos(StringBuilder mensagem) {
        try {
            return service.listarTodos();
        } catch (SQLException e) {
            mensagem.append("Erro ao carregar categorias: ").append(e.getMessage());
            return List.of();
        }
    }

    public boolean atualizar(Categoria c, StringBuilder mensagem) {
        try {
            service.atualizar(c);
            mensagem.append("Categoria #").append(c.getId()).append(" atualizada.");
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
            mensagem.append("Categoria excluída.");
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
