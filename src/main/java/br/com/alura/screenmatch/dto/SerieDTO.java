package br.com.alura.screenmatch.dto;

import br.com.alura.screenmatch.model.Genre;

public record SerieDTO(Long id,
                       String title,
                       Integer numberOfSeasons,
                       Double rating,
                       Genre genre,
                       String actors,
                       String posterUrl,
                       String plot) {
}
