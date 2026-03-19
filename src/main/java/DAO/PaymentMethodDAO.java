package DAO;

import models.PaymentMethod;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentMethodDAO {
    public PaymentMethod getPMById(int id) {
        String sql = "SELECT * FROM paymentmethod WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new PaymentMethod(id, rs.getString("name"), rs.getString("type"), rs.getString("description"), rs.getInt("isActive"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<PaymentMethod> getAllPMs() {
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean addPM(PaymentMethod paymentMethod) {
        String sql = "INSERT INTO paymentmethod (name, type, description, isActive)"
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentMethod.getName());
            ps.setString(2, paymentMethod.getType());
            ps.setString(3, paymentMethod.getDescription());
            ps.setInt(4, paymentMethod.getIsActive());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deletePM(int id) {
        String sql = "DELETE FROM paymentmethod WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updatePM(PaymentMethod paymentMethod) {
        String sql = "UPDATE paymentmethod SET name=?, type=?, description=?, isActive=? "
                + "WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentMethod.getName());
            ps.setString(2, paymentMethod.getType());
            ps.setString(3, paymentMethod.getDescription());
            ps.setInt(4, paymentMethod.getIsActive());
            ps.setInt(5, paymentMethod.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int getPaymentMethodIdByName(String name) {
        String sql = "SELECT id FROM paymentmethod WHERE name = ?";
        try (Connection connection = DBConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public Map<Integer, PaymentMethod> getPMMap() {
        Map<Integer, PaymentMethod> list = new HashMap<>();
        String sql = "SELECT * FROM paymentmethod";
        try(Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                PaymentMethod pm = new PaymentMethod(id, rs.getString("name"), rs.getString("type"), rs.getString("description"), rs.getInt("isActive"));
                list.put(id, pm);
            }
            return list;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
