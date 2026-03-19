package DAO;

import models.Category;
import models.Ebook;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    public Category getCategoryById (int id) {
        String query = "SELECT id, categoryName, description, icon FROM category WHERE id = ?";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);){
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Category(
                            rs.getInt("id"),
                            rs.getString("categoryName"),
                            rs.getString("description"),
                            rs.getString("icon")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Category> getAllCategory() {
        List<Category> list = new ArrayList<Category>();
        String sql = "SELECT * FROM category";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                Category c = new Category(
                        rs.getInt("id"),
                        rs.getString("categoryName"),
                        rs.getString("description"),
                        rs.getString("icon"),
                        rs.getString("categoryCode")
                );
                list.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean addCategory(Category category){
        String sql = "insert into category (categoryName, description, icon, categoryCode) values (?, ?, ?, ?)";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement stm = connection.prepareStatement(sql)){
            stm.setString(1, category.getName());
            stm.setString(2, category.getDescription());
            stm.setString(3, category.getIcon());
            stm.setString(4, category.getCategoryCode());
            int rows = stm.executeUpdate();
            return rows > 0;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCategory(int id){
        String sql = "delete from category where id = ?";
        try (Connection connection = DBConnection.getConnection();
        PreparedStatement stm = connection.prepareStatement(sql)){
            stm.setInt(1, id);
            int rows = stm.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCategory(Category category){
        String sql = "update category set categoryName = ?, description = ?, icon = ? where id = ?";
        try (Connection connection = DBConnection.getConnection();
        PreparedStatement stm = connection.prepareStatement(sql)){
            stm.setString(1, category.getName());
            stm.setString(2, category.getDescription());
            stm.setString(3, category.getIcon());
            stm.setInt(4, category.getId());
            int rows = stm.executeUpdate();
            return rows > 0;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasCategoryName(String name) {
        String sql = "select * from category where categoryName = ?";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, name);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getCategoryCodeById(int categoryId) {
        String sql = "SELECT categoryCode FROM category WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("categoryCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Category> getRandom5Categories() {
        List<Category> result = new ArrayList<Category>();
        String sql = "SELECT * FROM category ORDER BY RAND() LIMIT 5";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("CategoryName");
                String description = rs.getString("description");
                String icon = rs.getString("icon");
                Category category = new Category(id, name, description, icon);
                result.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
