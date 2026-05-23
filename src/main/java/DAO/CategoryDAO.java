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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CategoryDAO {

    private static final Logger logger = LoggerFactory.getLogger(CategoryDAO.class);
    private static final String LOG_PREFIX = "[CATEGORY_DAO]";

    public Category getCategoryById(int id) {
        logger.debug("{} Fetching category by ID: {}", LOG_PREFIX, id);
        String query = "SELECT id, categoryName, description, icon FROM category WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    logger.debug("{} Category found for ID: {}", LOG_PREFIX, id);
                    return new Category(
                            rs.getInt("id"),
                            rs.getString("categoryName"),
                            rs.getString("description"),
                            rs.getString("icon")
                    );
                }
                logger.warn("{} No category found with ID: {}", LOG_PREFIX, id);
            }
        } catch (Exception e) {
            logger.error("{} Error retrieving category ID: {}", LOG_PREFIX, id, e);
        }
        return null;
    }

    public List<Category> getAllCategory() {
        logger.info("{} Retrieving all categories", LOG_PREFIX);
        List<Category> list = new ArrayList<Category>();
        String sql = "SELECT * FROM category";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                list.add(new Category(
                        rs.getInt("id"),
                        rs.getString("categoryName"),
                        rs.getString("description"),
                        rs.getString("icon"),
                        rs.getString("categoryCode")
                ));
            }
            logger.debug("{} Successfully retrieved {} categories", LOG_PREFIX, list.size());
        } catch (SQLException e) {
            logger.error("{} Error fetching all categories", LOG_PREFIX, e);
        }
        return list;
    }

    public boolean addCategory(Category category) {
        logger.info("{} Attempting to add new category: {}", LOG_PREFIX, category.getName());
        String sql = "insert into category (categoryName, description, icon, categoryCode) values (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, category.getName());
            stm.setString(2, category.getDescription());
            stm.setString(3, category.getIcon());
            stm.setString(4, category.getCategoryCode());

            boolean success = stm.executeUpdate() > 0;
            if (success) {
                logger.info("{} Successfully added category: {}", LOG_PREFIX, category.getName());
            }
            return success;
        } catch (SQLException e) {
            logger.error("{} Error adding category: {}", LOG_PREFIX, category.getName(), e);
        }
        return false;
    }

    public boolean deleteCategory(int id) {
        logger.warn("{} Attempting to delete category ID: {}", LOG_PREFIX, id);
        String sql = "delete from category where id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);

            boolean success = stm.executeUpdate() > 0;
            if (success) {
                logger.info("{} Successfully deleted category ID: {}", LOG_PREFIX, id);
            } else {
                logger.warn("{} Delete failed. No category found with ID: {}", LOG_PREFIX, id);
            }
            return success;
        } catch (SQLException e) {
            logger.error("{} Error deleting category ID: {}. Check for foreign key constraints!", LOG_PREFIX, id, e);
        }
        return false;
    }

    public boolean updateCategory(Category category) {
        logger.info("{} Updating category ID: {}", LOG_PREFIX, category.getId());
        String sql = "update category set categoryName = ?, description = ?, icon = ? where id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, category.getName());
            stm.setString(2, category.getDescription());
            stm.setString(3, category.getIcon());
            stm.setInt(4, category.getId());

            boolean success = stm.executeUpdate() > 0;
            if (success) {
                logger.info("{} Successfully updated category ID: {}", LOG_PREFIX, category.getId());
            }
            return success;
        } catch (SQLException e) {
            logger.error("{} Error updating category ID: {}", LOG_PREFIX, category.getId(), e);
        }
        return false;
    }

    public boolean hasCategoryName(String name) {
        logger.debug("{} Checking if category name exists: {}", LOG_PREFIX, name);
        String sql = "select 1 from category where categoryName = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, name);
            ResultSet rs = stm.executeQuery();
            boolean exists = rs.next();
            logger.debug("{} Existence check for '{}': {}", LOG_PREFIX, name, exists);
            return exists;
        } catch (Exception e) {
            logger.error("{} Error checking category name existence: {}", LOG_PREFIX, name, e);
        }
        return false;
    }

    public String getCategoryCodeById(int categoryId) {
        logger.debug("{} Fetching category code for ID: {}", LOG_PREFIX, categoryId);
        String sql = "SELECT categoryCode FROM category WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String code = rs.getString("categoryCode");
                logger.debug("{} Code found for ID {}: {}", LOG_PREFIX, categoryId, code);
                return code;
            }
        } catch (Exception e) {
            logger.error("{} Error retrieving category code for ID: {}", LOG_PREFIX, categoryId, e);
        }
        return null;
    }

    public List<Category> getRandom5Categories() {
        logger.info("{} Fetching 5 random categories", LOG_PREFIX);
        List<Category> result = new ArrayList<Category>();
        String sql = "SELECT * FROM category ORDER BY RAND() LIMIT 5";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new Category(
                        rs.getInt("id"),
                        rs.getString("CategoryName"),
                        rs.getString("description"),
                        rs.getString("icon")
                ));
            }
            logger.debug("{} Successfully fetched {} random categories", LOG_PREFIX, result.size());
        } catch (Exception e) {
            logger.error("{} Error fetching random categories", LOG_PREFIX, e);
        }
        return result;
    }
}