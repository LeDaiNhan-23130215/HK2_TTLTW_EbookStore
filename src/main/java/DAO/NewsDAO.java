package DAO;

import models.News;
import utils.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO {

    private static final Logger logger = LoggerFactory.getLogger(NewsDAO.class);
    private static final String LOG_PREFIX = "[NEWS_DAO]";

    public News getNewsById(int id) {
        logger.info("{} Executing getNewsById for id: {}", LOG_PREFIX, id);
        String sql = "SELECT * FROM news WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                logger.info("{} Successfully fetched news for id: {}", LOG_PREFIX,id);
                return new News(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("imgURL"),
                        rs.getString("author"),
                        rs.getString("publishedAt"),
                        rs.getString("createdAt"),
                        rs.getInt("status")
                );
            }
            logger.info("{} No news found for id: {}", LOG_PREFIX, id);

        } catch (Exception e) {
            logger.error("{} Error in getNewsById for id: {}", LOG_PREFIX, id, e);
        }

        return null;
    }

    public List<News> getAllNews() {
        logger.info("{} Executing getAllNews", LOG_PREFIX);
        List<News> list = new ArrayList<>();
        String sql = "SELECT * FROM news ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                News n = new News(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("imgURL"),
                        rs.getString("author"),
                        rs.getString("publishedAt"),
                        rs.getString("createdAt"),
                        rs.getInt("status")
                );
                list.add(n);
            }
            logger.info("{} Successfully fetched {} news records", LOG_PREFIX, list.size());

        } catch (Exception e) {
            logger.error("{} Error in getAllNews", LOG_PREFIX, e);
        }
        return list;
    }

    public boolean addNews(News news) {
        logger.info("{} Executing addNews for title: {}", LOG_PREFIX, news.getTitle());
        String sql = "INSERT INTO news (title, content, imgURL, author, publishedAt, createdAt, status) "
                + "VALUES (?, ?, ?, ?, ?, NOW(), ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, news.getTitle());
            ps.setString(2, news.getContent());
            ps.setString(3, news.getImgURL());
            ps.setString(4, news.getAuthor());
            ps.setString(5, news.getPublishedAt());
            ps.setInt(6, news.getStatus());

            boolean result = ps.executeUpdate() > 0;
            logger.info("{} Add news status for title '{}': {}", LOG_PREFIX, news.getTitle(), result);
            return result;

        } catch (Exception e) {
            logger.error("{} Error in addNews for title: {}", LOG_PREFIX, news.getTitle(), e);
        }

        return false;
    }

    public boolean deleteNews(int id) {
        logger.info("{} Executing deleteNews for id: {}", LOG_PREFIX, id);
        String sql = "DELETE FROM news WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            boolean result = ps.executeUpdate() > 0;
            logger.info("{} Delete news status for id {}: {}", LOG_PREFIX, id, result);
            return result;

        } catch (Exception e) {
            logger.error("{} Error in deleteNews for id: {}", LOG_PREFIX, id, e);
        }

        return false;
    }

    public boolean updateNews(News news) {
        logger.info("{} Executing updateNews for id: {}", LOG_PREFIX, news.getId());
        String sql = "UPDATE news SET title=?, content=?, imgURL=?, author=?, publishedAt=?, status=? "
                + "WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, news.getTitle());
            ps.setString(2, news.getContent());
            ps.setString(3, news.getImgURL());
            ps.setString(4, news.getAuthor());
            ps.setString(5, news.getPublishedAt());
            ps.setInt(6, news.getStatus());
            ps.setInt(7, news.getId());

            boolean result = ps.executeUpdate() > 0;
            logger.info("{} Update news status for id {}: {}", LOG_PREFIX, news.getId(), result);
            return result;

        } catch (Exception e) {
            logger.error("{} Error in updateNews for id: {}", LOG_PREFIX, news.getId(), e);
        }
        return false;
    }
}