package attendIt.dao;

import attendIt.model.Chamado;
import java.sql.*;
import java.util.*;

public class ChamadoDAO {
    public void inserir(Chamado c) throws SQLException {
        String sql = """
                INSERT INTO Chamado (id_usuario, id_categoria, titulo, descricao, prioridade)
                VALUES (?, ?, ?, ?, ?) 
                """;
        try (Connection connection = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, c.getIdUsuario());
            preparedStatement.setInt(2, c.getIdCategoria());
            preparedStatement.setString(3, c.getTitulo());
            preparedStatement.setString(4, c.getDescricao());
            preparedStatement.setString(5, c.getPrioridade());
            preparedStatement.execute();
        }
    }

    public List<Chamado> listarTodos() throws SQLException {
        List<Chamado> lista = new ArrayList<>();
        String sql = "SELECT * FROM Chamado ORDER BY dt_abertura DESC";
        try (Connection connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public Chamado buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Chamado WHERE id_chamado = ?";
        try (Connection connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? mapear(rs) : null;
        }
    }

    public void atualizar(Chamado chamado) throws SQLException {
        String sql = """
                UPDATE Chamado
                    SET titulo = ?, descricao = ?, status = ?, prioridade = ?,
                    id_categoria = ?, dt_fechamento = ?
                WHERE id_chamado = ?;
                """;
        try (Connection connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, chamado.getTitulo());
            ps.setString(2, chamado.getDescricao());
            ps.setString(3, chamado.getStatus());
            ps.setString(4, chamado.getPrioridade());
            ps.setInt(5, chamado.getIdCategoria());
            ps.setObject(6, chamado.getDtFechamento());
            ps.setInt(7,  chamado.getId());
            ps.executeUpdate();
        }
    }

    public int excluir(int id) throws SQLException {
        String sql = "DELETE FROM Chamado WHERE id_chamado = ?";
        try (Connection connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    public int contarTecnicos(int idChamado) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Atendimento WHERE id_chamado = ?";
        try (Connection connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idChamado);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private Chamado mapear(ResultSet rs) throws SQLException {
        Chamado chamado = new Chamado();
        chamado.setId(rs.getInt("id_chamado"));
        chamado.setIdUsuario(rs.getInt("id_usuario"));
        chamado.setIdCategoria(rs.getInt("id_categoria"));
        chamado.setTitulo(rs.getString("titulo"));
        chamado.setDescricao(rs.getString("descricao"));
        chamado.setStatus(rs.getString("status"));
        chamado.setPrioridade(rs.getString("prioridade"));
        Timestamp ts = rs.getTimestamp("dt_abertura");
        if (ts != null) {
            chamado.setDtAbertura(ts.toLocalDateTime());
        }
        return chamado;
    }
}
