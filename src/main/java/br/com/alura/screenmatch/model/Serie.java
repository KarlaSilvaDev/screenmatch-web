package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.service.translation.MyMemoryQuery;

import java.util.OptionalDouble;

public class Serie {
    private String title;
    private Integer numberOfSeasons;
    private Double rating;
    private Genre genre;
    private String actors;
    private String posterUrl;
    private String plot;

    public Serie(SerieData serieData){
        this.title = serieData.title();
        this.numberOfSeasons = serieData.numberOfSeasons();
        this.rating = OptionalDouble.of(Double.valueOf(serieData.rating())).orElse(0);
        this.genre = Genre.fromString(serieData.genre().split(",")[0].trim());
        this.posterUrl = serieData.posterUrl();
        this.plot = MyMemoryQuery.getTranslation(serieData.plot()).trim();
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
