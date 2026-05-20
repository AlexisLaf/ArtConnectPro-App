package com.project.artconnect.service.impl;

import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Review;
import com.project.artconnect.persistence.JdbcCommunityMemberDao;
import com.project.artconnect.service.CommunityService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class JdbcCommunityService implements CommunityService {

    private final JdbcCommunityMemberDao memberDao = new JdbcCommunityMemberDao();

    @Override
    public List<CommunityMember> getAllMembers() {
        return memberDao.findAll();
    }

    @Override
    public Optional<CommunityMember> getMemberByName(String name) {
        return Optional.ofNullable(memberDao.findByName(name));
    }

    @Override
    public List<Review> getReviewsByMember(CommunityMember member) {
        return Collections.emptyList();
    }
}