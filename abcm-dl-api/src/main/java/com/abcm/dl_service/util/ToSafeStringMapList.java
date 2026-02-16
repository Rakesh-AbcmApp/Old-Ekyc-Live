package com.abcm.dl_service.util;

import org.json.JSONArray;
import java.util.*;

public class ToSafeStringMapList {
    public static List<Map<String, String>> covSafeStringMapList(JSONArray jsonArray) {
        if (jsonArray == null || jsonArray.length() == 0) return Collections.emptyList();
        List<Map<String, String>> resultList = new ArrayList<>();
        jsonArray.toList().stream()
            .filter(obj -> obj instanceof Map)
            .map(obj -> (Map<?, ?>) obj)
            .map(rawMap -> {
                Map<String, String> stringMap = new HashMap<>();
                rawMap.forEach((k, v) -> stringMap.put(String.valueOf(k), v != null ? String.valueOf(v) : ""));
                return stringMap;
            })
            .forEach(resultList::add);

        return resultList;
    }
}
