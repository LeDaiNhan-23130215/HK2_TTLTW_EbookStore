package DAO;

import models.Author;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorDAO {

    private static final Logger logger = LoggerFactory.getLogger(AuthorDAO.class);
    private static final String LOG_PREFIX = "[AUTHOR_DAO]";

    /* ================= INSERT ================= */
    public int insertAndReturnId(Author author) {
        logger.info("{} Attempting to insert author and return ID: {}", LOG_PREFIX, author.getAuthorName());
        String sql = "INSERT INTO author (authorName, authorDetail) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, author.getAuthorName());
            ps.setString(2, author.getAuthorDetail());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                logger.info("{} Successfully inserted author with ID: {}", LOG_PREFIX, generatedId);
                return generatedId;
            }
        } catch (SQLException e) {
            logger.error("{} Error in insertAndReturnId for author: {}", LOG_PREFIX, author.getAuthorName(), e);
            throw new RuntimeException(e);
        }
        return -1;
    }

    public boolean addAuthor(Author author) {
        logger.info("{} Adding new author: {}", LOG_PREFIX, author.getAuthorName());
        String sql = "INSERT INTO author (authorName, authorDetail) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, author.getAuthorName());
            ps.setString(2, author.getAuthorDetail());

            boolean success = ps.executeUpdate() > 0;
            if (success) logger.info("{} Successfully added author: {}", LOG_PREFIX, author.getAuthorName());
            return success;
        } catch (SQLException e) {
            logger.error("{} Failed to add author: {}", LOG_PREFIX, author.getAuthorName(), e);
            throw new RuntimeException(e);
        }
    }

    /* ================= GET BY ID ================= */
    public Author getById(int id) {
        logger.debug("{} Fetching author by ID: {}", LOG_PREFIX, id);
        String sql = "SELECT * FROM author WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                logger.debug("{} Found author: {}", LOG_PREFIX, id);
                return mapAuthor(rs);
            }
            logger.warn("{} Author with ID {} not found", LOG_PREFIX, id);
        } catch (SQLException e) {
            logger.error("{} Error retrieving author with ID: {}", LOG_PREFIX, id, e);
            throw new RuntimeException(e);
        }
        return null;
    }

    /* ================= GET ALL ================= */
    public List<Author> findAll() {
        logger.info("{} Fetching all authors", LOG_PREFIX);
        List<Author> list = new ArrayList<>();
        String sql = "SELECT * FROM author";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapAuthor(rs));
            }
            logger.debug("{} Total authors found: {}", LOG_PREFIX, list.size());
        } catch (SQLException e) {
            logger.error("{} Error fetching all authors", LOG_PREFIX, e);
            throw new RuntimeException(e);
        }
        return list;
    }

    /* ================= GET AUTHORS BY EBOOK ================= */
    public List<Author> getByEbookID(int ebookID) {
        logger.info("{} Fetching authors for Ebook ID: {}", LOG_PREFIX, ebookID);
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
            logger.debug("{} Found {} authors for Ebook ID: {}", LOG_PREFIX, list.size(), ebookID);
        } catch (SQLException e) {
            logger.error("{} Error fetching authors for Ebook ID: {}", LOG_PREFIX, ebookID, e);
            throw new RuntimeException(e);
        }
        return list;
    }

    /* ================= UPDATE ================= */
    public boolean update(Author author) {
        logger.info("{} Updating author ID: {}", LOG_PREFIX, author.getId());
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

            boolean success = ps.executeUpdate() > 0;
            if (success) logger.info("{} Successfully updated author ID: {}", LOG_PREFIX, author.getId());
            return success;
        } catch (SQLException e) {
            logger.error("{} Failed to update author ID: {}", LOG_PREFIX, author.getId(), e);
            throw new RuntimeException(e);
        }
    }

    /* ================= DELETE ================= */
    public boolean delete(int id) {
        logger.warn("{} Attempting to delete author ID: {}", LOG_PREFIX, id);
        String sql = "DELETE FROM author WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            boolean success = ps.executeUpdate() > 0;
            if (success) {
                logger.info("{} Successfully deleted author ID: {}", LOG_PREFIX, id);
            } else {
                logger.warn("{} No author found to delete with ID: {}", LOG_PREFIX, id);
            }
            return success;
        } catch (SQLException e) {
            logger.error("{} Error deleting author ID: {}. Check for foreign key constraints!", LOG_PREFIX, id, e);
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