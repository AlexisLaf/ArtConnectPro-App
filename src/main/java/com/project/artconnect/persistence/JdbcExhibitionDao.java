package com.project.artconnect.persistence;

import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcExhibitionDao {

    public List<Exhibition> findByGalleryName(String galleryName) {
        List<Exhibition> exhibitions = new ArrayList<>();

        String sql = """
                SELECT e.*, g.name AS gallery_name, g.address, g.rating
                FROM Exhibition e
                JOIN Gallery g ON e.gallery_id = g.gallery_id
                WHERE g.name = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, galleryName);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    exhibitions.add(mapExhibition(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exhibitions;
    }

    public List<Exhibition> findAll() {
        List<Exhibition> exhibitions = new ArrayList<>();

        String sql = """
                SELECT e.*, g.name AS gallery_name, g.address, g.rating
                FROM Exhibition e
                JOIN Gallery g ON e.gallery_id = g.gallery_id
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                exhibitions.add(mapExhibition(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exhibitions;
    }

    private Exhibition mapExhibition(ResultSet rs) throws SQLException {
        Gallery gallery = new Gallery();
        gallery.setName(rs.getString("gallery_name"));
        gallery.setAddress(rs.getString("address"));
        gallery.setRating(rs.getDouble("rating"));

        Exhibition exhibition = new Exhibition();
        exhibition.setTitle(rs.getString("title"));

        Date startDate = rs.getDate("start_date");
        if (startDate != null) {
            exhibition.setStartDate(startDate.toLocalDate());
        }

        Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            exhibition.setEndDate(endDate.toLocalDate());
        }

        exhibition.setDescription(rs.getString("description"));
        exhibition.setCuratorName(rs.getString("curator_name"));
        exhibition.setTheme(rs.getString("theme"));
        exhibition.setGallery(gallery);

        return exhibition;
    }
}