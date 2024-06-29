package br.com.alura.screenmatch.service.translation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TranslationData(ResponseData responseData) {
}