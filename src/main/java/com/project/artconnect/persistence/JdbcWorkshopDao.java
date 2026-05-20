package com.project.artconnect.persistence;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcWorkshopDao {

    public List<Workshop> findAll() {
        List<Workshop> workshops = new ArrayList<>();

        String sql = """
                SELECT w.*, a.name AS instructor_name
                FROM Workshop w
                JOIN Artist a ON w.instructor_artist_id = a.artist_id
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                workshops.add(mapWorkshop(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workshops;
    }

    public Workshop findByTitle(String title) {

        String sql = """
                SELECT w.*, a.name AS instructor_name
                FROM Workshop w
                JOIN Artist a ON w.instructor_artist_id = a.artist_id
                WHERE w.title = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapWorkshop(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Workshop mapWorkshop(ResultSet rs) throws SQLException {

        Artist instructor = new Artist();
        instructor.setName(rs.getString("instructor_name"));

        Workshop workshop = new Workshop();

        workshop.setTitle(rs.getString("title"));

        Timestamp timestamp = rs.getTimestamp("date");

        if (timestamp != null) {
            workshop.setDate(timestamp.toLocalDateTime());
        }

        workshop.setDurationMinutes(rs.getInt("duration_minutes"));
        workshop.setMaxParticipants(rs.getInt("max_participants"));
        workshop.setPrice(rs.getDouble("price"));
        workshop.setInstructor(instructor);
        workshop.setLocation(rs.getString("location"));
        workshop.setDescription(rs.getString("description"));
        workshop.setLevel(rs.getString("level"));

        return workshop;
    }
}