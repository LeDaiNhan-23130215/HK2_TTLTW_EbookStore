package DAO;

import models.News;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO {

    public News getNewsById(int id) {
        String sql = "SELECT * FROM news WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<News> getAllNews() {
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean addNews(News news) {
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

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteNews(int id) {
        String sql = "DELETE FROM news WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateNews(News news) {
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

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
