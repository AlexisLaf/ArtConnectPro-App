package com.project.artconnect.persistence;

import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcArtistDao implements ArtistDao {

    @Override
    public List<Artist> findAll() {
        List<Artist> artists = new ArrayList<>();

        String sql = "SELECT * FROM Artist";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                artists.add(mapArtist(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return artists;
    }

    @Override
    public void save(Artist artist) {
        String sql = """
                INSERT INTO Artist
                (name, bio, birth_year, contact_email, phone, city, website, social_media, is_active)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, artist.getName());
            stmt.setString(2, artist.getBio());

            if (artist.getBirthYear() == null) {
                stmt.setNull(3, Types.INTEGER);
            } else {
                stmt.setInt(3, artist.getBirthYear());
            }

            stmt.setString(4, artist.getContactEmail());
            stmt.setString(5, artist.getPhone());
            stmt.setString(6, artist.getCity());
            stmt.setString(7, artist.getWebsite());
            stmt.setString(8, artist.getSocialMedia());
            stmt.setBoolean(9, artist.isActive());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Artist artist) {
        String sql = """
                UPDATE Artist
                SET bio = ?, birth_year = ?, contact_email = ?, phone = ?, city = ?,
                    website = ?, social_media = ?, is_active = ?
                WHERE name = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, artist.getBio());

            if (artist.getBirthYear() == null) {
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(2, artist.getBirthYear());
            }

            stmt.setString(3, artist.getContactEmail());
            stmt.setString(4, artist.getPhone());
            stmt.setString(5, artist.getCity());
            stmt.setString(6, artist.getWebsite());
            stmt.setString(7, artist.getSocialMedia());
            stmt.setBoolean(8, artist.isActive());
            stmt.setString(9, artist.getName());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String artistName) {
        String sql = "DELETE FROM Artist WHERE name = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, artistName);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Artist> findByCity(String city) {
        List<Artist> artists = new ArrayList<>();

        String sql = "SELECT * FROM Artist WHERE city = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, city);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    artists.add(mapArtist(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return artists;
    }

    public List<Discipline> findAllDisciplines() {
        List<Discipline> disciplines = new ArrayList<>();

        String sql = "SELECT * FROM Discipline";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                disciplines.add(new Discipline(rs.getString("name")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return disciplines;
    }

    private Artist mapArtist(ResultSet rs) throws SQLException {
        Artist artist = new Artist();

        artist.setName(rs.getString("name"));
        artist.setBio(rs.getString("bio"));
        artist.setBirthYear(rs.getObject("birth_year", Integer.class));
        artist.setContactEmail(rs.getString("contact_email"));
        artist.setPhone(rs.getString("phone"));
        artist.setCity(rs.getString("city"));
        artist.setWebsite(rs.getString("website"));
        artist.setSocialMedia(rs.getString("social_media"));
        artist.setActive(rs.getBoolean("is_active"));

        return artist;
    }
}