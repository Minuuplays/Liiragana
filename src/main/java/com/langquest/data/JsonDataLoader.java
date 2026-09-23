package com.langquest.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.langquest.model.Gana;
import com.langquest.model.Word;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JsonDataLoader {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Gana> loadHiragana() {
        return loadJson("/com/langquest/data/hiragana.json", new TypeReference<List<Gana>>() {});
    }

    public static List<Word> loadWords() {
        return loadJson("/com/langquest/data/words.json", new TypeReference<List<Word>>() {});
    }

    private static <T> T loadJson(String resourcePath, TypeReference<T> typeRef) {
        try (InputStream is = JsonDataLoader.class.getResourceAsStream(resourcePath)) {
            return mapper.readValue(is, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + resourcePath, e);
        }
    }
}