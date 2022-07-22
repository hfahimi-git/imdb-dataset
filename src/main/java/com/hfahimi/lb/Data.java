package com.hfahimi.lb;

import java.util.Map;
import java.util.concurrent.ExecutionException;
public class Data {
    private static Map<String, String> titlesForSameLiveDirectorWriter;
    private static Map<String, String> actors;
    private static Map<String, String> allNames;
    private static Data instant;
    private static Map<String, String> titles;

    private Data() throws ExecutionException, InterruptedException {
        var task = new Assignment();
        task.loadData();
        titlesForSameLiveDirectorWriter = task.getTitlesForSameLiveDirectorWriter();
        actors = task.getActors();
        titles = task.getTitles();
        allNames = task.getAllNames();
    }

    public static Map<String, String> getTitles() {
        return titles;
    }

    public static Map<String, String> getTitlesForSameLiveDirectorWriter() {
        return titlesForSameLiveDirectorWriter;
    }

    public static Map<String, String> getActors() {
        return actors;
    }

    public static Map<String, String> getAllNames() {
        return allNames;
    }

    public static void build() throws ExecutionException, InterruptedException {
        if(instant == null)
            instant = new Data();
    }

}
