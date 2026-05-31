package attendIt.service;

import attendIt.dao.ChamadoDAO;
import attendIt.model.Chamado;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ChamadoService {
    private final ChamadoDAO dao = new ChamadoDAO();

    public void abrirChamado(Chamado chamado) throws SQLException {
        if (chamado.getTitulo() == null || chamado.getTitulo().isBlank()) {
            throw new IllegalArgumentException("O título do chamado é obrigatório.");
        }

        if (chamado.getIdUsuario() <= 0) {
            throw new IllegalArgumentException("Usuário inválido.");
        }

        chamado.setStatus("Aberto");
        dao.inserir(chamado);
    }

    public List<Chamado> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    public Chamado buscarPorId(int id) throws SQLException {
        Chamado chamado = dao.buscarPorId(id);
        if (chamado == null) {
            throw new IllegalArgumentException("Chamado #" + id + " não encontrado.");
        }
        return chamado;
    }

    public void atualizarChamado(Chamado chamado) throws SQLException {
        if (chamado.getTitulo() == null || chamado.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Título não pode ser vazio");
        }
        dao.atualizar(chamado);
    }

    public void fecharChamado(int idChamado) throws SQLException {
        int tecnicos = dao.contarTecnicos(idChamado);
        if (tecnicos == 0) {
            throw new IllegalStateException("Não é possível fechar o chamado sem um técnico atribuído.");
        }
        Chamado chamado = buscarPorId(idChamado);
        chamado.setStatus("Fechado");
        chamado.setDtFechamento(LocalDateTime.now());
        dao.atualizar(chamado);
    }

    public void atualizarStatus(int idChamado, String novoStatus) throws SQLException {
        Chamado chamado = buscarPorId(idChamado);
        String statusAtual = chamado.getStatus();

        if ("Fechado".equals(statusAtual)) {
            throw new IllegalStateException("Chamado fechado não pode ser reaberto.");
        }
        if ("Fechado".equals(novoStatus)) {
            throw new IllegalStateException("Use o método fecharChamado para encerrar.");
        }

        chamado.setStatus(novoStatus);
        dao.atualizar(chamado);
    }

    public void excluir(int id) throws SQLException {
        dao.excluir(id);
    }
}
