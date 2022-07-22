package com.hfahimi.lb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ImdbService {
    @Value("${page.size:10}")
    private int pageSize;
    private List<String> nameCode = null;
    private List<String> primaryName = null;
    private List<String> keysOfTitlesForSameLiveDirectorWriter = null;

    public List<String> titlesWithActors(String actor1, String actor2) {
        //name code, knownForTitles
        Map<String, String> actors = Data.getActors();
        //name code, primaryName
        Map<String, String> allNames = Data.getAllNames();
        if (nameCode == null) {
            nameCode = allNames.keySet().stream().toList();
        }
        if (primaryName == null) {
            primaryName = allNames.values().stream().toList();
        }

        //title code, primaryTitle
        Map<String, String> titles = Data.getTitles();

        int[] posActor1 = IntStream.range(0, primaryName.size()).filter(i -> Objects.equals(primaryName.get(i), actor1)).toArray();
        int[] posActor2 = IntStream.range(0, primaryName.size()).filter(i -> Objects.equals(primaryName.get(i), actor2)).toArray();

        List<String> titles1 = new ArrayList<>();
        for (int i : posActor1) {
            if (actors.get(nameCode.get(i)) != null) {
                titles1.addAll(List.of(actors.get(nameCode.get(i)).split(",")));
            }
        }

        List<String> titles2 = new ArrayList<>();
        for (int i : posActor2) {
            if (actors.get(nameCode.get(i)) != null) {
                titles2.addAll(List.of(actors.get(nameCode.get(i)).split(",")));
            }
        }

        titles1.retainAll(titles2);
        List<String> result = new ArrayList<>(titles1.size());
        for (String t : titles1) {
            result.add(titles.get(t));
        }
        return result;
    }

    public Result titlesWithSameAliveDirectorAndWriter(int page) {
        int from, to, recordCount;
        from = (page - 1) * pageSize;
        to = (page * pageSize) - 1;
        Map<String, String> titlesForSameLiveDirectorWriter = Data.getTitlesForSameLiveDirectorWriter();
        recordCount = titlesForSameLiveDirectorWriter.size();
        int lastPage = recordCount % pageSize > 0 ? (recordCount / pageSize) + 1 : recordCount / pageSize;
        if (keysOfTitlesForSameLiveDirectorWriter == null) {
            keysOfTitlesForSameLiveDirectorWriter = titlesForSameLiveDirectorWriter.keySet().stream().toList();
        }
        List<String> sublist = keysOfTitlesForSameLiveDirectorWriter.subList(from, to);
        return new Result(titlesForSameLiveDirectorWriter.entrySet().stream()
                .filter(x -> sublist.contains(x.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                1,
                page < 1 ? -1 : page - 1,
                page,
                page < lastPage ? page + 1 : -1,
                lastPage);
    }
}
