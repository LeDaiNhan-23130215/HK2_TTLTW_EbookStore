package DAO;

import models.Banner;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BannerDAO {

    private static final Logger logger = LoggerFactory.getLogger(BannerDAO.class);
    private static final String LOG_PREFIX = "[BANNER_DAO]";

    public Banner getBannerById(int id) {
        logger.debug("{} Fetching banner by ID: {}", LOG_PREFIX, id);
        String query = "SELECT * FROM banner WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    logger.debug("{} Banner found for ID: {}", LOG_PREFIX, id);
                    return new Banner(rs.getInt("id"),
                            rs.getString("url"),
                            rs.getString("position"),
                            rs.getString("startDate"),
                            rs.getString("endDate"),
                            rs.getInt("isActive")
                    );
                }
                logger.warn("{} No banner found with ID: {}", LOG_PREFIX, id);
            }
        } catch (Exception e) {
            logger.error("{} Error retrieving banner ID: {}", LOG_PREFIX, id, e);
        }
        return null;
    }

    public List<Banner> getAllBanner() {
        logger.info("{} Retrieving all banners", LOG_PREFIX);
        List<Banner> list = new ArrayList<>();
        String sql = "SELECT * FROM banner";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                list.add(new Banner(rs.getInt("id"),
                        rs.getString("url"),
                        rs.getString("position"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getInt("isActive")
                ));
            }
            logger.debug("{} Successfully retrieved {} banners", LOG_PREFIX, list.size());
        } catch (SQLException e) {
            logger.error("{} Error fetching all banners", LOG_PREFIX, e);
        }
        return list;
    }

    public boolean addBanner(Banner banner) {
        logger.info("{} Adding new banner at position: {}", LOG_PREFIX, banner.getPosition());
        String sql = "insert into banner (url, position, startDate, endDate, isActive) values (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, banner.getUrl());
            stm.setString(2, banner.getPosition());
            stm.setString(3, banner.getStartDate());
            stm.setString(4, banner.getEndDate());
            stm.setInt(5, banner.getIsActive());

            boolean success = stm.executeUpdate() > 0;
            if (success) logger.info("{} Banner added successfully", LOG_PREFIX);
            return success;
        } catch (SQLException e) {
            logger.error("{} Error adding banner", LOG_PREFIX, e);
        }
        return false;
    }

    public boolean deleteBanner(int id) {
        logger.warn("{} Attempting to delete banner ID: {}", LOG_PREFIX, id);
        String sql = "delete from banner where id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            boolean success = stm.executeUpdate() > 0;
            if (success) {
                logger.info("{} Successfully deleted banner ID: {}", LOG_PREFIX, id);
            } else {
                logger.warn("{} Delete failed. No banner found with ID: {}", LOG_PREFIX, id);
            }
            return success;
        } catch (SQLException e) {
            logger.error("{} Error deleting banner ID: {}", LOG_PREFIX, id, e);
        }
        return false;
    }

    public boolean updateBanner(Banner banner) {
        logger.info("{} Updating banner ID: {}", LOG_PREFIX, banner.getId());
        String sql = "update banner set url = ?, position = ?, startDate = ?, endDate = ?, isActive = ? where id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, banner.getUrl());
            stm.setString(2, banner.getPosition());
            stm.setString(3, banner.getStartDate());
            stm.setString(4, banner.getEndDate());
            stm.setInt(5, banner.getIsActive());
            stm.setInt(6, banner.getId());

            boolean success = stm.executeUpdate() > 0;
            if (success) logger.info("{} Successfully updated banner ID: {}", LOG_PREFIX, banner.getId());
            return success;
        } catch (SQLException e) {
            logger.error("{} Error updating banner ID: {}", LOG_PREFIX, banner.getId(), e);
        }
        return false;
    }

    public Banner getBannerByLocation(String pos) {
        logger.debug("{} Searching for active banner at position: {}", LOG_PREFIX, pos);
        String sql = """
        SELECT *
        FROM banner
        WHERE position = ?
          AND isActive = 1
          AND startDate <= NOW()
          AND (endDate IS NULL OR endDate >= NOW())
        ORDER BY createdAt DESC
        LIMIT 1
        """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {

            stm.setString(1, pos);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                logger.info("{} Active banner found for position: {}", LOG_PREFIX, pos);
                return new Banner(
                        rs.getInt("id"),
                        rs.getString("url"),
                        rs.getString("position"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getInt("isActive")
                );
            }
            logger.debug("{} No active banner available for position: {}", LOG_PREFIX, pos);
        } catch (SQLException e) {
            logger.error("{} Error finding banner by location: {}", LOG_PREFIX, pos, e);
        }
        return null;
    }
}