package br.com.alura.screenmatch.model;

public enum Genre {
    ACAO("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comédia"),
    DRAMA("Drama", "Drama"),
    CRIME("Crime", "Crime"),
    AVENTURA("Adventure", "Aventura" );

    private String omdbGenre;
    private String genreInPortuguese;


    Genre(String omdbGenre, String genreInPortuguese){
        this.omdbGenre = omdbGenre;
        this.genreInPortuguese = genreInPortuguese;
    }

    public static Genre fromString(String text) {
        for (Genre genre : Genre.values()) {
            if (genre.omdbGenre.equalsIgnoreCase(text)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

    public static Genre fromPortuguese(String text) {
        for (Genre genre : Genre.values()) {
            if (genre.genreInPortuguese.equalsIgnoreCase(text)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}



