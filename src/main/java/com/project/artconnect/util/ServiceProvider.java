package com.project.artconnect.util;

import com.project.artconnect.service.*;
import com.project.artconnect.service.impl.*;
import com.project.artconnect.dao.impl.*;

import com.project.artconnect.model.Workshop;
import java.util.List;
import java.util.Optional;

/**
 * Service Provider to manage singleton instances of services and handle their
 * initialization.
 */
public class ServiceProvider {
    /*private static final InMemoryArtistService artistService = new InMemoryArtistService();
    private static final InMemoryArtworkService artworkService = new InMemoryArtworkService();
    private static final InMemoryGalleryService galleryService = new InMemoryGalleryService();
    private static final InMemoryWorkshopService workshopService = new InMemoryWorkshopService();
    private static final InMemoryCommunityService communityService = new InMemoryCommunityService();*/
	
	private static final ArtistServiceImpl artistService = new ArtistServiceImpl();
    private static final ArtworkServiceImpl artworkService = new ArtworkServiceImpl();
    private static final GalleryServiceImpl galleryService = new GalleryServiceImpl();
    private static final WorkshopServiceImpl workshopService = new WorkshopServiceImpl();
    private static final CommunityServiceImpl communityService = new CommunityServiceImpl();
	

    static {
        // Initialize services with their dependencies
        artworkService.initData(artistService);
        galleryService.initData(artworkService);
        workshopService.initData(artistService);
        communityService.initData(artworkService);
    }

    public static ArtistService getArtistService() {
        return artistService;
    }

    public static ArtworkService getArtworkService() {
        return artworkService;
    }

    public static GalleryService getGalleryService() {
        return galleryService;
    }

    public static WorkshopService getWorkshopService() {
        return workshopService;
    }

    public static CommunityService getCommunityService() {
        return communityService;
    }
	
	
	/*
	private static final ArtistDaoImpl artistService = new ArtistDaoImpl();
    private static final ArtworkDaoImpl artworkService = new ArtworkDaoImpl();
    private static final GalleryDaoImpl galleryService = new GalleryDaoImpl();
    private static final WorkshopDaoImpl workshopService = new WorkshopDaoImpl();
    private static final CommunityMemberDaoImpl communityService = new CommunityMemberDaoImpl();*/
}
