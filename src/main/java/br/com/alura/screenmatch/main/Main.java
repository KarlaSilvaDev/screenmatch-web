package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.SeasonData;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.model.SerieData;
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

    public void showMenu(){
        var option = -1;
        while(option != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                                    
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
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void searchSerieWeb(){
        SerieData data = getSerieData();
        this.serieData.add(data);
        System.out.println(data);
    }

    private SerieData getSerieData(){
        System.out.println("Digite o nome da série para a busca:");
        var serieTitle = scanner.nextLine();
        var json = apiConsumer.getData(ADDRESS + serieTitle.replace(" ", "+") + API_KEY);
        SerieData data = converter.getData(json, SerieData.class);
        return data;
    }

    private void searchSerieEpisodes(){
        SerieData serieData = getSerieData();
        List<SeasonData> seasons = new ArrayList<>();

        for (int i = 1; i <= serieData.numberOfSeasons() ; i++) {
            var json = apiConsumer.getData(ADDRESS + serieData.title().replace(" ", "+") + "&season=" + i + API_KEY);
            SeasonData seasonData = converter.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }

        seasons.forEach(System.out::println);
    }

    private void listSearchedSeries(){
        List<Serie> series = new ArrayList<>();
        series = serieData.stream()
                        .map(d -> new Serie(d))
                                .collect(Collectors.toList());

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);
    }
}
