package com.project.artconnect.persistence;

import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcCommunityMemberDao {

    public List<CommunityMember> findAll() {
        List<CommunityMember> members = new ArrayList<>();

        String sql = "SELECT * FROM CommunityMember";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                members.add(mapMember(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }

    public CommunityMember findByName(String name) {
        String sql = "SELECT * FROM CommunityMember WHERE name = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapMember(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private CommunityMember mapMember(ResultSet rs) throws SQLException {
        CommunityMember member = new CommunityMember();

        member.setName(rs.getString("name"));
        member.setEmail(rs.getString("email"));
        member.setBirthYear(rs.getObject("birth_year", Integer.class));
        member.setPhone(rs.getString("phone"));
        member.setCity(rs.getString("city"));
        member.setMembershipType(rs.getString("membership_type"));

        return member;
    }
}