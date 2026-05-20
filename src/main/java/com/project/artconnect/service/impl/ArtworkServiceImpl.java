package com.project.artconnect.service.impl;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.service.ArtworkService;
import java.util.*;
import com.project.artconnect.dao.impl.ArtworkDaoImpl;

public class ArtworkServiceImpl implements ArtworkService {
    private final Map<String, Artwork> artworks = new LinkedHashMap<>();

    public ArtworkServiceImpl() {
        // Data initialized after ArtistService is ready
    }

    public void initData(ArtistService artistService) {
		ArtworkDaoImpl tmp = new ArtworkDaoImpl();
		
		for (Artist artist : artistService.getAllArtists()){
			for (Artwork work : tmp.findByArtistName(artist.getName())){
				work.setArtist(artist);
				artworks.put(work.getTitle(),work);
			}
		}
    }

    @Override
    public List<Artwork> getAllArtworks() {
        return new ArrayList<>(artworks.values());
    }

    @Override
    public Optional<Artwork> getArtworkByTitle(String title) {
        return Optional.ofNullable(artworks.get(title));
    }

    @Override
    public List<Artwork> getArtworksByArtist(Artist artist) {
        if (artist == null)
            return Collections.emptyList();
        return artist.getArtworks();
    }

    @Override
    public void createArtwork(Artwork artwork) {
        artworks.put(artwork.getTitle(), artwork);
    }

    @Override
    public void updateArtwork(Artwork artwork) {
        artworks.put(artwork.getTitle(), artwork);
    }

    @Override
    public void deleteArtwork(String title) {
        artworks.remove(title);
    }
}
