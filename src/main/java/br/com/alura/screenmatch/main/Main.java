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

//        System.out.println("Top 10 Episódios:");
//        episodesData.stream()
//                .filter(e -> !e.rating().equals("N/A"))
//                .peek(e -> System.out.println("Filtro: "+ e))
//                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
//                .peek(e -> System.out.println("Ordenação: "+ e))
//                .limit(10)
//                .peek(e -> System.out.println("Limite: "+ e))
//                .map(e -> e.title().toUpperCase())
//                .peek(e -> System.out.println("Map: "+ e))
//                .forEach(System.out::println);

        List<Episode> episodes = seasons.stream()
                .flatMap(s -> s.episodes().stream()
                        .map(e -> new Episode(s.number(), e))
                ).collect(Collectors.toList());

        episodes.forEach(System.out::println);

        System.out.println("Digite um trecho do título do episódio:");
        var titleExcerpt = scanner.nextLine();

        Optional<Episode> searchedEpisode = episodes.stream()
                .filter(e -> e.getTitle().toUpperCase().contains(titleExcerpt.toUpperCase()))
                .findFirst();
        if(searchedEpisode.isPresent()){
            System.out.println("Episódio encontrado!");
            System.out.println("Temporada: " + searchedEpisode.get().getSeason());
        }else{
            System.out.println("Episódio não encontrado!");
        }

//        System.out.println("Você deseja ver os episódios lançados a partir de que ano?");
//        var year = scanner.nextInt();
//        scanner.nextLine();
//
//        LocalDate searchDate = LocalDate.of(year, 1,1);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodes.stream()
//                .filter(e -> e.getReleaseDate()!= null && e.getReleaseDate().isAfter(searchDate))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getSeason() +
//                                ", Episódio: " + e.getTitle() +
//                                ", Data de lançamento: " + e.getReleaseDate().format(formatter)
//                ));

        Map<Integer, Double> ratingsPerSeason = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.groupingBy(Episode::getSeason, Collectors.averagingDouble(Episode::getRating)));
        System.out.println(ratingsPerSeason);

        DoubleSummaryStatistics est = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.summarizingDouble(Episode::getRating));
        System.out.println("Média: " + est.getAverage());
        System.out.println("Maior nota episódio: " + est.getMax());
        System.out.println("Menor nota episódio: " + est.getMin());
        System.out.println("Total de episódios avaliados: " + est.getCount());
    }
}
