package com.project.artconnect.persistence;

import com.project.artconnect.dao.ArtworkDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcArtworkDao implements ArtworkDao {

    @Override
    public List<Artwork> findAll() {
        List<Artwork> artworks = new ArrayList<>();

        String sql = """
                SELECT aw.*, ar.name AS artist_name, ar.bio, ar.birth_year,
                       ar.contact_email, ar.phone, ar.city, ar.website,
                       ar.social_media, ar.is_active
                FROM Artwork aw
                JOIN Artist ar ON aw.artist_id = ar.artist_id
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                artworks.add(mapArtwork(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return artworks;
    }

    @Override
    public void save(Artwork artwork) {
        String sql = """
                INSERT INTO Artwork
                (artist_id, title, creation_year, type, medium, dimensions, description, price, status)
                VALUES (
                    (SELECT artist_id FROM Artist WHERE name = ?),
                    ?, ?, ?, ?, ?, ?, ?, ?
                )
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, artwork.getArtist().getName());
            stmt.setString(2, artwork.getTitle());

            if (artwork.getCreationYear() == null) {
                stmt.setNull(3, Types.INTEGER);
            } else {
                stmt.setInt(3, artwork.getCreationYear());
            }

            stmt.setString(4, artwork.getType());
            stmt.setString(5, artwork.getMedium());
            stmt.setString(6, artwork.getDimensions());
            stmt.setString(7, artwork.getDescription());
            stmt.setDouble(8, artwork.getPrice());
            stmt.setString(9, artwork.getStatus().name());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Artwork artwork) {
        String sql = """
                UPDATE Artwork
                SET creation_year = ?, type = ?, medium = ?, dimensions = ?,
                    description = ?, price = ?, status = ?
                WHERE title = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (artwork.getCreationYear() == null) {
                stmt.setNull(1, Types.INTEGER);
            } else {
                stmt.setInt(1, artwork.getCreationYear());
            }

            stmt.setString(2, artwork.getType());
            stmt.setString(3, artwork.getMedium());
            stmt.setString(4, artwork.getDimensions());
            stmt.setString(5, artwork.getDescription());
            stmt.setDouble(6, artwork.getPrice());
            stmt.setString(7, artwork.getStatus().name());
            stmt.setString(8, artwork.getTitle());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String title) {
        String sql = "DELETE FROM Artwork WHERE title = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Artwork> findByArtistName(String artistName) {
        List<Artwork> artworks = new ArrayList<>();

        String sql = """
                SELECT aw.*, ar.name AS artist_name, ar.bio, ar.birth_year,
                       ar.contact_email, ar.phone, ar.city, ar.website,
                       ar.social_media, ar.is_active
                FROM Artwork aw
                JOIN Artist ar ON aw.artist_id = ar.artist_id
                WHERE ar.name = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, artistName);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    artworks.add(mapArtwork(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return artworks;
    }

    private Artwork mapArtwork(ResultSet rs) throws SQLException {
        Artist artist = new Artist();
        artist.setName(rs.getString("artist_name"));
        artist.setBio(rs.getString("bio"));
        artist.setBirthYear(rs.getObject("birth_year", Integer.class));
        artist.setContactEmail(rs.getString("contact_email"));
        artist.setPhone(rs.getString("phone"));
        artist.setCity(rs.getString("city"));
        artist.setWebsite(rs.getString("website"));
        artist.setSocialMedia(rs.getString("social_media"));
        artist.setActive(rs.getBoolean("is_active"));

        Artwork artwork = new Artwork();
        artwork.setTitle(rs.getString("title"));
        artwork.setCreationYear(rs.getObject("creation_year", Integer.class));
        artwork.setType(rs.getString("type"));
        artwork.setMedium(rs.getString("medium"));
        artwork.setDimensions(rs.getString("dimensions"));
        artwork.setDescription(rs.getString("description"));
        artwork.setPrice(rs.getDouble("price"));
        artwork.setStatus(Artwork.Status.valueOf(rs.getString("status")));
        artwork.setArtist(artist);

        return artwork;
    }
}