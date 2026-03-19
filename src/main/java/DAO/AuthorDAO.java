package DAO;

import models.Author;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {

    /* ================= INSERT ================= */
    public int insertAndReturnId(Author author) {
        String sql = """
            INSERT INTO author
            (authorName, authorDetail)
            VALUES (?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, author.getAuthorName());
            ps.setString(2, author.getAuthorDetail());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public boolean addAuthor(Author author) {
        String sql = """
            INSERT INTO author
            (authorName, authorDetail)
            VALUES (?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, author.getAuthorName());
            ps.setString(2, author.getAuthorDetail());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* ================= GET BY ID ================= */
    public Author getById(int id) {
        String sql = "SELECT * FROM author WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return mapAuthor(rs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /* ================= GET ALL ================= */
    public List<Author> findAll() {
        List<Author> list = new ArrayList<>();
        String sql = "SELECT * FROM author";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapAuthor(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /* ================= GET AUTHORS BY EBOOK ================= */
    public List<Author> getByEbookID(int ebookID) {
        List<Author> list = new ArrayList<>();

        String sql = """
            SELECT a.*
            FROM ebookauthor ea
            JOIN author a ON ea.authorID = a.id
            WHERE ea.ebookID = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ebookID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapAuthor(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /* ================= UPDATE ================= */
    public boolean update(Author author) {
        String sql = """
            UPDATE author
            SET authorName = ?,
                authorDetail = ?
            WHERE id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, author.getAuthorName());
            ps.setString(2, author.getAuthorDetail());
            ps.setInt(3, author.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* ================= DELETE ================= */
    public boolean delete(int id) {
        String sql = "DELETE FROM author WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* ================= HELPER ================= */
    private Author mapAuthor(ResultSet rs) throws SQLException {
        return new Author(
                rs.getInt("id"),
                rs.getString("authorName"),
                rs.getString("authorDetail")
        );
    }
}
