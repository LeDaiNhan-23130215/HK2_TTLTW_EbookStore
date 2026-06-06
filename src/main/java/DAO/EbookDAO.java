package DAO;

import DTO.AdminEbookView;
import DTO.EbookFilterView;
import DTO.EbookProductCardView;
import models.Author;
import models.Ebook;
import models.Image;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EbookDAO {
    private static final Logger logger = LoggerFactory.getLogger(EbookDAO.class);
    private static final String LOG_PREFIX = "[EBOOK_DAO]";

    public Ebook getEbookById(int id) {
        String sql = "SELECT * FROM ebook WHERE id = ?";
        logger.debug("{} Attempting to retrieve ebook with id: {}", LOG_PREFIX, id);

        ImageDAO imageDAO = new ImageDAO();
        AuthorDAO authorDAO = new AuthorDAO();
        EbookImageDAO ebookImageDAO = new EbookImageDAO();
        EbookAuthorDAO ebookAuthorDAO = new EbookAuthorDAO();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Ebook ebook = new Ebook(
                        rs.getInt("id"),
                        rs.getString("eBookCode"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getInt("categoryID"),
                        rs.getInt("fileID"),
                        rs.getString("status")
                );

                List<Image> images = new ArrayList<>();
                for (int imgId : ebookImageDAO.getImageIdsByEbook(id)) {
                    images.add(imageDAO.getImageById(imgId));
                }

                List<Author> authors = new ArrayList<>();
                for (int authorId : ebookAuthorDAO.getAuthorIdsByEbook(id)) {
                    authors.add(authorDAO.getById(authorId));
                }

                ebook.setImages(images);
                ebook.setAuthors(authors);

                logger.info("{} Ebook {} retrieved successfully", LOG_PREFIX, id);
                return ebook;
            }
            logger.warn("{} Ebook {} not found in database", LOG_PREFIX, id);
        } catch (Exception e) {
            logger.error("{} Error retrieving ebook with id: {}", LOG_PREFIX, id, e);
            throw new RuntimeException("Failed to retrieve ebook id: " + id, e);
        }
        return null;
    }

    public int countTotalEBook() {
        String sql = "SELECT COUNT(*) FROM ebook";
        logger.debug("{} Counting total ebooks", LOG_PREFIX);

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            int total = rs.next() ? rs.getInt(1) : 0;
            logger.info("{} Total ebooks count: {}", LOG_PREFIX, total);
            return total;
        } catch (SQLException e) {
            logger.error("{} Error counting total ebooks", LOG_PREFIX, e);
            throw new RuntimeException("Failed to count total ebooks", e);
        }
    }

    public List<Ebook> getNewBook() {
        logger.info("{} Fetching 15 newest active ebooks", LOG_PREFIX);
        List<Ebook> ebooks = new ArrayList<>();
        String sql = "SELECT id, ebookCode, title, price, description, categoryID, fileID, status " +
                "FROM ebook " +
                "WHERE status = 'active' " +
                "ORDER BY id DESC " +
                "LIMIT 15";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ebooks.add(new Ebook(
                        rs.getInt("id"),
                        rs.getString("ebookCode"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getInt("categoryID"),
                        rs.getInt("fileID"),
                        rs.getString("status")
                ));
            }
            logger.debug("{} Successfully fetched {} new ebooks", LOG_PREFIX, ebooks.size());
        } catch (SQLException e) {
            logger.error("{} Error fetching newest ebooks", LOG_PREFIX, e);
            throw new RuntimeException("Failed to retrieve newest ebooks", e);
        }
        return ebooks;
    }

    public List<Ebook> findAll() {
        logger.debug("{} Finding all ACTIVE ebooks", LOG_PREFIX);
        List<Ebook> result = new ArrayList<>();
        String sql = "SELECT * FROM ebook where status = 'ACTIVE'";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new Ebook(
                        rs.getInt("id"),
                        rs.getString("eBookCode"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getInt("categoryID"),
                        rs.getInt("fileID"),
                        rs.getString("status")
                ));
            }
            logger.info("{} Found {} active ebooks", LOG_PREFIX, result.size());
        } catch (SQLException e) {
            logger.error("{} Error in findAll()", LOG_PREFIX, e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public Ebook getBasicEbook(int ebookID) {
        logger.debug("{} Getting basic ebook info for ID: {}", LOG_PREFIX, ebookID);
        String sql = "SELECT id, eBookCode, title, price, description, categoryID, fileID, status FROM ebook WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ebookID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Ebook ebook = new Ebook(rs.getInt("id"));
                ebook.setTitle(rs.getString("title"));
                ebook.setPrice(rs.getDouble("price"));
                ebook.setDescription(rs.getString("description"));
                ebook.setCategoryID(rs.getInt("categoryID"));
                ebook.setFileID(rs.getInt("fileID"));
                ebook.setStatus(rs.getString("status"));
                ebook.setBookCode(rs.getString("eBookCode"));
                return ebook;
            }
        } catch (Exception e) {
            logger.error("{} Error in getBasicEbook for ID: {}", LOG_PREFIX, ebookID, e);
        }
        return null;
    }

    public Ebook getEbookWithDetailsById(int ebookID) {
        logger.debug("{} Fetching ebook with details for ID: {}", LOG_PREFIX, ebookID);
        Ebook ebook = getBasicEbook(ebookID);
        if (ebook == null) {
            logger.warn("{} Cannot find basic ebook for details with ID: {}", LOG_PREFIX, ebookID);
            return null;
        }
        EbookImageDAO ebookImageDAO = new EbookImageDAO();
        EbookAuthorDAO ebookAuthorDAO = new EbookAuthorDAO();
        ebook.setImages(ebookImageDAO.getImagesByEbookID(ebookID));
        ebook.setAuthors(ebookAuthorDAO.getAuthorsByEbookID(ebookID)); // Note: Cần khởi tạo authorDAO hoặc gọi static tùy cấu trúc của bạn

        logger.info("{} Successfully built detailed ebook for ID: {}", LOG_PREFIX, ebookID);
        return ebook;
    }

    public List<EbookProductCardView> findProductCards(int page, int pageSize, EbookFilterView filter) {
        logger.info("{} Finding product cards - Page: {}, Size: {}", LOG_PREFIX, page, pageSize);
        List<EbookProductCardView> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                        SELECT
                                e.id,
                                e.title,
                                e.price,
                                COALESCE(
                                    MIN(CASE
                                        WHEN i.migration_status = 'MIGRATED'
                                             AND i.cloudinary_url IS NOT NULL
                                             AND i.cloudinary_url <> ''
                                        THEN i.cloudinary_url
                                    END),
                                    MIN(i.imgLink),
                                    '/assets/img/no-image.png'
                                ) AS thumbnail
                            FROM ebook e
                            LEFT JOIN ebookimage ei ON e.id = ei.ebookID
                            LEFT JOIN images i ON ei.imgID = i.id
                            LEFT JOIN ebookauthor ea ON e.id = ea.ebookID
                            LEFT JOIN author a ON ea.authorID = a.id
                            LEFT JOIN files f ON f.id = e.id
                            WHERE i.imgStatus = 'ACTIVE'
                            AND e.status = 'ACTIVE'
                """);

        List<Object> params = new ArrayList<>();
        applyFilter(sql, params, filter);
        sql.append(" GROUP BY e.id, e.title, e.price ");

        sql.append(" ORDER BY ");
        String sortBy = (filter.getSortBy() == null || filter.getSortBy().isEmpty()) ? "created_at" : filter.getSortBy();
        String sortDir = (filter.getSortDir() == null || filter.getSortDir().isEmpty()) ? "desc" : filter.getSortDir();

        switch (sortBy.toLowerCase()) {
            case "title" -> sql.append("e.title");
            case "price" -> sql.append("e.price");
            default -> sql.append("e.id");
        }
        sql.append(" ").append("desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC");
        sql.append(" LIMIT ? OFFSET ?");

        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            bindParams(ps, params);
            logger.debug("{} Executing query for cards with params: {}", LOG_PREFIX, params);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new EbookProductCardView(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("thumbnail")
                ));
            }
            logger.debug("{} Found {} product cards", LOG_PREFIX, result.size());
        } catch (SQLException e) {
            logger.error("{} Error finding product cards", LOG_PREFIX, e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public int countProductCards(EbookFilterView filter) {
        logger.debug("{} Counting filtered product cards", LOG_PREFIX);
        StringBuilder sql = new StringBuilder("""
                SELECT COUNT(DISTINCT e.id)
                FROM ebook e
                LEFT JOIN ebookauthor ea ON e.id = ea.ebookID
                LEFT JOIN author a ON ea.authorID = a.id
                JOIN files f ON f.id = e.id
                WHERE e.status = 'ACTIVE'
                """);

        List<Object> params = new ArrayList<>();
        applyFilter(sql, params, filter);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(String.valueOf(sql))) {
            bindParams(ps, params);
            ResultSet rs = ps.executeQuery();
            int count = rs.next() ? rs.getInt(1) : 0;
            logger.debug("{} Filtered product count: {}", LOG_PREFIX, count);
            return count;
        } catch (SQLException e) {
            logger.error("{} Error counting filtered product cards", LOG_PREFIX, e);
            throw new RuntimeException(e);
        }
    }

    public List<AdminEbookView> findAllForAdmin() {
        logger.info("{} Admin: Fetching all ebooks for management", LOG_PREFIX);
        List<AdminEbookView> list = new ArrayList<>();
        String sql = """
        SELECT e.id, e.title, a.authorName, c.categoryName, e.price
        FROM ebook e
        JOIN author a ON e.authorID = a.id
        JOIN category c ON e.categoryID = c.id
        ORDER BY e.id DESC
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new AdminEbookView(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("authorName"),
                        rs.getString("categoryName"),
                        rs.getDouble("price")
                ));
            }
            logger.debug("{} Admin: Loaded {} ebooks", LOG_PREFIX, list.size());
        } catch (SQLException e) {
            logger.error("{} Admin: Error in findAllForAdmin", LOG_PREFIX, e);
            throw new RuntimeException(e);
        }
        return list;
    }

    public boolean insert(Ebook e) {
        logger.info("{} Inserting new ebook: {}", LOG_PREFIX, e.getTitle());
        String sql = "INSERT INTO ebook (eBookCode, title, price, description, categoryID, fileID, status) VALUES (?, ?, ?, ?, ?, ?, 'ACTIVE')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getEBookCode());
            ps.setString(2, e.getTitle());
            ps.setDouble(3, e.getPrice());
            ps.setString(4, e.getDescription());
            ps.setInt(5, e.getCategoryID());
            ps.setInt(6, e.getFileID());

            boolean success = ps.executeUpdate() > 0;
            if (success) logger.info("{} Successfully inserted ebook: {}", LOG_PREFIX, e.getEBookCode());
            return success;
        } catch (SQLException ex) {
            logger.error("{} Error inserting ebook: {}", LOG_PREFIX, e.getEBookCode(), ex);
            throw new RuntimeException(ex);
        }
    }

    public int insertAndReturnId(Ebook e) {
        logger.info("{} Inserting ebook and returning ID: {}", LOG_PREFIX, e.getTitle());
        String sql = "INSERT INTO ebook (eBookCode, title, price, description, categoryID, fileID, status) VALUES (?, ?, ?, ?, ?, ?, 'ACTIVE')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getEBookCode());
            ps.setString(2, e.getTitle());
            ps.setDouble(3, e.getPrice());
            ps.setString(4, e.getDescription());
            ps.setInt(5, e.getCategoryID());
            ps.setInt(6, e.getFileID());

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        logger.info("{} Ebook created with ID: {}", LOG_PREFIX, generatedId);
                        return generatedId;
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("{} Error in insertAndReturnId for: {}", LOG_PREFIX, e.getTitle(), ex);
            throw new RuntimeException(ex);
        }
        return -1;
    }

    public boolean update(Ebook e) {
        logger.info("{} Updating ebook ID: {}", LOG_PREFIX, e.getId());
        String sql = "UPDATE ebook SET title = ?, price = ?, description = ?, categoryID = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getTitle());
            ps.setDouble(2, e.getPrice());
            ps.setString(3, e.getDescription());
            ps.setInt(4, e.getCategoryID());
            ps.setInt(5, e.getId());

            boolean success = ps.executeUpdate() > 0;
            if (success) logger.info("{} Successfully updated ebook ID: {}", LOG_PREFIX, e.getId());
            return success;
        } catch (SQLException ex) {
            logger.error("{} Error updating ebook ID: {}", LOG_PREFIX, e.getId(), ex);
            throw new RuntimeException(ex);
        }
    }

    public boolean delete(int id) {
        logger.info("{} Deactivating (soft delete) ebook ID: {}", LOG_PREFIX, id);
        String sql = "UPDATE ebook SET status = 'INACTIVE' WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            boolean success = ps.executeUpdate() > 0;
            if (success) logger.info("{} Ebook ID: {} is now INACTIVE", LOG_PREFIX, id);
            return success;
        } catch (SQLException e) {
            logger.error("{} Error deactivating ebook ID: {}", LOG_PREFIX, id, e);
            throw new RuntimeException(e);
        }
    }

    public Integer getMaxCodeNumberByCategory(int categoryId) {
        logger.debug("{} Getting max code number for category: {}", LOG_PREFIX, categoryId);
        String sql = "SELECT MAX(CAST(SUBSTRING(eBookCode, 3) AS UNSIGNED)) FROM ebook WHERE categoryID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            logger.error("{} Error getting max code for category: {}", LOG_PREFIX, categoryId, e);
        }
        return null;
    }


    public List<Ebook> getSimilarByCategory(int categoryID, int excludeEbookId, int limit) {
        logger.debug("{} Fetching {} similar books for category: {} (excluding ID: {})", LOG_PREFIX, limit, categoryID, excludeEbookId);
        List<Ebook> list = new ArrayList<>();
        String sql = "SELECT * FROM ebook WHERE status = 'ACTIVE' AND categoryID = ? AND id <> ? ORDER BY RAND() LIMIT ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, categoryID);
            ps.setInt(2, excludeEbookId);
            ps.setInt(3, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Ebook(
                        rs.getInt("id"),
                        rs.getString("eBookCode"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getInt("categoryID"),
                        rs.getInt("fileID"),
                        rs.getString("status")
                ));
            }
            return list;
        } catch (SQLException e) {
            logger.error("{} Error fetching similar ebooks", LOG_PREFIX, e);
            throw new RuntimeException(e);
        }
    }

    public void bindParams(PreparedStatement ps, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
    }

    private void applyFilter(StringBuilder sql, List<Object> params, EbookFilterView f) {
        if (f.getFree() != null) {
            sql.append(f.getFree() ? " AND e.price = 0 " : " AND e.price > 0 ");
        }
        if (f.getCategoryId() != null && !f.getCategoryId().isEmpty()) {
            sql.append(" AND e.categoryID IN (");
            sql.repeat("?,", f.getCategoryId().size() - 1).append("?)");
            params.addAll(f.getCategoryId());
        }
        if (f.getFormats() != null && !f.getFormats().isEmpty()) {
            sql.append(" AND f.fileFormat IN (");
            for (int i = 0; i < f.getFormats().size(); i++) {
                sql.append("?");
                if (i < f.getFormats().size() - 1) sql.append(",");
            }
            sql.append(")");
            params.addAll(f.getFormats());
        }
        if (f.getKeywords() != null && !f.getKeywords().isEmpty()) {
            sql.append(" AND (e.title LIKE ? OR a.authorName LIKE ?)");
            params.add("%" + f.getKeywords() + "%");
            params.add("%" + f.getKeywords() + "%");
        }
    }

    public List<Ebook> getAdminEbooks(int page, int size) {
        List<Ebook> list = new ArrayList<>();
        String sql = """
            SELECT id, title, price, status, eBookCode, categoryID
            FROM ebook
            ORDER BY id DESC
            LIMIT ? OFFSET ?
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, (page - 1) * size);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = Integer.parseInt(rs.getString("id"));
                Ebook e = new Ebook(id);
                e.setTitle(rs.getString("title"));
                e.setPrice(rs.getDouble("price"));
                e.setStatus(rs.getString("status"));
                e.setBookCode(rs.getString("eBookCode"));
                e.setCategoryID(rs.getInt("categoryID"));
                list.add(e);
            }
        } catch (Exception e) {
            logger.error("{} Error fetching admin ebooks", LOG_PREFIX);
        }
        return list;

    }

    public List<EbookProductCardView> getNewEbookCardsWithThumbnail(int limit) {
        List<EbookProductCardView> result = new ArrayList<>();
        String sql = """
            SELECT e.id,
                e.title,
                e.price,
                COALESCE(
                    MIN(CASE
                        WHEN i.migration_status = 'MIGRATED'
                                 AND i.cloudinary_url IS NOT NULL
                                 AND i.cloudinary_url <> ''
                            THEN i.cloudinary_url
                    END),
                    MIN(i.imgLink),
                    '/assets/img/no-image.png'
                ) AS thumbnail
            FROM ebook e
            LEFT JOIN ebookimage ei ON e.id = ei.ebookID
            LEFT JOIN images i ON ei.imgID = i.id
            WHERE e.status = 'ACTIVE'
            GROUP BY e.id, e.title, e.price
            ORDER BY e.id DESC
            LIMIT ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, limit);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new EbookProductCardView(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("thumbnail")
                ));
            }

            logger.info("{} Loaded {} ebook cards", LOG_PREFIX, result.size());

        } catch (SQLException e) {
            logger.error("{} Error in getEbookCardsWithThumbnail", LOG_PREFIX, e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public List<EbookProductCardView> getRandomEbookCardsWithThumbnail(int limit) {
        List<EbookProductCardView> result = new ArrayList<>();
        String sql = """
            SELECT
                e.id,
                e.title,
                e.price,
                COALESCE(
                    MIN(CASE 
                        WHEN i.migration_status = 'MIGRATED'
                             AND i.cloudinary_url IS NOT NULL
                             AND i.cloudinary_url <> ''
                        THEN i.cloudinary_url
                    END),
                    MIN(i.imgLink),
                    '/assets/img/no-image.png'
                ) AS thumbnail
            FROM ebook e
            LEFT JOIN ebookimage ei ON e.id = ei.ebookID
            LEFT JOIN images i ON ei.imgID = i.id
            WHERE e.status = 'ACTIVE'
            GROUP BY e.id, e.title, e.price
            ORDER BY RAND()
            LIMIT ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, limit);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new EbookProductCardView(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("thumbnail")
                ));
            }

            logger.info("{} Loaded random {} ebook cards", LOG_PREFIX, result.size());

        } catch (SQLException e) {
            logger.error("{} Error in getEbookCardsWithThumbnail", LOG_PREFIX, e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public EbookProductCardView getEbookCardsWithThumbnailById(int id) {
        EbookProductCardView result = null;
        String sql = """
            SELECT 
                e.id,
                e.title,
                e.price,
                COALESCE(
                    MIN(CASE 
                        WHEN i.migration_status = 'MIGRATED'
                             AND i.cloudinary_url IS NOT NULL
                             AND i.cloudinary_url <> ''
                        THEN i.cloudinary_url
                    END),
                    MIN(i.imgLink),
                    '/assets/img/no-image.png'
                ) AS thumbnail
            FROM ebook e
            LEFT JOIN ebookimage ei ON e.id = ei.ebookID
            LEFT JOIN images i ON ei.imgID = i.id
            WHERE e.status = 'ACTIVE' 
            AND e.id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = new EbookProductCardView(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("thumbnail")
                );
            }

            logger.info("{} Loaded ebook cards with id: {}", LOG_PREFIX, id);

        } catch (SQLException e) {
            logger.error("{} Error in getEbookCardsWithThumbnail", LOG_PREFIX, e);
            throw new RuntimeException(e);
        }
        return result;
    }
}