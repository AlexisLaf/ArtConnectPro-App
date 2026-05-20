package com.project.artconnect.service.impl;

import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.persistence.JdbcExhibitionDao;
import com.project.artconnect.persistence.JdbcGalleryDao;
import com.project.artconnect.service.GalleryService;

import java.util.List;
import java.util.Optional;

public class JdbcGalleryService implements GalleryService {

    private final JdbcGalleryDao galleryDao = new JdbcGalleryDao();
    private final JdbcExhibitionDao exhibitionDao = new JdbcExhibitionDao();

    @Override
    public List<Gallery> getAllGalleries() {
        return galleryDao.findAll();
    }

    @Override
    public Optional<Gallery> getGalleryByName(String name) {
        return Optional.ofNullable(galleryDao.findByName(name));
    }

    @Override
    public List<Exhibition> getExhibitionsByGallery(Gallery gallery) {
        if (gallery == null) {
            return List.of();
        }

        return exhibitionDao.findByGalleryName(gallery.getName());
    }
}