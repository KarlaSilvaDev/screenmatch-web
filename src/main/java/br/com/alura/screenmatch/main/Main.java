package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ApiConsumer;
import br.com.alura.screenmatch.service.DataConverter;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private ApiConsumer apiConsumer = new ApiConsumer();
    private DataConverter converter = new DataConverter();
    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<SerieData> serieData = new ArrayList<>();
    private List<Serie> series = new ArrayList<>();
    private SerieRepository repository;
    private Optional<Serie> searchedSerie;

    public Main(SerieRepository repository) {
        this.repository = repository;
    }

    public void showMenu() {
        var option = -1;
        while (option != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar série por título
                    5 - Buscar séries por ator
                    6 - Top 5 séries
                    7 - Buscar séries por categoria
                    8 - Filtrar séries
                    9 - Buscar episódio por trecho
                    10 - Top 5 episódios por série
                    11 - Buscar episódios a partir de uma data
                                                   
                    0 - Sair
                    """;
            System.out.println(menu);
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    searchSerieWeb();
                    break;
                case 2:
                    searchSerieEpisodes();
                    break;
                case 3:
                    listSearchedSeries();
                    break;
                case 4:
                    findSeriesByTitle();
                    break;
                case 5:
                    findSeriesByActor();
                    break;
                case 6:
                    findTop5Series();
                    break;
                case 7:
                    findSeriesByCategory();
                    break;
                case 8:
                    filterSeriesBySeasonsAndRating();
                    break;
                case 9:
                    findEpisodesByExcerpt();
                    break;
                case 10:
                    findTop5SerieEpisodes();
                    break;
                case 11:
                    findEpisodesAfterADate();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void searchSerieWeb() {
        SerieData data = getSerieData();
        Serie serie = new Serie(data);
        repository.save(serie);
        System.out.println(data);
    }

    private SerieData getSerieData() {
        System.out.println("Digite o nome da série para a busca:");
        var serieTitle = scanner.nextLine();
        var json = apiConsumer.getData(ADDRESS + serieTitle.replace(" ", "+") + API_KEY);
        SerieData data = converter.getData(json, SerieData.class);
        return data;
    }

    private void searchSerieEpisodes() {
        listSearchedSeries();
        System.out.println("Escolha uma série pelo nomme: ");
        var serieTitle = scanner.nextLine();

        Optional<Serie> serie = repository.findByTitleContainingIgnoreCase(serieTitle);

        if (serie.isPresent()) {
            Serie serieFound = serie.get();
            List<SeasonData> seasons = new ArrayList<>();

            for (int i = 1; i <= serieFound.getNumberOfSeasons(); i++) {
                var json = apiConsumer.getData(ADDRESS + serieFound.getTitle().replace(" ", "+") + "&season=" + i + API_KEY);
                SeasonData seasonData = converter.getData(json, SeasonData.class);
                seasons.add(seasonData);
            }

            seasons.forEach(System.out::println);

            List<Episode> episodes = seasons.stream()
                    .flatMap(s -> s.episodes().stream()
                            .map(e -> new Episode(s.number(), e)))
                    .collect(Collectors.toList());

            serieFound.setEpisodes(episodes);
            repository.save(serieFound);
        } else {
            System.out.println("Série não encontrada.");
        }
    }

    private void listSearchedSeries() {
        this.series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);
    }

    private void findSeriesByTitle() {
        System.out.println("Escolha uma série pelo nome: ");
        var serieTitle = scanner.nextLine();
        this.searchedSerie = repository.findByTitleContainingIgnoreCase(serieTitle);

        if (searchedSerie.isPresent()) {
            System.out.println("Dados da série: " + searchedSerie.get());
        } else {
            System.out.println("Série não encontrada.");
        }
    }

    private void findSeriesByActor() {
        System.out.println("Digite o nome do ator ou atriz: ");
        var actorName = scanner.nextLine();
        System.out.println("Avaliações a partir de que valor?");
        var rating = scanner.nextDouble();
        List<Serie> series = repository.findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(actorName, rating);
        System.out.println("Séries em que " + actorName + " trabalhou: ");
        series.forEach(s ->
                System.out.println(s.getTitle() + " - avaliação: " + s.getRating())
        );
    }

    private void findTop5Series() {
        List<Serie> top5Series = repository.findTop5ByOrderByRatingDesc();
        top5Series.forEach(s ->
                System.out.println(s.getTitle() + " - avaliação: " + s.getRating()));
    }

    private void findSeriesByCategory() {
        System.out.println("Deseja buscar séries de que categoria/gênero?");
        var genreName = scanner.nextLine();
        Genre genre = Genre.fromPortuguese(genreName);
        List<Serie> seriesByGenre = repository.findByGenre(genre);
        System.out.println("Séries da categoria " + genreName.toLowerCase());
        seriesByGenre.forEach(System.out::println);
    }

    private void filterSeriesBySeasonsAndRating() {
        System.out.println("Filtrar séries até quantas temporadas?");
        var totalSeasons = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Com avaliação a partir de que valor?");
        var ratingMinimumValue = scanner.nextDouble();
        scanner.nextLine();
        //List<Serie> filteredSeries = repository.findByNumberOfSeasonsLessThanEqualAndRatingGreaterThanEqual(totalSeasons, ratingMinimumValue);
        List<Serie> filteredSeries = repository.findSeriesBySeasonAndRating(totalSeasons, ratingMinimumValue);
        System.out.println("Séries filtradas: ");
        filteredSeries.forEach(s ->
                System.out.println(s.getTitle() + " - temporadas: " + s.getNumberOfSeasons() + " - avaliação: " + s.getRating()));
    }

    private void findEpisodesByExcerpt() {
        System.out.println("Qual o nome do episódio para a busca?");
        var episodeTitleExcerpt = scanner.nextLine();
        List<Episode> foundEpisodes = repository.findEpisodesByExcerpt(episodeTitleExcerpt);
        foundEpisodes.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitle(), e.getSeason(),
                        e.getEpisodeNumber(), e.getTitle()));

    }

    private void findTop5SerieEpisodes() {
        findSeriesByTitle();
        if (searchedSerie.isPresent()) {
            Serie serie = searchedSerie.get();
            List<Episode> top5Episodes = repository.findTop5SerieEpisodes(serie);
            top5Episodes.forEach(e ->
                    System.out.printf("Série: %s Temporada: %s - Episódio: %s - Avaliação: %s \n",
                            e.getSerie().getTitle(),
                            e.getSeason(),
                            e.getTitle(),
                            e.getRating()));
        }
    }

    private void findEpisodesAfterADate(){
        findSeriesByTitle();
        if (searchedSerie.isPresent()){
            Serie serie = searchedSerie.get();
            System.out.println("Digite o ano limite do lançamento: ");
            int year = scanner.nextInt();

            List<Episode> filteredEpisodes = repository.findEpisodesAfterADate(serie, year);

            System.out.println("Série: " + serie.getTitle());;
            filteredEpisodes.forEach(e ->
                    System.out.printf("Temporada: %s - Episódio: %s - Data de Lançamento: %s - Avaliação: %s \n",
                            e.getSeason(),
                            e.getTitle(),
                            e.getReleaseDate(),
                            e.getRating()
                            ));
        }
    }
}