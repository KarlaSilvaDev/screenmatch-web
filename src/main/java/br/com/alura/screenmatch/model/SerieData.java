package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SerieData(@JsonAlias("Title") String title,
                        @JsonAlias("totalSeasons") Integer numberOfSeasons,
                        @JsonAlias("imdbRating") String rating,
                        @JsonAlias("Genre") String genre,
                        @JsonAlias("Actors") String actors,
                        @JsonAlias("Poster") String posterUrl,
                        @JsonAlias("Plot") String plot
                        ) {}