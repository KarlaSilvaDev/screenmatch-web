package br.com.alura.screenmatch.service.translation;

import br.com.alura.screenmatch.service.ApiConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;

public class MyMemoryQuery {
    public static String getTranslation(String text) {
        ObjectMapper mapper = new ObjectMapper();

        ApiConsumer consumer = new ApiConsumer();

        String langpair = URLEncoder.encode("en|pt-br");

        String url = "https://api.mymemory.translated.net/get?q=" + URLEncoder.encode(text) + "&langpair=" + langpair;

        String json = consumer.getData(url);

        TranslationData translation;
        try {
            translation = mapper.readValue(json, TranslationData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return translation.responseData().translatedText();
    }
}
