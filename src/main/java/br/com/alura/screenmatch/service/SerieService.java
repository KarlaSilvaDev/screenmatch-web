package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodeDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.Genre;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {
    @Autowired
    private SerieRepository repository;

    private List<SerieDTO> convertData(List<Serie> series){
        return series.stream()
                .map(s -> new SerieDTO(
                        s.getId(),
                        s.getTitle(),
                        s.getNumberOfSeasons(),
                        s.getRating(),
                        s.getGenre(),
                        s.getActors(),
                        s.getPosterUrl(),
                        s.getPlot()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> getAllSeries(){
        return convertData(repository.findAll());
    }

    public List<SerieDTO> getTop5Series() {
        return convertData(repository.findTop5ByOrderByRatingDesc());
    }

    public List<SerieDTO> getReleases() {
        return convertData(repository.findMostRecentEpisodes());
    }

    public SerieDTO getDetailsById(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()){
            Serie s = serie.get();
            return new SerieDTO(s.getId(),
                                s.getTitle(),
                                s.getNumberOfSeasons(),
                                s.getRating(),
                                s.getGenre(),
                                s.getActors(),
                                s.getPosterUrl(),
                                s.getPlot());
        }
        return null;
    }

    public List<EpisodeDTO> getAllSeasons(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodes().stream()
                    .map(e -> new EpisodeDTO(e.getSeason(), e.getEpisodeNumber(), e.getTitle()))
                    .collect(Collectors.toList());
        }

        return null;
    }


    public List<EpisodeDTO> getSeasonEpisodes(Long id, Long seasonNumber) {
        return repository.getEpisodes(id, seasonNumber).stream()
                .map(e -> new EpisodeDTO(e.getSeason(), e.getEpisodeNumber(), e.getTitle()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> getSeriesByGenre(String genreName) {
        Genre genre = Genre.fromPortuguese(genreName);
        return convertData(repository.findByGenre(genre));
    }

    public List<EpisodeDTO> getTop5Episodes(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {
            List<Episode> top5SerieEpisodes = repository.findTop5SerieEpisodes(serie.get());
            return top5SerieEpisodes.stream()
                    .map(e -> new EpisodeDTO(e.getSeason(), e.getEpisodeNumber(), e.getTitle()))
                    .collect(Collectors.toList());
        }

        return null;
    }
}
