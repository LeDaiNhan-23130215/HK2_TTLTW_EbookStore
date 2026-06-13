package DAO;

import models.Voucher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoucherDAO {
    private static final Logger logger     = LoggerFactory.getLogger(VoucherDAO.class);
    private static final String LOG_PREFIX = "[VOUCHER_DAO]";

    private Voucher map(ResultSet rs) throws SQLException {
        Voucher v = new Voucher(rs.getInt("id"));
        v.setCode(rs.getString("code"));
        v.setDescription(rs.getString("description"));
        v.setDiscountType(rs.getString("discount_type"));
        v.setDiscountValue(rs.getDouble("discount_value"));
        v.setMinOrderValue(rs.getDouble("min_order_value"));
        v.setMaxDiscount(rs.getDouble("max_discount"));
        v.setQuantity(rs.getInt("quantity"));
        v.setUsedCount(rs.getInt("used_count"));
        v.setStartedAt(rs.getTimestamp("started_at"));
        v.setExpiredAt(rs.getTimestamp("expired_at"));
        int mup = rs.getInt("max_uses_per_user");
        v.setMaxUsesPerUser(rs.wasNull() ? null : mup);
        v.setActive(rs.getInt("is_active") == 1);
        return v;
    }

    public List<Voucher> findAll() {
        String sql = "SELECT * FROM vouchers ORDER BY created_at DESC";
        List<Voucher> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (Exception e) {
            logger.error("{} findAll error: {}", LOG_PREFIX, e.getMessage());
        }
        return list;
    }

    public Voucher findById(int id) {
        String sql = "SELECT * FROM vouchers WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (Exception e) {
            logger.error("{} findById error: {}", LOG_PREFIX, e.getMessage());
        }
        return null;
    }

    public Voucher findByCode(String code) {
        String sql = "SELECT * FROM vouchers WHERE code = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (Exception e) {
            logger.error("{} findByCode error: {}", LOG_PREFIX, e.getMessage());
        }
        return null;
    }

    public boolean existsByCode(String code) {
        String sql = "SELECT 1 FROM vouchers WHERE code = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            logger.error("{} existsByCode error: {}", LOG_PREFIX, e.getMessage());
        }
        return false;
    }

    public boolean existsByCodeExcluding(String code, int excludeId) {
        String sql = "SELECT 1 FROM vouchers WHERE code = ? AND id <> ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.setInt(2, excludeId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            logger.error("{} existsByCodeExcluding error: {}", LOG_PREFIX, e.getMessage());
        }
        return false;
    }

    public boolean insert(Voucher v) {
        String sql = """
            INSERT INTO vouchers
              (code, description, discount_type, discount_value, min_order_value,
               max_discount, quantity, started_at, expired_at, max_uses_per_user, is_active)
            VALUES (?,?,?,?,?,?,?,?,?,?,?)
            """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, v.getCode().toUpperCase());
            ps.setString(2, v.getDescription());
            ps.setString(3, v.getDiscountType());
            ps.setDouble(4, v.getDiscountValue());
            ps.setDouble(5, v.getMinOrderValue());
            if (v.getMaxDiscount() > 0) ps.setDouble(6, v.getMaxDiscount());
            else ps.setNull(6, Types.DOUBLE);
            ps.setInt(7, v.getQuantity());
            if (v.getStartedAt() != null) ps.setTimestamp(8, v.getStartedAt());
            else ps.setNull(8, Types.TIMESTAMP);
            ps.setTimestamp(9, v.getExpiredAt());
            if (v.getMaxUsesPerUser() != null) ps.setInt(10, v.getMaxUsesPerUser());
            else ps.setNull(10, Types.INTEGER);
            ps.setInt(11, v.isActive() ? 1 : 0);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error("{} insert error: {}", LOG_PREFIX, e.getMessage());
        }
        return false;
    }

    public boolean update(Voucher v) {
        String sql = """
            UPDATE vouchers SET
              code = ?, description = ?, discount_type = ?, discount_value = ?,
              min_order_value = ?, max_discount = ?, quantity = ?,
              started_at = ?, expired_at = ?, max_uses_per_user = ?, is_active = ?
            WHERE id = ?
            """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, v.getCode().toUpperCase());
            ps.setString(2, v.getDescription());
            ps.setString(3, v.getDiscountType());
            ps.setDouble(4, v.getDiscountValue());
            ps.setDouble(5, v.getMinOrderValue());
            if (v.getMaxDiscount() > 0) ps.setDouble(6, v.getMaxDiscount());
            else ps.setNull(6, Types.DOUBLE);
            ps.setInt(7, v.getQuantity());
            if (v.getStartedAt() != null) ps.setTimestamp(8, v.getStartedAt());
            else ps.setNull(8, Types.TIMESTAMP);
            ps.setTimestamp(9, v.getExpiredAt());
            if (v.getMaxUsesPerUser() != null) ps.setInt(10, v.getMaxUsesPerUser());
            else ps.setNull(10, Types.INTEGER);
            ps.setInt(11, v.isActive() ? 1 : 0);
            ps.setInt(12, v.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error("{} update error: {}", LOG_PREFIX, e.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM vouchers WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error("{} delete error: {}", LOG_PREFIX, e.getMessage());
        }
        return false;
    }

    public int countUsageByUser(int voucherId, int userId) {
        String sql = "SELECT COUNT(*) FROM voucher_usage WHERE voucher_id = ? AND user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, voucherId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            logger.error("{} countUsageByUser error: {}", LOG_PREFIX, e.getMessage());
        }
        return 0;
    }

    public void recordUsage(int voucherId, int userId) {
        String sqlUpdate = "UPDATE vouchers SET used_count = used_count + 1 WHERE id = ?";
        String sqlInsert = "INSERT INTO voucher_usage (voucher_id, user_id) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps1 = con.prepareStatement(sqlUpdate);
                 PreparedStatement ps2 = con.prepareStatement(sqlInsert)) {
                ps1.setInt(1, voucherId);
                ps1.executeUpdate();
                ps2.setInt(1, voucherId);
                ps2.setInt(2, userId);
                ps2.executeUpdate();
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        } catch (Exception e) {
            logger.error("{} recordUsage error: {}", LOG_PREFIX, e.getMessage());
        }
    }

    public void increaseUsedCount(int id) {
        String sql = "UPDATE vouchers SET used_count = used_count + 1 WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("{} increaseUsedCount error: {}", LOG_PREFIX, e.getMessage());
        }
    }
}