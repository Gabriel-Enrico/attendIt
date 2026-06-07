package attendIt.service;

import attendIt.dao.AtendimentoDAO;
import attendIt.dao.ChamadoDAO;
import attendIt.model.Atendimento;
import attendIt.model.Chamado;
import java.sql.SQLException;
import java.util.List;

public class AtendimentoService {

    private final AtendimentoDAO dao = new AtendimentoDAO();
    private final ChamadoDAO chamadoDAO = new ChamadoDAO();

    public void atribuir(Atendimento a) throws SQLException {
        if (a.getIdChamado() <= 0)
            throw new IllegalArgumentException("Selecione um chamado válido.");
        if (a.getIdTecnico() <= 0)
            throw new IllegalArgumentException("Selecione um técnico válido.");

        // Atualiza status do chamado para Em_Atendimento
        Chamado chamado = chamadoDAO.buscarPorId(a.getIdChamado());
        if (chamado != null && "Aberto".equals(chamado.getStatus())) {
            chamado.setStatus("Em_Atendimento");
            chamadoDAO.atualizar(chamado);
        }

        dao.inserir(a);
    }

    public List<Atendimento> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    public void registrarResolucao(int idAtendimento, String observacao) throws SQLException {
        if (observacao == null || observacao.isBlank())
            throw new IllegalArgumentException("Informe uma observação de resolução.");
        dao.registrarResolucao(idAtendimento, observacao);

        Atendimento a = dao.buscarPorId(idAtendimento);
        if (a != null) {
            Chamado chamado = chamadoDAO.buscarPorId(a.getIdChamado());
            if (chamado != null) {
                chamado.setStatus("Resolvido");
                chamadoDAO.atualizar(chamado);
            }
        }
    }

    public void excluir(int id) throws SQLException {
        dao.excluir(id);
    }
}