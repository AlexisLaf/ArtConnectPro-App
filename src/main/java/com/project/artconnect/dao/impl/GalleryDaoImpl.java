package com.project.artconnect.dao.impl;

import com.project.artconnect.model.Gallery;
import java.util.List;
import java.util.Optional;
import com.project.artconnect.dao.GalleryDao;
import com.project.artconnect.util.ConnectionManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class GalleryDaoImpl implements GalleryDao {
    public Optional<Gallery> findById(Long id){
		Connection con = ConnectionManager.getConnection();
		
		String query = "select name, address, owner_name, opening_hours, contact_phone, rating, website from Gallery where gallery_id = ?";

		try (PreparedStatement pstm = con.prepareStatement(query)) {
			pstm.setInt(1, Math.toIntExact(id));
			ResultSet res = pstm.executeQuery(query);

			while (res.next()) {
				
				Gallery resgal = new Gallery();
				
				resgal.setName(res.getString(1));
				resgal.setAddress(res.getString(2));
				resgal.setOwnerName(res.getString(3));
				resgal.setOpeningHours(res.getString(4));
				resgal.setContactPhone(res.getString(5));
				resgal.setRating(res.getDouble(6));
				resgal.setWebsite(res.getString(7));
				
				//resgal.setExhibitions addExhibition
				//private List<Exhibition> exhibitions = new ArrayList<>();

				return Optional.of(resgal);
			}
		} catch (SQLException e) {
			System.out.println(e.getErrorCode() + e.getSQLState() + e.getMessage());
			return Optional.empty();
		}
		
		return Optional.empty();
		
	}
	
	public List<Gallery> findAll(){
		throw new UnsupportedOperationException();
	}

    public static List<Gallery> findAlll(){
		Connection con = ConnectionManager.getConnection();
		
		String query = "select name, address, owner_name, opening_hours, contact_phone, rating, website from Gallery, gallery_id";

		try (PreparedStatement pstm = con.prepareStatement(query)) {
			
			ResultSet res = pstm.executeQuery(query);
			
			List<Gallery> galleries = new ArrayList<>();

			while (res.next()) {
				
				Gallery resgal = new Gallery();
				
				resgal.setName(res.getString(1));
				resgal.setAddress(res.getString(2));
				resgal.setOwnerName(res.getString(3));
				resgal.setOpeningHours(res.getString(4));
				resgal.setContactPhone(res.getString(5));
				resgal.setRating(res.getDouble(6));
				resgal.setWebsite(res.getString(7));
				
				int gal_id = res.getInt(8);
				//resgal.setExhibitions addExhibition
				//private List<Exhibition> exhibitions = new ArrayList<>();

				galleries.add(resgal);
			}
			
			return galleries;
			
		} catch (SQLException e) {
			System.out.println(e.getErrorCode() + e.getSQLState() + e.getMessage());
			return new ArrayList<>();
		}
	}
}
