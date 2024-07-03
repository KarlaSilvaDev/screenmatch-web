package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.Genre;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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

    //O código abaixo pode trazer a mesma série do banco de dados se ela tiver os 5 episódios mais recentes. Por isso, não utilizaremos derivedQuerie
    //List<Serie> findTop5ByOrderByEpisodesReleaseDateDesc();

    @Query("SELECT s FROM Serie s JOIN s.episodes e GROUP BY s ORDER BY MAX(e.releaseDate) DESC LIMIT 5")
    List<Serie> findMostRecentEpisodes();

    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE s.id = :id AND e.season = :seasonNumber")
    List<Episode> getEpisodes(Long id, Long seasonNumber);
}
