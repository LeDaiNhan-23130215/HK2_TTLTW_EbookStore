package DAO;

import models.Banner;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BannerDAO {
    public Banner getBannerById(int id) {
        String query = "SELECT * FROM banner WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Banner(rs.getInt("id"),
                            rs.getString("url"),
                            rs.getString("position"),
                            rs.getString("startDate"),
                            rs.getString("endDate"),
                            rs.getInt("isActive")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Banner> getAllBanner() {
        List<Banner> list = new ArrayList<Banner>();
        String sql = "SELECT * FROM banner";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                Banner b = new Banner(rs.getInt("id"),
                        rs.getString("url"),
                        rs.getString("position"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getInt("isActive")
                );
                list.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean addBanner(Banner banner) {
        String sql = "insert into banner (url, position, startDate, endDate, isActive) values (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, banner.getUrl());
            stm.setString(2, banner.getPosition());
            stm.setString(3, banner.getStartDate());
            stm.setString(4, banner.getEndDate());
            stm.setInt(5, banner.getIsActive());

            int rows = stm.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteBanner(int id) {
        String sql = "delete from banner where id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            int rows = stm.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateBanner(Banner banner) {
        String sql = "update banner set url = ?, position = ?, startDate = ?, endDate = ?, isActive = ? where id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, banner.getUrl());
            stm.setString(2, banner.getPosition());
            stm.setString(3, banner.getStartDate());
            stm.setString(4, banner.getEndDate());
            stm.setInt(5, banner.getIsActive());
            stm.setInt(6, banner.getId());
            int rows = stm.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Banner getBannerByLocation(String pos) {
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
        Banner banner = null;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {

            stm.setString(1, pos);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                int id =rs.getInt("id");
                String url =rs.getString("url");
                System.out.print(url);
                String position =rs.getString("position");
                String startDate = rs.getString("startDate");
                String endDate = rs.getString("endDate");
                int isActive = rs.getInt("isActive");
                banner = new Banner(id, url, position, startDate, endDate, isActive);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return banner;
    }
}
