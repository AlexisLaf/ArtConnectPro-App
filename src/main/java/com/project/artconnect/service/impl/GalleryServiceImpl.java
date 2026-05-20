package com.project.artconnect.service.impl;

import com.project.artconnect.model.Gallery;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.service.GalleryService;
import com.project.artconnect.service.ArtworkService;
import java.time.LocalDate;
import java.util.*;
import com.project.artconnect.dao.impl.GalleryDaoImpl;

public class GalleryServiceImpl implements GalleryService {
    private final Map<String, Gallery> galleries = new LinkedHashMap<>();

    public GalleryServiceImpl() {
        // initData after other services if needed, but Gallery is top-level
    }

    public void initData(ArtworkService artworkService) {
        for (Gallery tmpgalleries : GalleryDaoImpl.findAlll()){
			galleries.put(tmpgalleries.getName(),tmpgalleries);
		}
		

        // Add Exhibitions
		/*
        addExhibition("Renaissance Revival", LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(2), louvre,
                "Dr. Elena Rossi", "Classic Renaissance",
                artworkService.getArtworkByTitle("Mona Lisa").orElse(null),
                artworkService.getArtworkByTitle("The Last Supper").orElse(null));

        addExhibition("Sculpting the Soul", LocalDate.now().minusDays(15), LocalDate.now().plusMonths(1), british,
                "Marcus Thorne", "Modern & Classical Sculpture",
                artworkService.getArtworkByTitle("The Thinker").orElse(null));

        addExhibition("Impressionist Dreams", LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(3), met,
                "Sarah Jenkins", "Light and Color",
                artworkService.getArtworkByTitle("Water Lilies").orElse(null));*/
    }

    private void addExhibition(String title, LocalDate start, LocalDate end, Gallery gallery, String curator,
            String theme, Artwork... artworks) {
        Exhibition e = new Exhibition(title, start, end, gallery);
        e.setCuratorName(curator);
        e.setTheme(theme);
        for (Artwork a : artworks) {
            if (a != null)
                e.getArtworks().add(a);
        }
        gallery.addExhibition(e);
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
