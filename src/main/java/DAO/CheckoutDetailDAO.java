package DAO;

import models.CheckoutDetail;
import utils.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CheckoutDetailDAO {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutDetailDAO.class);

    public boolean addCheckoutDetail(Connection con, CheckoutDetail detail) throws SQLException {
        logger.info("Executing addCheckoutDetail for checkoutID: {}, bookID: {}", detail.getCheckoutID(), detail.getBookID());
        String sql =
                "INSERT INTO checkoutdetail (checkoutID, bookID, price) VALUES (?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, detail.getCheckoutID());
            ps.setInt(2, detail.getBookID());
            ps.setDouble(3, detail.getPrice());
            return ps.executeUpdate() > 0;
        }
    }

    public List<CheckoutDetail> getDetailsByCheckoutID(int checkoutID) {
        logger.info("Executing getDetailsByCheckoutID for checkoutID: {}", checkoutID);
        List<CheckoutDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM checkoutdetail WHERE checkoutID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, checkoutID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CheckoutDetail cd = new CheckoutDetail(
                        rs.getInt("id"),
                        rs.getInt("checkoutID"),
                        rs.getInt("bookID"),
                        rs.getDouble("price")
                );
                list.add(cd);
            }
            logger.info("Successfully fetched {} details for checkoutID: {}", list.size(), checkoutID);

        } catch (Exception e) {
            logger.error("Error in getDetailsByCheckoutID for checkoutID: {}", checkoutID, e);
            e.printStackTrace();
        }
        return list;
    }

    public List<Integer> getEbookIdsTopSale() {
        logger.info("Executing getEbookIdsTopSale");
        List<Integer> list = new ArrayList<>();
        String sql = """
                SELECT
                    bookID,
                    COUNT(*) AS total_sold
                FROM checkoutDetail
                GROUP BY bookID
                ORDER BY total_sold DESC
                LIMIT 7;
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("bookID");
                list.add(id);
            }
            logger.info("Successfully fetched {} top sale ebook IDs", list.size());

        } catch (Exception e) {
            logger.error("Error in getEbookIdsTopSale", e);
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        CheckoutDetailDAO cddao = new CheckoutDetailDAO();

        for (int i : cddao.getEbookIdsTopSale()) {
            System.out.println(i);
        }
    }
}