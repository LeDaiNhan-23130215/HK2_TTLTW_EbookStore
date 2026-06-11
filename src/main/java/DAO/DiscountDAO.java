package DAO;

import models.Discount;
import utils.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscountDAO {

    private static final Logger logger    = LoggerFactory.getLogger(DiscountDAO.class);
    private static final String LOG_PREFIX = "[DISCOUNT_DAO]";

    private Discount map(ResultSet rs) throws SQLException {
        Discount d = new Discount();
        d.setId(rs.getInt("id"));
        d.setName(rs.getString("name"));
        d.setDescription(rs.getString("description"));
        d.setDiscountType(rs.getString("discount_type"));
        d.setDiscountValue(rs.getBigDecimal("discount_value"));
        Timestamp start = rs.getTimestamp("start_date");
        Timestamp end   = rs.getTimestamp("end_date");
        if (start != null) d.setStartDate(start.toLocalDateTime());
        if (end   != null) d.setEndDate(end.toLocalDateTime());
        d.setStatus(rs.getString("status"));
        return d;
    }

    public List<Discount> getAll() {
        String sql = "SELECT * FROM discount ORDER BY id DESC";
        List<Discount> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (Exception e) {
            logger.error("{} getAll error", LOG_PREFIX, e);
        }
        return list;
    }

    public Discount getById(int id) {
        String sql = "SELECT * FROM discount WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (Exception e) {
            logger.error("{} getById error id={}", LOG_PREFIX, id, e);
        }
        return null;
    }

    public int create(Discount d) {
        String sql = """
            INSERT INTO discount
              (name, description, discount_type, discount_value, start_date, end_date, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getName());
            ps.setString(2, d.getDescription());
            ps.setString(3, d.getDiscountType());
            ps.setBigDecimal(4, d.getDiscountValue());
            ps.setObject(5, d.getStartDate() != null ? Timestamp.valueOf(d.getStartDate()) : null);
            ps.setObject(6, d.getEndDate()   != null ? Timestamp.valueOf(d.getEndDate())   : null);
            ps.setString(7, d.getStatus());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (Exception e) {
            logger.error("{} create error", LOG_PREFIX, e);
        }
        return -1;
    }

    public boolean update(Discount d) {
        String sql = """
            UPDATE discount
               SET name=?, description=?, discount_type=?, discount_value=?,
                   start_date=?, end_date=?, status=?
             WHERE id=?
            """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getName());
            ps.setString(2, d.getDescription());
            ps.setString(3, d.getDiscountType());
            ps.setBigDecimal(4, d.getDiscountValue());
            ps.setObject(5, d.getStartDate() != null ? Timestamp.valueOf(d.getStartDate()) : null);
            ps.setObject(6, d.getEndDate()   != null ? Timestamp.valueOf(d.getEndDate())   : null);
            ps.setString(7, d.getStatus());
            ps.setInt(8, d.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error("{} update error id={}", LOG_PREFIX, d.getId(), e);
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM discount WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error("{} delete error id={}", LOG_PREFIX, id, e);
        }
        return false;
    }

    public int expireEnded() {
        String sql = """
            UPDATE discount SET status = 'ENDED'
             WHERE status = 'ACTIVE'
               AND end_date IS NOT NULL
               AND end_date < NOW()
            """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            return ps.executeUpdate();
        } catch (Exception e) {
            logger.error("{} expireEnded error", LOG_PREFIX, e);
        }
        return 0;
    }

    public List<Discount> findActiveForEbook(int ebookId) {
        String sql = """
            SELECT DISTINCT d.*
              FROM discount d
             WHERE d.status = 'ACTIVE'
               AND (d.start_date IS NULL OR d.start_date <= NOW())
               AND (d.end_date   IS NULL OR d.end_date   >= NOW())
               AND (
                     EXISTS (
                         SELECT 1 FROM discount_ebook de
                          WHERE de.discountID = d.id AND de.ebookID = ?
                     )
                     OR
                     EXISTS (
                         SELECT 1 FROM discount_category dc
                           JOIN ebook e ON e.categoryID = dc.categoryID
                          WHERE dc.discountID = d.id AND e.id = ?
                     )
                     OR
                     EXISTS (
                         SELECT 1 FROM discount_author da
                           JOIN ebookauthor ea ON ea.authorID = da.authorID
                          WHERE da.discountID = d.id AND ea.ebookID = ?
                     )
                   )
            """;
        List<Discount> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ebookId);
            ps.setInt(2, ebookId);
            ps.setInt(3, ebookId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (Exception e) {
            logger.error("{} findActiveForEbook error ebookId={}", LOG_PREFIX, ebookId, e);
        }
        return list;
    }

    public void setEbooks(int discountId, List<Integer> ebookIds) {
        replaceMapping(discountId, ebookIds,
                "DELETE FROM discount_ebook WHERE discountID = ?",
                "INSERT IGNORE INTO discount_ebook (discountID, ebookID) VALUES (?, ?)");
    }

    public void setCategories(int discountId, List<Integer> categoryIds) {
        replaceMapping(discountId, categoryIds,
                "DELETE FROM discount_category WHERE discountID = ?",
                "INSERT IGNORE INTO discount_category (discountID, categoryID) VALUES (?, ?)");
    }

    public void setAuthors(int discountId, List<Integer> authorIds) {
        replaceMapping(discountId, authorIds,
                "DELETE FROM discount_author WHERE discountID = ?",
                "INSERT IGNORE INTO discount_author (discountID, authorID) VALUES (?, ?)");
    }

    private void replaceMapping(int discountId, List<Integer> ids,
                                String deleteSql, String insertSql) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement del = con.prepareStatement(deleteSql);
            del.setInt(1, discountId);
            del.executeUpdate();

            if (ids == null || ids.isEmpty()) return;

            PreparedStatement ins = con.prepareStatement(insertSql);
            for (int id : ids) {
                ins.setInt(1, discountId);
                ins.setInt(2, id);
                ins.addBatch();
            }
            ins.executeBatch();
        } catch (Exception e) {
            logger.error("{} replaceMapping error discountId={}", LOG_PREFIX, discountId, e);
        }
    }

    public List<Integer> getEbookIds(int discountId) {
        return getIds("SELECT ebookID FROM discount_ebook WHERE discountID = ?", discountId);
    }

    public List<Integer> getCategoryIds(int discountId) {
        return getIds("SELECT categoryID FROM discount_category WHERE discountID = ?", discountId);
    }

    public List<Integer> getAuthorIds(int discountId) {
        return getIds("SELECT authorID FROM discount_author WHERE discountID = ?", discountId);
    }

    private List<Integer> getIds(String sql, int discountId) {
        List<Integer> ids = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, discountId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) ids.add(rs.getInt(1));
        } catch (Exception e) {
            logger.error("{} getIds error", LOG_PREFIX, e);
        }
        return ids;
    }
}
