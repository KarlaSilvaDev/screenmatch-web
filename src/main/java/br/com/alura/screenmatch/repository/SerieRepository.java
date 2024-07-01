package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.Genre;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTitleContainingIgnoreCase(String serieTitle);

    List<Serie> findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(String actorName, double rating);

    List<Serie> findTop5ByOrderByRatingDesc();

    List<Serie> findByGenre(Genre genre);

    List<Serie> findByNumberOfSeasonsLessThanEqualAndRatingGreaterThanEqual(int numberOfSeasons, double rating);
    @Query("SELECT s FROM Serie s WHERE s.numberOfSeasons <= :numberOfSeasons AND s.rating >= :rating")
    List<Serie> findSeriesBySeasonAndRating(int numberOfSeasons, double rating);

    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE e.title ILIKE %:episodeTitleExcerpt%")
    List<Episode> findEpisodesByExcerpt(String episodeTitleExcerpt);

    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE s = :serie ORDER BY e.rating DESC LIMIT 5")
    List<Episode> findTop5SerieEpisodes(Serie serie);

    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE s = :serie AND YEAR(e.releaseDate) >= :releaseDate")
    List<Episode> findEpisodesAfterADate(Serie serie, int releaseDate);
}
