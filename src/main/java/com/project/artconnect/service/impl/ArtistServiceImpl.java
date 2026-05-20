package com.project.artconnect.service.impl;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.service.ArtistService;
import java.util.*;
import java.util.stream.Collectors;
import com.project.artconnect.dao.impl.ArtistDaoImpl;

public class ArtistServiceImpl implements ArtistService {
    private final Map<String, Artist> artists = new LinkedHashMap<>();
    private final Map<String, Discipline> disciplines = new LinkedHashMap<>();

    public ArtistServiceImpl() {
        initData();
    }

    private void initData() {
        //Disciplines
		for (Discipline discp : ArtistDaoImpl.getDisciplines()){
			disciplines.put(discp.getName(), discp);
		}

        // Artists
		for (Artist artist : ArtistDaoImpl.findAlll()){
			artists.put(artist.getName(), artist);
		}
    }

    @Override
    public List<Artist> getAllArtists(){
        return new ArrayList<>(artists.values());
    }

    @Override
    public Optional<Artist> getArtistByName(String name){
        return Optional.ofNullable(artists.get(name));
    }

    @Override
    public void createArtist(Artist artist){
        artists.put(artist.getName(), artist);
    }

    @Override
    public void updateArtist(Artist artist){
        artists.put(artist.getName(), artist);
    }

    @Override
    public void deleteArtist(String name){
        artists.remove(name);
    }

    @Override
    public List<Discipline> getAllDisciplines(){
        return new ArrayList<>(disciplines.values());
    }

    @Override
    public List<Artist> searchArtists(String query, String disciplineName, String city) {
        return artists.values().stream()
                .filter(a -> query == null || a.getName().toLowerCase().contains(query.toLowerCase()))
                .filter(a -> city == null || city.isEmpty() || a.getCity().equalsIgnoreCase(city))
                .filter(a -> disciplineName == null
                        || a.getDisciplines().stream().anyMatch(d -> d.getName().equals(disciplineName)))
                .collect(Collectors.toList());
    }
}
