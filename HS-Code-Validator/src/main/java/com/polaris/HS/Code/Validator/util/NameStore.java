package com.polaris.HS.Code.Validator.util;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NameStore {

    private static final Map<String, String> store = new ConcurrentHashMap<>();


    public static void storeName(String memoryId, String name) {
        store.put(memoryId, name);
    }

    public static String getName(String memoryId) {
        return store.get(memoryId);
    }

    public static boolean hasName(String memoryId) {
        return store.containsKey(memoryId);
    }
}