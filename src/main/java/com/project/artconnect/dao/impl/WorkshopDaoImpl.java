package com.project.artconnect.dao.impl;

import com.project.artconnect.dao.WorkshopDao;
import com.project.artconnect.model.Workshop;
import java.util.List;
import java.util.Optional;
import java.lang.Math;
import com.project.artconnect.util.ConnectionManager;
import com.project.artconnect.dao.impl.ArtistDaoImpl;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class WorkshopDaoImpl implements WorkshopDao {
    public Optional<Workshop> findById(Long id){
		Connection con = ConnectionManager.getConnection();
		
		String query = "select title, date, duration_minutes, max_participants, price, location, description, level , instructor_artist_id from Workshop where workshop_id = ?";

		try (PreparedStatement pstm = con.prepareStatement(query)) {
			pstm.setInt(1, Math.toIntExact(id));
			ResultSet res = pstm.executeQuery(query);
			
			

			while (res.next()) {
				
				Workshop resws = new Workshop();
	
				resws.setTitle(res.getString(1));
				resws.setDate(res.getTimestamp(2).toLocalDateTime());
				resws.setDurationMinutes(res.getInt(3));
				resws.setMaxParticipants(res.getInt(4));
    			resws.setPrice(res.getDouble(5));
				resws.setLocation(res.getString(6));
				resws.setDescription(res.getString(7));
				resws.setLevel(res.getString(8));

				int instructor_artist_id = res.getInt(9);
				
				resws.setInstructor(ArtistDaoImpl.findById(instructor_artist_id));
	
				//The next line can be done inside the while loop since we only expect one single line to be returned.
				res.close();
				return Optional.of(resws);
			}
		} catch (SQLException e) {
			System.out.println(e.getErrorCode() + e.getSQLState() + e.getMessage());
			return Optional.empty();
		}
		return Optional.empty();
	}

    public List<Workshop> findAll(){
		Connection con = ConnectionManager.getConnection();
		
		String query = "select title, date, duration_minutes, max_participants, price, location, description, level , instructor_artist_id from Workshop";

		//Will automatically call res.close() after finishing the try clause.	
		try (PreparedStatement pstm = con.prepareStatement(query)) {
			ResultSet res = pstm.executeQuery(query);
			
			List<Workshop> Workshops = new ArrayList<>();

			while (res.next()) {
				
				Workshop resws = new Workshop();
	
				resws.setTitle(res.getString(1));
				resws.setDate(res.getTimestamp(2).toLocalDateTime());
				resws.setDurationMinutes(res.getInt(3));
				resws.setMaxParticipants(res.getInt(4));
    			resws.setPrice(res.getDouble(5));
				resws.setLocation(res.getString(6));
				resws.setDescription(res.getString(7));
				resws.setLevel(res.getString(8));

				int instructor_artist_id = res.getInt(9);
				
				resws.setInstructor(ArtistDaoImpl.findById(instructor_artist_id));
				
				Workshops.add(resws);
				
			}
			res.close();
			return Workshops;
			
		} catch (SQLException e) {
			System.out.println(e.getErrorCode() + e.getSQLState() + e.getMessage());
			return new ArrayList<>();
		}
	}
}
