package attendIt.controller;

import attendIt.model.Chamado;
import attendIt.service.ChamadoService;
import java.sql.SQLException;
import java.util.List;

public class ChamadoController {

    private final ChamadoService chamadoService = new ChamadoService();

    public boolean abrirChamado(Chamado chamado, StringBuilder mensagem) {
        try {
            chamadoService.abrirChamado(chamado);
            mensagem.append("Chamado aberto com sucesso!");
            return true;
        } catch (IllegalArgumentException e) {
            mensagem.append("Erro de validação: ").append(e.getMessage());
            return false;
        } catch (SQLException e) {
            mensagem.append("Erro ao salvar no banco: ").append(e.getMessage());
            return false;
        }
    }

    public List<Chamado> listarTodos(StringBuilder mensagem) {
        try {
            return chamadoService.listarTodos();
        } catch (SQLException e) {
            mensagem.append("Erro ao carregar chamados: ").append(e.getMessage());
            return List.of();
        }
    }

    public boolean fecharChamado(int id, StringBuilder mensagem) {
        try {
            chamadoService.fecharChamado(id);
            mensagem.append("Chamado #").append(id).append(" fechado com sucesso!");
            return true;
        } catch (IllegalArgumentException e) {
            mensagem.append(e.getMessage());
            return false;
        } catch (SQLException e) {
            mensagem.append("Erro ao fechar chamados: ").append(e.getMessage());
            return false;
        }
    }

    public boolean atualizarChamado(Chamado c, StringBuilder mensagem) {
        try {
            chamadoService.atualizarChamado(c);
            mensagem.append("Chamado #").append(c.getId()).append(" atualizado.");
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
            chamadoService.excluir(id);
            mensagem.append("Chamado excluído");
            return true;
        } catch (SQLException e) {
            mensagem.append("Erro ao excluir: ").append(e.getMessage());
            return false;
        }
    }
}
