package com.project.artconnect.service.impl;

import com.project.artconnect.model.Gallery;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.service.GalleryService;
import com.project.artconnect.service.ArtworkService;
import java.time.LocalDate;
import java.util.*;
import com.project.artconnect.dao.impl.GalleryDaoImpl;
import com.project.artconnect.dao.impl.ExhibitionDaoImpl;


public class GalleryServiceImpl implements GalleryService {
    private final Map<String, Gallery> galleries = new LinkedHashMap<>();

    public GalleryServiceImpl() {
        // initData after other services if needed, but Gallery is top-level
    }

    public void initData(ArtworkService artworkService) {
        for (Gallery tmpgalleries : GalleryDaoImpl.findAlll()){
			galleries.put(tmpgalleries.getName(),tmpgalleries);
		} //Exhibitions are added inside the gallery creations.
    }

    @Override
    public List<Gallery> getAllGalleries() {
        return new ArrayList<>(galleries.values());
    }

    @Override
    public Optional<Gallery> getGalleryByName(String name) {
        return Optional.ofNullable(galleries.get(name));
    }

    @Override
    public List<Exhibition> getExhibitionsByGallery(Gallery gallery) {
        if (gallery == null)
            return Collections.emptyList();
        return gallery.getExhibitions();
    }
}
