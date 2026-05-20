package com.project.artconnect.dao.impl;

import com.project.artconnect.model.CommunityMember;
import java.util.List;
import java.util.Optional;
import com.project.artconnect.dao.CommunityMemberDao;
import com.project.artconnect.util.ConnectionManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class CommunityMemberDaoImpl implements CommunityMemberDao {
    public Optional<CommunityMember> findById(Long id){
		
		Connection con = ConnectionManager.getConnection();
		
		String query = "select name, email, birth_year, phone, city, membership_type member_id from CommunityMember where member_id = ?";

		try (PreparedStatement pstm = con.prepareStatement(query)) {
			pstm.setInt(1, Math.toIntExact(id));
			ResultSet res = pstm.executeQuery(query);

			while (res.next()) {
				
				CommunityMember rescom = new CommunityMember();
				
				rescom.setName(res.getString(1));
				rescom.setEmail(res.getString(2));
				rescom.setBirthYear(res.getInt(3));
				rescom.setPhone(res.getString(4));
				rescom.setCity(res.getString(5));
				rescom.setMembershipType(res.getString(6));
				
				/*
				rescom.setFavoriteDisciplines();
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
	For the time being we do not populate the 3 fields.
	
	private List<Discipline> favoriteDisciplines = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
	
	Member_Favorite_Discipline (
    member_id INT NOT NULL,
    discipline_id INT NOT NULL,
	
	Booking (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    workshop_id INT NOT NULL,
    member_id INT NOT NULL,
    booking_date DATETIME NOT NULL,
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
	
	Review (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    artwork_id INT NOT NULL,
    rating INT NOT NULL,
    comment TEXT,
    review_date DATE NOT NULL,
	*/	
	}

    public List<CommunityMember> findAll(){
		throw new UnsupportedOperationException();
	}
}
