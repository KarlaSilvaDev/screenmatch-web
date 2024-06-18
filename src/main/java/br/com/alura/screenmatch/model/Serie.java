package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;

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
        this.plot = serieData.plot();
    }
}
