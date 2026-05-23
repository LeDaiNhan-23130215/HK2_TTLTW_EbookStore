package DAO;

import models.Author;
import utils.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EbookAuthorDAO {

    private static final Logger logger = LoggerFactory.getLogger(EbookAuthorDAO.class);

    public void insert(int ebookId, int authorId) {
        logger.info("Executing insert for ebookId: {}, authorId: {}", ebookId, authorId);
        String sql = "INSERT INTO ebookauthor (ebookID, authorID) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ebookId);
            ps.setInt(2, authorId);
            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Error in insert for ebookId: {}, authorId: {}", ebookId, authorId, e);
            throw new RuntimeException(e);
        }
    }

    public List<Integer> getAuthorIdsByEbook(int ebookId) {
        logger.info("Executing getAuthorIdsByEbook for ebookId: {}", ebookId);
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT authorID FROM ebookauthor WHERE ebookID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getInt("authorID"));
            }
            logger.info("Successfully fetched {} author IDs for ebookId: {}", list.size(), ebookId);

        } catch (Exception e) {
            logger.error("Error in getAuthorIdsByEbook for ebookId: {}", ebookId, e);
            e.printStackTrace();
        }
        return list;
    }

    public List<Author> getAuthorsByEbookID(int ebookID) {
        logger.info("Executing getAuthorsByEbookID for ebookID: {}", ebookID);
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
            logger.info("Successfully fetched {} authors for ebookID: {}", authors.size(), ebookID);
        } catch (Exception e) {
            logger.error("Error in getAuthorsByEbookID for ebookID: {}", ebookID, e);
            e.printStackTrace();
        }
        return authors;
    }

    public void linkAuthorToEbook(int ebookID, int authorID) {
        logger.info("Executing linkAuthorToEbook for ebookID: {}, authorID: {}", ebookID, authorID);
        String sql = "INSERT INTO ebookauthor (ebookID, authorID) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ebookID);
            ps.setInt(2, authorID);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error in linkAuthorToEbook for ebookID: {}, authorID: {}", ebookID, authorID, e);
            throw new RuntimeException(e);
        }
    }

    public void removeByEbook(int ebookID) {
        logger.info("Executing removeByEbook for ebookID: {}", ebookID);
        String sql = "DELETE FROM ebookauthor WHERE ebookID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ebookID);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error in removeByEbook for ebookID: {}", ebookID, e);
            throw new RuntimeException(e);
        }
    }
}