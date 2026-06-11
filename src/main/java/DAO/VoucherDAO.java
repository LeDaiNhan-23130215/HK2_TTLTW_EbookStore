package DAO;

import models.Voucher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VoucherDAO {
    private static final Logger logger     = LoggerFactory.getLogger(UserDAO.class);
    private static final String LOG_PREFIX = "[VOUCHER_DAO]";

    public Voucher findByCode(String code) {
        String sql = """
        SELECT id, code, description, discount_type, discount_value, min_order_value,
               max_discount, quantity, used_count, expired_at, is_active
        FROM vouchers
        WHERE code = ? 
        """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, code);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Voucher v = new Voucher(rs.getInt("id"));

                v.setCode(rs.getString("code"));
                v.setDescription(rs.getString("description"));
                v.setDiscountType(rs.getString("discount_type"));
                v.setDiscountValue(rs.getDouble("discount_value"));
                v.setMinOrderValue(rs.getDouble("min_order_value"));
                v.setMaxDiscount(rs.getDouble("max_discount"));
                v.setQuantity(rs.getInt("quantity"));
                v.setUsedCount(rs.getInt("used_count"));
                v.setExpiredAt(rs.getTimestamp("expired_at"));
                v.setActive(rs.getInt("is_active") == 1);

                return v;
            }
        } catch (Exception e) {
            logger.error("{}", LOG_PREFIX);
        }
        return null;
    }

    public void increaseUsedCount(int id) {
        String sql = """
                UPDATE vouchers
                SET used_count = used_count + 1
                WHERE id = ?
                """;
        try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("{} Can't update used count for voucher id : {} ", LOG_PREFIX, id);
        }
    }
}
