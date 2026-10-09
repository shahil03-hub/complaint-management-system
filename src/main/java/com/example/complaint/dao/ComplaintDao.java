package com.example.complaint.dao;

import com.example.complaint.model.Complaint;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Plain JDBC data access (no JPA / JdbcTemplate). */
@Repository
public class ComplaintDao {

    private final DataSource dataSource;

    public ComplaintDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int save(Complaint c) throws SQLException {
        String sql = "INSERT INTO complaints (name, email, category, subject, description) "
                   + "VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getCategory());
            ps.setString(4, c.getSubject());
            ps.setString(5, c.getDescription());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public List<Complaint> findAll(String status) throws SQLException {
        boolean filter = status != null && !status.isBlank();
        String sql = "SELECT * FROM complaints "
                   + (filter ? "WHERE status = ? " : "")
                   + "ORDER BY created_at DESC";
        List<Complaint> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (filter) ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public Complaint findById(int id) throws SQLException {
        String sql = "SELECT * FROM complaints WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public boolean updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE complaints SET status = ? WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM complaints WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Complaint map(ResultSet rs) throws SQLException {
        Complaint c = new Complaint();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setEmail(rs.getString("email"));
        c.setCategory(rs.getString("category"));
        c.setSubject(rs.getString("subject"));
        c.setDescription(rs.getString("description"));
        c.setStatus(rs.getString("status"));
        c.setCreatedAt(rs.getTimestamp("created_at").toString());
        return c;
    }
}
