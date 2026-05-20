package com.project.artconnect.service.impl;

import com.project.artconnect.model.Workshop;
import com.project.artconnect.model.Booking;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.service.WorkshopService;
import com.project.artconnect.service.ArtistService;
import java.time.LocalDateTime;
import java.util.*;
import com.project.artconnect.dao.impl.WorkshopDaoImpl;

public class WorkshopServiceImpl implements WorkshopService {
    private final Map<String, Workshop> workshops = new LinkedHashMap<>();

    public WorkshopServiceImpl() {
    }

    public void initData(ArtistService artistService) {
		for (Artist artist : artistService.getAllArtists()){
			for (Workshop wshop : WorkshopDaoImpl.findbyInstructorname(artist.getName())){
				wshop.setInstructor(artist);
				workshops.put(wshop.getTitle(), wshop);
			}
		}
    }

    private void addWorkshop(String title, LocalDateTime date, Artist instructor, double price, String level,
            String location) {
        if (instructor == null)
            return;
        Workshop w = new Workshop(title, date, instructor, price);
        w.setLevel(level);
        w.setLocation(location);
        w.setDurationMinutes(180);
        w.setMaxParticipants(10);
        workshops.put(title, w);
    }

    @Override
    public List<Workshop> getAllWorkshops() {
        return new ArrayList<>(workshops.values());
    }

    @Override
    public Optional<Workshop> getWorkshopByTitle(String title) {
        return Optional.ofNullable(workshops.get(title));
    }

    @Override
    public void bookWorkshop(Workshop workshop, CommunityMember member) {
        if (workshop == null || member == null)
            return;
        Booking b = new Booking(workshop, member);
        member.addBooking(b);
    }

    @Override
    public List<Booking> getBookingsByMember(CommunityMember member) {
        if (member == null)
            return Collections.emptyList();
        return member.getBookings();
    }
}
