package com.project.artconnect.dao.impl;

import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Discipline;
import java.util.List;
import java.util.Optional;
import com.project.artconnect.dao.CommunityMemberDao;
import com.project.artconnect.util.ConnectionManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class CommunityMemberDaoImpl implements CommunityMemberDao {
    public Optional<CommunityMember> findById(Long id){
		
		Connection con = ConnectionManager.getConnection();
		
		String query = "select name, email, birth_year, phone, city, membership_type, member_id from CommunityMember where member_id = ?";

		try (PreparedStatement pstm = con.prepareStatement(query)) {
			pstm.setInt(1, Math.toIntExact(id));
			ResultSet res = pstm.executeQuery();

			while (res.next()) {
				
				CommunityMember rescom = new CommunityMember();
				
				rescom.setName(res.getString(1));
				rescom.setEmail(res.getString(2));
				rescom.setBirthYear(res.getInt(3));
				rescom.setPhone(res.getString(4));
				rescom.setCity(res.getString(5));
				rescom.setMembershipType(res.getString(6));
				
				/*
				rescom.setFavoriteDisciplines(FavoriteDisciplines(res.getInt(7)));
				rescom.setBookings();
				rescom.setReviews();
				*/
				
				return Optional.of(rescom);
			}
		} catch (SQLException e) {
			System.out.println(e.getErrorCode() + e.getSQLState() + e.getMessage());
			return Optional.empty();
		}
		
		return Optional.empty();

	
	
	/*
	For the time being we do not populate the 2 fields. (Bookings and reviews)
	
	
    private List<Review> reviews = new ArrayList<>();
	Review (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    artwork_id INT NOT NULL,
    rating INT NOT NULL,
    comment TEXT,
    review_date DATE NOT NULL,
	*/	
	}
	
	public static List<Discipline> FavoriteDisciplines(int id){
		
		Connection con = ConnectionManager.getConnection();
		
		String query = "select name from Discipline d inner join Member_Favorite_Discipline m where member_id = ?";

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
	
	/*
	public List<Booking> findBookings(int id){
		
		Connection con = ConnectionManager.getConnection();
		
		String query = "select name from Discipline d inner join Member_Favorite_Discipline m where member_id = ?";

		List<CommunityMember> members = new ArrayList<>();

		try (PreparedStatement pstm = con.prepareStatement(query)) {
			pstm.setInt(1, id);
			ResultSet res = pstm.executeQuery();
			
			List<Booking> bookings = new ArrayList<>();

			while (res.next()) {
				
				Booking resb = new Booking();
				
				resb.
				private List<Booking> bookings = new ArrayList<>();
	Booking (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    workshop_id INT NOT NULL,
    member_id INT NOT NULL,
    booking_date DATETIME NOT NULL,
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
	private Workshop workshop;
    private CommunityMember member;
    private LocalDateTime bookingDate;
    private String paymentStatus; // PENDING, PAID, CANCELLED
				
				bookings.add(resb);
				
			}
			
			return disciplines;
			
		} catch (SQLException e) {
			System.out.println(e.getErrorCode() + e.getSQLState() + e.getMessage());
			return new ArrayList<>();
		}
	}*/
	
	public List<CommunityMember> findAll(){
		throw new UnsupportedOperationException();
	}

    public static List<CommunityMember> findAlll(){
		Connection con = ConnectionManager.getConnection();
		
		String query = "select name, email, birth_year, phone, city, membership_type, member_id from CommunityMember";

		List<CommunityMember> members = new ArrayList<>();

		try (PreparedStatement pstm = con.prepareStatement(query)) {
			ResultSet res = pstm.executeQuery();

			while (res.next()) {
				
				CommunityMember rescom = new CommunityMember();
				
				rescom.setName(res.getString(1));
				rescom.setEmail(res.getString(2));
				rescom.setBirthYear(res.getInt(3));
				rescom.setPhone(res.getString(4));
				rescom.setCity(res.getString(5));
				rescom.setMembershipType(res.getString(6));
				
				rescom.setFavoriteDisciplines(FavoriteDisciplines(res.getInt(7)));
				
				/*
				rescom.setBookings();
				rescom.setReviews();
				*/
				
				members.add(rescom);
				
			}
			
			return members;
			
		} catch (SQLException e) {
			System.out.println(e.getErrorCode() + e.getSQLState() + e.getMessage());
			return new ArrayList<>();
		}
	}
}
