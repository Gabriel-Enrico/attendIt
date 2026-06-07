package attendIt.dao;

import attendIt.model.Categoria;
import java.sql.*;
import java.util.*;

public class CategoriaDAO {

    public void inserir(Categoria c) throws SQLException {
        String sql = "INSERT INTO Categoria (descricao, sla_horas) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getDescricao());
            ps.setInt(2, c.getSlaHoras());
            ps.executeUpdate();
        }
    }

    public List<Categoria> listarTodos() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM Categoria ORDER BY descricao";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public Categoria buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Categoria WHERE id_categoria = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? mapear(rs) : null;
        }
    }

    public void atualizar(Categoria c) throws SQLException {
        String sql = "UPDATE Categoria SET descricao = ?, sla_horas = ? WHERE id_categoria = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getDescricao());
            ps.setInt(2, c.getSlaHoras());
            ps.setInt(3, c.getId());
            ps.executeUpdate();
        }
    }

    public int excluir(int id) throws SQLException {
        String sql = "DELETE FROM Categoria WHERE id_categoria = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    private Categoria mapear(ResultSet rs) throws SQLException {
        Categoria c = new Categoria();
        c.setId(rs.getInt("id_categoria"));
        c.setDescricao(rs.getString("descricao"));
        c.setSlaHoras(rs.getInt("sla_horas"));
        return c;
    }
}
