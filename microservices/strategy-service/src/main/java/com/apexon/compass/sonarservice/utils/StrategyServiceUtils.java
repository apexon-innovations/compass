package com.apexon.compass.sonarservice.utils;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bson.Document;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StrategyServiceUtils {

    public static String documentsToJsonConverter(List<Document> documents) {
        Gson gson = new Gson();
        return gson.toJson(documents);
    }

}
