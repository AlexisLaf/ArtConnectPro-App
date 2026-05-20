package com.project.artconnect.dao.impl;

import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.ArtworkTag;
import java.util.List;
import com.project.artconnect.dao.ArtworkDao;
import com.project.artconnect.util.ConnectionManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class ArtworkDaoImpl implements ArtworkDao {
    public List<Artwork> findAll(){
		Connection con = ConnectionManager.getConnection();
		
		String query = "select title, creation_year, type, medium, dimensions, description, price, status, artist_id , artwork_id from Artwork";

		try (PreparedStatement pstm = con.prepareStatement(query)) {
			
			ResultSet res = pstm.executeQuery(query);
			
			List<Artwork> Artworks = new ArrayList<>();

			while (res.next()) {
				
				Artwork resaw = new Artwork();
				
				resaw.setTitle(res.getString(1));
				resaw.setCreationYear(res.getInt(2));
				resaw.setType(res.getString(3));
				resaw.setMedium(res.getString(4));
				resaw.setDimensions(res.getString(5));
				resaw.setDescription(res.getString(6));
				resaw.setPrice(res.getDouble(7));
				resaw.setStatus(res.getString(8));
				
				resaw.setArtist(ArtistDaoImpl.findById(res.getInt(9)));
				
				resaw.setTags(getTags(res.getInt(10)));
				
				Artworks.add(resaw);

			}
			
			return Artworks;
			
		} catch (SQLException e) {
			System.out.println(e.getErrorCode() + e.getSQLState() + e.getMessage());
			return new ArrayList<>();
		}
		
	}

    public void save(Artwork artwork){
		throw new UnsupportedOperationException();
	}

    public void update(Artwork artwork){
		throw new UnsupportedOperationException();
	}

    public void delete(String title){
		throw new UnsupportedOperationException();
	}

    public List<Artwork> findByArtistName(String artistName){
		Connection con = ConnectionManager.getConnection();
		
		String query = "select a.title, a.creation_year, a.type, a.medium, a.dimensions, a.description, a.price, a.status, a.artist_id , artwork_id from Artwork a left join vw_artist_overview v on a.artist_id = v.artist_id where v.name = ? ";

		try (PreparedStatement pstm = con.prepareStatement(query)) {
			pstm.setString(1, artistName);
			ResultSet res = pstm.executeQuery(query);
			
			List<Artwork> Artworks = new ArrayList<>();

			while (res.next()) {
				
				Artwork resaw = new Artwork();
				
				resaw.setTitle(res.getString(1));
				resaw.setCreationYear(res.getInt(2));
				resaw.setType(res.getString(3));
				resaw.setMedium(res.getString(4));
				resaw.setDimensions(res.getString(5));
				resaw.setDescription(res.getString(6));
				resaw.setPrice(res.getDouble(7));
				resaw.setStatus(res.getString(8));
				
				resaw.setArtist(ArtistDaoImpl.findById(res.getInt(9)));
				
				resaw.setTags(getTags(res.getInt(10)));
				
				Artworks.add(resaw);

			}
			
			return Artworks;
			
		} catch (SQLException e) {
			System.out.println(e.getErrorCode() + e.getSQLState() + e.getMessage());
			return new ArrayList<>();
		}
	}
	
	private static List<ArtworkTag> getTags(int id){
		
		Connection con = ConnectionManager.getConnection();
		
		String query = "select name from ArtworkTag tag inner join Artwork_Tag a on tag.tag_id = a.tag_id where a.artwork_id = ?";

		try (PreparedStatement pstm = con.prepareStatement(query)) {
			pstm.setInt(1, id);
			ResultSet res = pstm.executeQuery(query);
			
			List<ArtworkTag> tags = new ArrayList<>();

			while (res.next()) {
				
				ArtworkTag tag = new ArtworkTag();
				
				tag.setName(res.getString(1));
				
				tags.add(tag);

			}
			
			return tags;
			
		} catch (SQLException e) {
			System.out.println(e.getErrorCode() + e.getSQLState() + e.getMessage());
			return new ArrayList<>();
		}
			
		
		
	}
}
