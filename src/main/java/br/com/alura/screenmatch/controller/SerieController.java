package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.EpisodeDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {
    @Autowired
    private SerieService service;

    @GetMapping
    public List<SerieDTO> getAllSeries(){
        return service.getAllSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> getTop5Series(){
        return service.getTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> getReleases(){
        return service.getReleases();
    }

    @GetMapping("/{id}")
    public SerieDTO getSerieDetailsById(@PathVariable Long id){
        return service.getDetailsById(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodeDTO> getAllSeasons(@PathVariable Long id){
        return service.getAllSeasons(id);
    }

    @GetMapping("/{id}/temporadas/{temporada}")
    public List<EpisodeDTO> getSeasonEpisodes(@PathVariable Long id, @PathVariable("temporada") Long seasonNumber){
        return service.getSeasonEpisodes(id, seasonNumber);
    }

    @GetMapping("{id}/temporadas/top")
    public List<EpisodeDTO> getTop5Episodes(@PathVariable Long id){
        return service.getTop5Episodes(id);
    }

    @GetMapping("/categoria/{categoria}")
    public List<SerieDTO> getSeriesByGenre(@PathVariable("categoria") String genreName){
        return service.getSeriesByGenre(genreName);
    }


}
