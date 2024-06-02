package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.EpisodeData;
import br.com.alura.screenmatch.model.SeasonData;
import br.com.alura.screenmatch.model.SerieData;
import br.com.alura.screenmatch.service.ApiConsumer;
import br.com.alura.screenmatch.service.DataConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private ApiConsumer apiConsumer = new ApiConsumer();
    private DataConverter converter = new DataConverter();
    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    public void showMenu(){
        System.out.println("Digite o nome da série");
        var serieTitle = scanner.nextLine();
        var json = apiConsumer.getData( ADDRESS + serieTitle.replace(" ", "+") + API_KEY);
        SerieData serieData = converter.getData(json, SerieData.class);
        System.out.println(serieData);

        List<SeasonData> seasons = new ArrayList<>();
		for (int i = 1; i <= serieData.numberOfSeasons(); i++){
			json = apiConsumer.getData(ADDRESS + serieTitle.replace(" ", "+") + "&season=" + i + API_KEY);
			SeasonData seasonData = converter.getData(json, SeasonData.class);
			seasons.add(seasonData);
		}

		seasons.forEach(System.out::println);

//        for(int i = 0; i < serieData.numberOfSeasons(); i++){
//            List<EpisodeData> seasonEpisodes = seasons.get(i).episodes();
//            for (int j = 0; j < seasonEpisodes.size(); j++ ){
//                System.out.println(seasonEpisodes.get(j).title());
//            }
//        }

//        seasons.forEach(s -> s.episodes().forEach(e -> System.out.println(e.title())));

        List<EpisodeData> episodesData = seasons.stream()
                .flatMap(s -> s.episodes().stream())
                .collect(Collectors.toList()); //.toList() criaria uma lista imutável

        System.out.println("Top 5 Episódios:");
        episodesData.stream()
                .filter(e -> !e.rating().equals("N/A"))
                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episode> episodes = seasons.stream()
                .flatMap(s -> s.episodes().stream()
                        .map(e -> new Episode(s.number(), e))
                ).collect(Collectors.toList());

        episodes.forEach(System.out::println);

        System.out.println("Você deseja ver os episódios lançados a partir de que ano?");
        var year = scanner.nextInt();
        scanner.nextLine();

        LocalDate searchDate = LocalDate.of(year, 1,1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodes.stream()
                .filter(e -> e.getReleaseDate()!= null && e.getReleaseDate().isAfter(searchDate))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getSeason() +
                                ", Episódio: " + e.getTitle() +
                                ", Data de lançamento: " + e.getReleaseDate().format(formatter)
                ));

    }
}
