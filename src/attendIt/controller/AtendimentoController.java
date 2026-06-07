package attendIt.controller;

import attendIt.model.Atendimento;
import attendIt.service.AtendimentoService;
import java.sql.SQLException;
import java.util.List;

public class AtendimentoController {

    private final AtendimentoService service = new AtendimentoService();

    public boolean atribuir(Atendimento a, StringBuilder mensagem) {
        try {
            service.atribuir(a);
            mensagem.append("Técnico atribuído ao chamado com sucesso!");
            return true;
        } catch (IllegalArgumentException e) {
            mensagem.append("Erro de validação: ").append(e.getMessage());
            return false;
        } catch (SQLException e) {
            mensagem.append("Erro ao salvar no banco: ").append(e.getMessage());
            return false;
        }
    }

    public List<Atendimento> listarTodos(StringBuilder mensagem) {
        try {
            return service.listarTodos();
        } catch (SQLException e) {
            mensagem.append("Erro ao carregar atendimentos: ").append(e.getMessage());
            return List.of();
        }
    }

    public boolean registrarResolucao(int idAtendimento, String observacao, StringBuilder mensagem) {
        try {
            service.registrarResolucao(idAtendimento, observacao);
            mensagem.append("Resolução registrada com sucesso!");
            return true;
        } catch (IllegalArgumentException e) {
            mensagem.append(e.getMessage());
            return false;
        } catch (SQLException e) {
            mensagem.append("Erro ao registrar resolução: ").append(e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id, StringBuilder mensagem) {
        try {
            service.excluir(id);
            mensagem.append("Atendimento removido.");
            return true;
        } catch (SQLException e) {
            mensagem.append("Erro ao excluir: ").append(e.getMessage());
            return false;
        }
    }
}