package DAO;

import models.Author;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EbookAuthorDAO {

    public void insert(int ebookId, int authorId) {
        String sql = "INSERT INTO ebookauthor (ebookID, authorID) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ebookId);
            ps.setInt(2, authorId);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer> getAuthorIdsByEbook(int ebookId) {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT authorID FROM ebookauthor WHERE ebookID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getInt("authorID"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Author> getAuthorsByEbookID(int ebookID) {
        List<Author> authors = new ArrayList<>();

        String sql = """
        SELECT a.id, a.authorName, a.authorDetail
        FROM ebookauthor ea
        JOIN author a ON ea.authorID = a.id
        WHERE ea.ebookID = ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ebookID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                Author author = new Author(id);
                author.setAuthorName(rs.getString("authorName"));
                author.setAuthorDetail(rs.getString("authorDetail"));
                authors.add(author);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authors;
    }

    public void linkAuthorToEbook(int ebookID, int authorID) {
        String sql = "INSERT INTO ebookauthor (ebookID, authorID) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ebookID);
            ps.setInt(2, authorID);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeByEbook(int ebookID) {
        String sql = "DELETE FROM ebookauthor WHERE ebookID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ebookID);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
