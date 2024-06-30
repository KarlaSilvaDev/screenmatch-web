package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.service.translation.MyMemoryQuery;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    private Integer numberOfSeasons;
    private Double rating;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    private String actors;
    private String posterUrl;
    private String plot;
    @Transient
    private List<Episode> episodes = new ArrayList<>();

    public Serie(){}

    public Serie(SerieData serieData){
        this.title = serieData.title();
        this.numberOfSeasons = serieData.numberOfSeasons();
        this.rating = OptionalDouble.of(Double.valueOf(serieData.rating())).orElse(0);
        this.genre = Genre.fromString(serieData.genre().split(",")[0].trim());
        this.actors = serieData.actors();
        this.posterUrl = serieData.posterUrl();
        this.plot = MyMemoryQuery.getTranslation(serieData.plot()).trim();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(Integer numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    @Override
    public String toString() {
        return
                "gênero=" + genre +
                ", título='" + title + '\'' +
                ", número de temporadas=" + numberOfSeasons +
                ", avaliação=" + rating +

                ", atores='" + actors + '\'' +
                ", url do poster='" + posterUrl + '\'' +
                ", sinopse='" + plot + '\'';
    }
}
