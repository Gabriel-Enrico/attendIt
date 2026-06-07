package attendIt.dao;

import attendIt.model.Tecnico;
import java.sql.*;
import java.util.*;

public class TecnicoDAO {

    public void inserir(Tecnico t) throws SQLException {
        String sql = "INSERT INTO Tecnico (nome, especialidade, nivel, disponivel) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getNome());
            ps.setString(2, t.getEspecialidade());
            ps.setString(3, t.getNivel());
            ps.setBoolean(4, t.isDisponivel());
            ps.executeUpdate();
        }
    }

    public List<Tecnico> listarTodos() throws SQLException {
        List<Tecnico> lista = new ArrayList<>();
        String sql = "SELECT * FROM Tecnico ORDER BY nome";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Tecnico> listarDisponiveis() throws SQLException {
        List<Tecnico> lista = new ArrayList<>();
        String sql = "SELECT * FROM Tecnico WHERE disponivel = TRUE ORDER BY nome";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public Tecnico buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Tecnico WHERE id_tecnico = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? mapear(rs) : null;
        }
    }

    public void atualizar(Tecnico t) throws SQLException {
        String sql = "UPDATE Tecnico SET nome = ?, especialidade = ?, nivel = ?, disponivel = ? WHERE id_tecnico = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getNome());
            ps.setString(2, t.getEspecialidade());
            ps.setString(3, t.getNivel());
            ps.setBoolean(4, t.isDisponivel());
            ps.setInt(5, t.getId());
            ps.executeUpdate();
        }
    }

    public int excluir(int id) throws SQLException {
        String sql = "DELETE FROM Tecnico WHERE id_tecnico = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    private Tecnico mapear(ResultSet rs) throws SQLException {
        Tecnico t = new Tecnico();
        t.setId(rs.getInt("id_tecnico"));
        t.setNome(rs.getString("nome"));
        t.setEspecialidade(rs.getString("especialidade"));
        t.setNivel(rs.getString("nivel"));
        t.setDisponivel(rs.getBoolean("disponivel"));
        return t;
    }
}
