package com.project.artconnect.dao.impl;

import com.project.artconnect.model.Exhibition;
import java.util.List;
import com.project.artconnect.dao.ExhibitionDao;
import com.project.artconnect.util.ConnectionManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class ExhibitionDaoImpl implements ExhibitionDao {
    public List<Exhibition> findAll(){
		
		Connection con = ConnectionManager.getConnection();
		
		String query = "select title, start_date, end_date, description, curator_name, theme, gallery_id from Exhibition";

		try (PreparedStatement pstm = con.prepareStatement(query)) {
			
			ResultSet res = pstm.executeQuery(query);
			
			List<Exhibition> exhebits = new ArrayList<>();

			while (res.next()) {

				Exhibition resex = new Exhibition();
				
				resex.setTitle(res.getString(1));
				resex.setStartDate(res.getDate(2).toLocalDate());
				resex.setEndDate(res.getDate(3).toLocalDate());
				resex.setDescription(res.getString(4));
				resex.setCuratorName(res.getString(5));
				resex.setTheme(res.getString(6));

				//resex.setGallery //gallery_id INT NOT NULL,
				//resex.setArtworks
				
				exhebits.add(resex);
			}
			
			return exhebits;
			
		} catch (SQLException e) {
			System.out.println(e.getErrorCode() + e.getSQLState() + e.getMessage());
			return new ArrayList<>();
		}
	
	/*
    private Gallery gallery;
    private List<Artwork> artworks = new ArrayList<>();
		
		Exhibition_Artwork (
		exhibition_id INT NOT NULL,
    artwork_id INT NOT NULL,
	*/	
		
	}

    public void save(Exhibition exhibition){
		throw new UnsupportedOperationException();
	}

    public void update(Exhibition exhibition){
		throw new UnsupportedOperationException();
	}

    public void delete(String title){
		throw new UnsupportedOperationException();
	}
}
