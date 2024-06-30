package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.SeasonData;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.model.SerieData;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ApiConsumer;
import br.com.alura.screenmatch.service.DataConverter;

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
                    searchTop5Series();
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
        Optional<Serie> searchedSeries = repository.findByTitleContainingIgnoreCase(serieTitle);

        if (searchedSeries.isPresent()) {
            System.out.println("Dados da série: " + searchedSeries.get());
        } else {
            System.out.println("Série não encontrada.");
        }
    }

    private void findSeriesByActor(){
        System.out.println("Digite o nome do ator ou atriz: ");
        var actorName = scanner.nextLine();
        System.out.println("Avaliações a partir de que valor?");
        var rating = scanner.nextDouble();
        List<Serie> series = repository.findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(actorName, rating);
        System.out.println("Séries em que "+ actorName +" trabalhou: ");
        series.forEach(s ->
                System.out.println(s.getTitle() + " - avaliação: " + s.getRating())
        );
    }

    private void searchTop5Series(){
        List<Serie> top5Series = repository.findTop5ByOrderByRatingDesc();
        top5Series.forEach(s ->
                System.out.println(s.getTitle() + " - avaliação: " + s.getRating()));
    }
}