package DAO;

import DTO.AdminEbookView;
import DTO.EbookFilterView;
import DTO.EbookProductCardView;
import models.Author;
import models.Ebook;
import models.Image;
import services.EbookService;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EbookDAO {

    public Ebook getEbookById(int id) {
        String sql = "SELECT * FROM ebook WHERE id = ?";
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

                // ===== LOAD IMAGES =====
                List<Image> images = new ArrayList<>();
                for (int imgId : ebookImageDAO.getImageIdsByEbook(id)) {
                    images.add(imageDAO.getImageById(imgId));
                }

                // ===== LOAD AUTHORS =====
                List<Author> authors = new ArrayList<>();
                for (int authorId : ebookAuthorDAO.getAuthorIdsByEbook(id)) {
                    authors.add(authorDAO.getById(authorId));
                }

                ebook.setImages(images);
                ebook.setAuthors(authors);

                return ebook;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    public int countTotalEBook() {
        String sql = "SELECT COUNT(*) FROM ebook";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Ebook> getNewBook() {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ebooks;
    }

    public List<Ebook> findAll() {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Ebook getBasicEbook(int ebookID) {
        String sql = """
        SELECT id, eBookCode, title, price, description, categoryID,
               fileID, status
        FROM ebook
        WHERE id = ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ebookID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = Integer.parseInt(rs.getString("id"));
                Ebook ebook = new Ebook(id);
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
            e.printStackTrace();
        }
        return null;
    }

    public Ebook getEbookWithDetailsById(int ebookID) {
        Ebook ebook = getBasicEbook(ebookID);
        if (ebook == null) return null;
        EbookImageDAO ebookImageDAO = new EbookImageDAO();
        EbookAuthorDAO ebookAuthorDAO = new EbookAuthorDAO();
        ebook.setImages(ebookImageDAO.getImagesByEbookID(ebookID));
        ebook.setAuthors(ebookAuthorDAO.getAuthorsByEbookID(ebookID));

        return ebook;
    }

    public List<EbookProductCardView> findProductCards(
            int page, int pageSize, EbookFilterView filter) {

        List<EbookProductCardView> result = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                    SELECT e.id, e.title, e.price, MIN(i.imgLink) AS imgLink
                    FROM ebook e
                    JOIN ebookimage ie ON e.id = ie.ebookID
                    JOIN images i ON ie.imgID = i.id
                    LEFT JOIN ebookauthor ea ON e.id = ea.ebookID
                    LEFT JOIN author a ON ea.authorID = a.id
                    LEFT JOIN files f ON f.id = e.id
                    WHERE i.imgStatus = 'ACTIVE'
                    AND i.imgStatus = 'ACTIVE'
                """);

        List<Object> params = new ArrayList<>();
        applyFilter(sql, params, filter);
        sql.append(" GROUP BY e.id, e.title, e.price ");
        // ===== SORTING (ALWAYS APPLY) =====
        sql.append(" ORDER BY ");

        String sortBy = filter.getSortBy();
        String sortDir = filter.getSortDir();

        // Default values if not specified
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "created_at";
        }
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "desc";
        }

        // Apply sort field
        switch (sortBy.toLowerCase()) {
            case "title":
                sql.append("e.title");
                break;
            case "price":
                sql.append("e.price");
                break;
            case "created_at":
                sql.append("e.id"); // Using id as proxy for created_at
                break;
            default:
                sql.append("e.id"); // Default sort by ID (newest first)
        }

        // Apply sort direction
        sql.append(" ");
        sql.append("desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC");

        // ===== PAGINATION =====
        sql.append(" LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            bindParams(ps, params);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(new EbookProductCardView(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("imgLink")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public int countProductCards(EbookFilterView filter) {

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
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            bindParams(ps, params);

            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AdminEbookView> findAllForAdmin() {

        List<AdminEbookView> list = new ArrayList<>();

        String sql = """
        SELECT 
            e.id,
            e.title,
            a.authorName,
            c.categoryName,
            e.price
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
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
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Ebook e) {

        String sql = """
        INSERT INTO ebook
        (eBookCode, title, price, description,
         categoryID,fileID, status)
        VALUES (?, ?, ?, ?, ?, ?, 'ACTIVE')
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getEBookCode());
            ps.setString(2, e.getTitle());
            ps.setDouble(3, e.getPrice());
            ps.setString(4, e.getDescription());
            ps.setInt(5, e.getCategoryID());
            ps.setInt(6, e.getFileID());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int insertAndReturnId(Ebook e) {
        String sql = """
        INSERT INTO ebook
        (eBookCode, title, price, description,
         categoryID,fileID, status)
        VALUES (?, ?, ?, ?, ?, ?, 'ACTIVE')
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, e.getEBookCode());
            ps.setString(2, e.getTitle());
            ps.setDouble(3, e.getPrice());
            ps.setString(4, e.getDescription());
            ps.setInt(5, e.getCategoryID());
            ps.setInt(6, e.getFileID());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return -1;
    }

    public Ebook getByIdForAdmin(int id) {

        String sql = "SELECT * FROM ebook WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Ebook(
                        rs.getInt("id"),
                        rs.getString("eBookCode"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getInt("categoryID"),
                        rs.getInt("fileID"),
                        rs.getString("status")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public boolean update(Ebook e) {

        String sql = """
        UPDATE ebook
        SET title = ?,
            price = ?,
            description = ?,
            categoryID = ?
        WHERE id = ?
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getTitle());
            ps.setDouble(2, e.getPrice());
            ps.setString(3, e.getDescription());
            ps.setInt(4, e.getCategoryID());
            ps.setInt(5, e.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean delete(int id) {

        String sql = "UPDATE ebook SET status = 'INACTIVE' WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void applyFilter(StringBuilder sql,
                             List<Object> params,
                             EbookFilterView f) {

        // ===== FREE/PAID FILTER =====
        if (f.getFree() != null) {
            sql.append(f.getFree() ? " AND e.price = 0 " : " AND e.price > 0 ");
        }

        // ===== CATEGORY FILTER =====
        if (f.getCategoryId() != null && !f.getCategoryId().isEmpty()) {
            sql.append(" AND e.categoryID IN (");
            sql.append("?,".repeat(f.getCategoryId().size() - 1)).append("?)");
            params.addAll(f.getCategoryId());
        }

        // ===== FORMAT FILTER (NEW) =====
        if (f.getFormats() != null && !f.getFormats().isEmpty()) {
            sql.append(" AND f.fileFormat IN (");
            for (int i = 0; i < f.getFormats().size(); i++) {
                sql.append("?");
                if (i < f.getFormats().size() - 1) {
                    sql.append(",");
                }
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

    public Integer getMaxCodeNumberByCategory(int categoryId) {
        String sql = """
        SELECT MAX(CAST(SUBSTRING(eBookCode, 3) AS UNSIGNED))
        FROM ebook
        WHERE categoryID = ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void bindParams(PreparedStatement ps, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
    }

    public List<Ebook> getRandomEbook(int numberOfBook) {
        List<Ebook> ebooks = new ArrayList<>();
        String sql = "SELECT id, ebookCode, title, price, description, categoryID, fileID, status " +
                "FROM ebook " +
                "WHERE status = 'active' " +
                "ORDER BY RAND() " +
                "LIMIT ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, numberOfBook);
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ebooks;
    }

    public List<Ebook> getSimilarByCategory(int categoryID, int excludeEbookId, int limit) {
        List<Ebook> list = new ArrayList<>();

        String sql = """
        SELECT id, eBookCode, title, price, description, categoryID, fileID, status
        FROM ebook
        WHERE status = 'ACTIVE'
          AND categoryID = ?
          AND id <> ?
        ORDER BY RAND()
        LIMIT ?
    """;

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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }






    public static void main(String[] args) {
        // Test code here

    }
}