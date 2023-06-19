package com.apexon.compass.clientdashboardservice.utils;

import com.google.gson.Gson;
import org.bson.Document;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ClientDashboardServiceUtils {

    private ClientDashboardServiceUtils() {
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public static String documentsToJsonConverter(List<Document> documents) {
        Gson gson = new Gson();
        return gson.toJson(documents);
    }

}
