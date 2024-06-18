package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.EpisodeData;
import br.com.alura.screenmatch.model.SeasonData;
import br.com.alura.screenmatch.model.SerieData;
import br.com.alura.screenmatch.service.ApiConsumer;
import br.com.alura.screenmatch.service.DataConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private ApiConsumer apiConsumer = new ApiConsumer();
    private DataConverter converter = new DataConverter();
    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    public void showMenu(){
        var menu = """
                1 - Buscar séries
                2 - Buscar episódios
                
                0 - Sair
                """;
        System.out.println(menu);
        var option = scanner.nextInt();
        scanner.nextLine();

        switch (option){
            case 1:
                searchSerie();
                break;
            case 2:
                searchSerieEpisodes();
                break;
            case 0:
                System.out.println("Saindo...");
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private void searchSerie(){
        SerieData data = getSerieData();
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
}
