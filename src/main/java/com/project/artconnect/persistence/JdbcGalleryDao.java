package com.project.artconnect.persistence;

import com.project.artconnect.model.Gallery;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcGalleryDao {

    public List<Gallery> findAll() {
        List<Gallery> galleries = new ArrayList<>();

        String sql = "SELECT * FROM Gallery";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                galleries.add(mapGallery(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return galleries;
    }

    public Gallery findByName(String name) {
        String sql = "SELECT * FROM Gallery WHERE name = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapGallery(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Gallery mapGallery(ResultSet rs) throws SQLException {
        Gallery gallery = new Gallery();

        gallery.setName(rs.getString("name"));
        gallery.setAddress(rs.getString("address"));
        gallery.setOwnerName(rs.getString("owner_name"));
        gallery.setOpeningHours(rs.getString("opening_hours"));
        gallery.setContactPhone(rs.getString("contact_phone"));
        gallery.setRating(rs.getDouble("rating"));
        gallery.setWebsite(rs.getString("website"));

        return gallery;
    }
}