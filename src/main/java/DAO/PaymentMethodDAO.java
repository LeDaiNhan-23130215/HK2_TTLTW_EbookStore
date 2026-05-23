package DAO;

import models.PaymentMethod;
import utils.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentMethodDAO {

    private static final Logger logger = LoggerFactory.getLogger(PaymentMethodDAO.class);

    public PaymentMethod getPMById(int id) {
        logger.info("Executing getPMById for id: {}", id);
        String sql = "SELECT * FROM paymentmethod WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                logger.info("Successfully fetched payment method for id: {}", id);
                return new PaymentMethod(id, rs.getString("name"), rs.getString("type"), rs.getString("description"), rs.getInt("isActive"));
            }
            logger.info("No payment method found for id: {}", id);

        } catch (Exception e) {
            logger.error("Error in getPMById for id: {}", id, e);
            e.printStackTrace();
        }

        return null;
    }

    public List<PaymentMethod> getAllPMs() {
        logger.info("Executing getAllPMs");
        List<PaymentMethod> list = new ArrayList<>();
        String sql = "SELECT * FROM paymentmethod ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PaymentMethod pm = new PaymentMethod(
                        rs.getInt("id"), rs.getString("name"), rs.getString("type"), rs.getString("description"), rs.getInt("isActive")
                );
                list.add(pm);
            }
            logger.info("Successfully fetched {} payment methods", list.size());

        } catch (Exception e) {
            logger.error("Error in getAllPMs", e);
            e.printStackTrace();
        }

        return list;
    }

    public boolean addPM(PaymentMethod paymentMethod) {
        logger.info("Executing addPM for name: {}", paymentMethod.getName());
        String sql = "INSERT INTO paymentmethod (name, type, description, isActive)"
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentMethod.getName());
            ps.setString(2, paymentMethod.getType());
            ps.setString(3, paymentMethod.getDescription());
            ps.setInt(4, paymentMethod.getIsActive());

            boolean result = ps.executeUpdate() > 0;
            logger.info("Add payment method status for name '{}': {}", paymentMethod.getName(), result);
            return result;

        } catch (Exception e) {
            logger.error("Error in addPM for name: {}", paymentMethod.getName(), e);
            e.printStackTrace();
        }

        return false;
    }

    public boolean deletePM(int id) {
        logger.info("Executing deletePM for id: {}", id);
        String sql = "DELETE FROM paymentmethod WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            boolean result = ps.executeUpdate() > 0;
            logger.info("Delete payment method status for id {}: {}", id, result);
            return result;

        } catch (Exception e) {
            logger.error("Error in deletePM for id: {}", id, e);
            e.printStackTrace();
        }

        return false;
    }

    public boolean updatePM(PaymentMethod paymentMethod) {
        logger.info("Executing updatePM for id: {}", paymentMethod.getId());
        String sql = "UPDATE paymentmethod SET name=?, type=?, description=?, isActive=? "
                + "WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentMethod.getName());
            ps.setString(2, paymentMethod.getType());
            ps.setString(3, paymentMethod.getDescription());
            ps.setInt(4, paymentMethod.getIsActive());
            ps.setInt(5, paymentMethod.getId());

            boolean result = ps.executeUpdate() > 0;
            logger.info("Update payment method status for id {}: {}", paymentMethod.getId(), result);
            return result;

        } catch (Exception e) {
            logger.error("Error in updatePM for id: {}", paymentMethod.getId(), e);
            e.printStackTrace();
        }

        return false;
    }

    public int getPaymentMethodIdByName(String name) {
        logger.info("Executing getPaymentMethodIdByName for name: {}", name);
        String sql = "SELECT id FROM paymentmethod WHERE name = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                logger.info("Successfully found payment method ID {} for name: {}", id, name);
                return id;
            }
            logger.info("No payment method found with name: {}", name);
        } catch (Exception e){
            logger.error("Error in getPaymentMethodIdByName for name: {}", name, e);
            e.printStackTrace();
        }
        return -1;
    }

    public Map<Integer, PaymentMethod> getPMMap() {
        logger.info("Executing getPMMap");
        Map<Integer, PaymentMethod> map = new HashMap<>();
        String sql = "SELECT * FROM paymentmethod";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                PaymentMethod pm = new PaymentMethod(id, rs.getString("name"), rs.getString("type"), rs.getString("description"), rs.getInt("isActive"));
                map.put(id, pm);
            }
            logger.info("Successfully built payment method map with {} entries", map.size());
            return map;
        } catch (Exception e){
            logger.error("Error in getPMMap", e);
            e.printStackTrace();
        }
        return null;
    }
}