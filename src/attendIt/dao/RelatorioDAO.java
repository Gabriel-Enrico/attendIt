package attendIt.dao;

import attendIt.model.RelatorioChamado;
import java.sql.*;
import java.util.*;

public class RelatorioDAO {

    private static final String SQL_BASE = """
            SELECT * FROM vw_chamados_completo
            """;

    public List<RelatorioChamado> listarTodos() throws SQLException {
        return executar(SQL_BASE + "ORDER BY dt_abertura DESC", null);
    }

    public List<RelatorioChamado> listarPorStatus(String status) throws SQLException {
        return executar(SQL_BASE + "WHERE status = ? ORDER BY dt_abertura DESC", status);
    }

    private List<RelatorioChamado> executar(String sql, String parametro) throws SQLException {
        List<RelatorioChamado> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (parametro != null) ps.setString(1, parametro);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private RelatorioChamado mapear(ResultSet rs) throws SQLException {
        RelatorioChamado r = new RelatorioChamado();
        r.setIdChamado(rs.getInt("id_chamado"));
        r.setTitulo(rs.getString("titulo"));
        r.setStatus(rs.getString("status"));
        r.setPrioridade(rs.getString("prioridade"));
        r.setSolicitante(rs.getString("solicitante"));
        r.setSetor(rs.getString("setor"));
        r.setCategoria(rs.getString("categoria"));
        r.setSlaHoras(rs.getInt("sla_horas"));
        r.setTecnico(rs.getString("tecnico_responsavel"));
        r.setNivelTecnico(rs.getString("nivel_tecnico"));
        Timestamp dtAb = rs.getTimestamp("dt_abertura");
        if (dtAb != null) r.setDtAbertura(dtAb.toLocalDateTime());
        Timestamp dtFe = rs.getTimestamp("dt_fechamento");
        if (dtFe != null) r.setDtFechamento(dtFe.toLocalDateTime());
        Timestamp dtAt = rs.getTimestamp("dt_atribuicao");
        if (dtAt != null) r.setDtAtribuicao(dtAt.toLocalDateTime());
        Timestamp dtRe = rs.getTimestamp("dt_resolucao");
        if (dtRe != null) r.setDtResolucao(dtRe.toLocalDateTime());
        r.setObservacao(rs.getString("observacao_resolucao"));
        return r;
    }
}