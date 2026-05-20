package com.project.artconnect.dao.impl;

import com.project.artconnect.model.Artist;
import java.util.List;
import java.util.Optional;
import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.util.ConnectionManager;
import com.project.artconnect.dao.impl.ArtworkDaoImpl;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import com.project.artconnect.model.Discipline;

/**
 * Data Access Object for Artist entity.
 */
public class ArtistDaoImpl implements ArtistDao {
    public static List<Artist> findAlll(){
		Connection con = ConnectionManager.getConnection();
		
		String query = "select name, bio, birth_year, contact_email, phone, city, website, social_media, is_active, artist_id from Artist";

		//Will automatically call res.close() after finishing the try clause.	
		try (PreparedStatement pstm = con.prepareStatement(query)) {
			ResultSet res = pstm.executeQuery(query);
			
			List<Artist> Artists = new ArrayList<>();

			while (res.next()) {
				
				Artist resA = new Artist();
				
				resA.setName(res.getString(1));
				resA.setBio(res.getString(2));
				resA.setBirthYear(res.getInt(3));
				resA.setContactEmail(res.getString(4));
				resA.setPhone(res.getString(5));
				resA.setCity(res.getString(6));
				resA.setWebsite(res.getString(7));
				resA.setSocialMedia(res.getString(8));
				resA.setActive(res.getBoolean(9));
				
				// Fill the list with other dao implementations.
				resA.setDisciplines(getDiscipline(res.getInt(10)));
				
				ArtworkDaoImpl tmp = new ArtworkDaoImpl();
				resA.setArtworks(tmp.findByArtistName(resA.getName()));
				
				Artists.add(resA);
				
			}
			res.close();
			return Artists;
			
		} catch (SQLException e) {
			System.out.println(e.getErrorCode() + e.getSQLState() + e.getMessage());
			return new ArrayList<>();
		}
		
	}

    public void save(Artist artist){
		throw new UnsupportedOperationException();
	}

    public void update(Artist artist){
		throw new UnsupportedOperationException();
	}

    public void delete(String artistName){
		Connection con = ConnectionManager.getConnection();
		
		String query = "DELETE FROM Artist where name = ?";

		try (PreparedStatement pstm = con.prepareStatement(query)) {
			pstm.setString(1, artistName);
			int res = pstm.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getErrorCode() + e.getSQLState() + e.getMessage());
			return ;
		}
		
		return ;
	}
	
	public List<Artist> findAll(){
		throw new UnsupportedOperationException();
	}

    public List<Artist> findByCity(String city){
		throw new UnsupportedOperationException();
	}
	
	public static Artist findById(int id){
		Connection con = ConnectionManager.getConnection();
		
		String query = "select name, bio, birth_year, contact_email, phone, city, website, social_media, is_active from Artist where artist_id = ?";

		try (PreparedStatement pstm = con.prepareStatement(query)) {
			pstm.setInt(1, id);
			ResultSet res = pstm.executeQuery();

			while (res.next()) {
				
				Artist resA = new Artist();
				
				resA.setName(res.getString(1));
				resA.setBio(res.getString(2));
				resA.setBirthYear(res.getInt(3));
				resA.setContactEmail(res.getString(4));
				resA.setPhone(res.getString(5));
				resA.setCity(res.getString(6));
				resA.setWebsite(res.getString(7));
				resA.setSocialMedia(res.getString(8));
				resA.setActive(res.getBoolean(9));
				
				// Fill the list with other dao implementations.
				resA.setDisciplines(getDiscipline(id));
				
				ArtworkDaoImpl tmp = new ArtworkDaoImpl();
				resA.setArtworks(tmp.findByArtistName(resA.getName()));
				
				//The next line can be done inside the while loop since we only expect one single line to be returned.
				res.close();
				return resA;
			}
		} catch (SQLException e) {
			System.out.println(e.getErrorCode() + e.getSQLState() + e.getMessage());
			return new Artist();
		}
		
		return new Artist();
		
	
	}
	
	public static List<Discipline> getDiscipline(int id){
		Connection con = ConnectionManager.getConnection();
		
		String query = "select d.name from Discipline d inner join Artist_Discipline a on d.discipline_id = a.discipline_id where artist_id = ? ";

		try (PreparedStatement pstm = con.prepareStatement(query)) {
			pstm.setInt(1, id);
			
			ResultSet res = pstm.executeQuery();
			
			List<Discipline> disciplines = new ArrayList<>();

			while (res.next()) {
				
				Discipline resD = new Discipline();
				
				resD.setName(res.getString(1));
				
				disciplines.add(resD);

			}
			
			return disciplines;
			
		} catch (SQLException e) {
			System.out.println(e.getErrorCode() + e.getSQLState() + e.getMessage());
			return new ArrayList<>();
		}
	}
	
	public static List<Discipline> getDisciplines(){
		Connection con = ConnectionManager.getConnection();
		
		String query = "select name from Discipline";

		try (PreparedStatement pstm = con.prepareStatement(query)) {
			
			ResultSet res = pstm.executeQuery();
			
			List<Discipline> disciplines = new ArrayList<>();

			while (res.next()) {
				Discipline resD = new Discipline();
				
				resD.setName(res.getString(1));
				
				disciplines.add(resD);

			}
			
			return disciplines;
			
		} catch (SQLException e) {
			System.out.println(e.getErrorCode() + e.getSQLState() + e.getMessage());
			return new ArrayList<>();
		}
	}
}
