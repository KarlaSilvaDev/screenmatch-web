package br.com.alura.screenmatch.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodes")
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer season;
    private String title;
    private Integer episodeNumber;
    private Double rating;
    private LocalDate releaseDate;
    @ManyToOne
    private Serie serie;

    public Episode(){};

    public Episode(Integer seasonNumber, EpisodeData episodeData) {
        this.season = seasonNumber;
        this.title = episodeData.title();
        this.episodeNumber = episodeData.number();
        try {
            this.rating = Double.valueOf(episodeData.rating());
        }catch (NumberFormatException e){
            this.rating = 0d;
        }
        try{
            this.releaseDate = LocalDate.parse(episodeData.releaseDate());
        }catch (DateTimeParseException e){
            this.releaseDate = null;
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    @Override
    public String toString() {
        return  "Temporada:" + season +
                ", Título: '" + title + '\'' +
                ", Número do episódio: " + episodeNumber +
                ", Avaliação: " + rating +
                ", Data de Lançamento: " + releaseDate;
    }
}
