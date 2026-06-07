package attendIt.dao;

import attendIt.model.Atendimento;
import java.sql.*;
import java.util.*;

public class AtendimentoDAO {

    public void inserir(Atendimento a) throws SQLException {
        String sql = """
                INSERT INTO Atendimento (id_chamado, id_tecnico, observacao)
                VALUES (?, ?, ?)
                """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, a.getIdChamado());
            ps.setInt(2, a.getIdTecnico());
            ps.setString(3, a.getObservacao());
            ps.executeUpdate();
        }
    }

    public List<Atendimento> listarTodos() throws SQLException {
        List<Atendimento> lista = new ArrayList<>();
        String sql = """
                SELECT a.*, t.nome AS nome_tecnico, c.titulo AS titulo_chamado
                FROM Atendimento a
                JOIN Tecnico t ON t.id_tecnico = a.id_tecnico
                JOIN Chamado c ON c.id_chamado = a.id_chamado
                ORDER BY a.dt_atribuicao DESC
                """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public void registrarResolucao(int idAtendimento, String observacao) throws SQLException {
        String sql = """
                UPDATE Atendimento
                SET dt_resolucao = CURRENT_TIMESTAMP, observacao = ?
                WHERE id_atendimento = ?
                """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, observacao);
            ps.setInt(2, idAtendimento);
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM Atendimento WHERE id_atendimento = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Atendimento mapear(ResultSet rs) throws SQLException {
        Atendimento a = new Atendimento();
        a.setId(rs.getInt("id_atendimento"));
        a.setIdChamado(rs.getInt("id_chamado"));
        a.setIdTecnico(rs.getInt("id_tecnico"));
        a.setNomeTecnico(rs.getString("nome_tecnico"));
        a.setTituloChamado(rs.getString("titulo_chamado"));
        a.setObservacao(rs.getString("observacao"));
        Timestamp dtAtrib = rs.getTimestamp("dt_atribuicao");
        if (dtAtrib != null) a.setDtAtribuicao(dtAtrib.toLocalDateTime());
        Timestamp dtResol = rs.getTimestamp("dt_resolucao");
        if (dtResol != null) a.setDtResolucao(dtResol.toLocalDateTime());
        return a;
    }

    public Atendimento buscarPorId(int id) throws SQLException {
        String sql = """
            SELECT a.*, t.nome AS nome_tecnico, c.titulo AS titulo_chamado
            FROM Atendimento a
            JOIN Tecnico t ON t.id_tecnico = a.id_tecnico
            JOIN Chamado c ON c.id_chamado = a.id_chamado
            WHERE a.id_atendimento = ?
            """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? mapear(rs) : null;
        }
    }
}