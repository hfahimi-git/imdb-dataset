package com.hfahimi.lb;

import de.siegmar.fastcsv.reader.NamedCsvReader;
import de.siegmar.fastcsv.reader.NamedCsvRow;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class Assignment {
    private Map<String, String> actors = new HashMap<>();
    private final Map<String, String> allNames = new HashMap<>();
    private final Map<String, String> titlesForSameDirectorWriter = new HashMap<>();
    private Map<String, String> titlesForSameLiveDirectorWriter = new HashMap<>();
    private final List<String> livePerson = new ArrayList<>();
    private Map<String, String> titles = new HashMap<>();

    public Map<String, String> getActors() {
        return actors;
    }

    public Map<String, String> getAllNames() {
        return allNames;
    }

    public Map<String, String> getTitlesForSameLiveDirectorWriter() {
        return titlesForSameLiveDirectorWriter;
    }

    public Map<String, String> getTitles() {
        return titles;
    }

    private void p1() throws IOException {
        System.out.println("start loading title.crew ...");
        var t1 = new Date().getTime();

        FileReader fis = new FileReader(new ClassPathResource("title.crew.tsv").getFile());
        for (final NamedCsvRow csvRow : NamedCsvReader.builder().fieldSeparator('\t').build(fis)) {
            String directors = csvRow.getField("directors"),
                    writers = csvRow.getField("writers");

            if (directors.equals("\\N") || writers.equals("\\N")) {
                continue;
            }

            Set<String> same = new HashSet<>(Arrays.asList(directors.split(",")));
            same.retainAll(Arrays.asList(writers.split(",")));
            if (same.size() < 1) {
                continue;
            }

            String tConst = csvRow.getField("tconst");
            for (String name : same) {
                if (titlesForSameDirectorWriter.containsKey(name)) {
                    titlesForSameDirectorWriter.put(name, titlesForSameDirectorWriter.get(name) + "," + tConst);
                } else {
                    titlesForSameDirectorWriter.put(name, tConst);
                }
            }
        }
        fis.close();
        System.out.println("end loading title.crew. it took " + (new Date().getTime() - t1) + " milliseconds");
    }

    private void p2() throws IOException {
        System.out.println("start loading name.basic and live person ...");
        var t1 = new Date().getTime();

        FileReader fis = new FileReader(new ClassPathResource("name.basics.tsv").getFile());
        Map<String, String> m = new HashMap<>();
        for (NamedCsvRow csvRow : NamedCsvReader.builder().fieldSeparator('\t').build(fis)) {
            if (csvRow.getField("deathYear").equals("\\N") && !csvRow.getField("birthYear").equals("\\N")) {
                livePerson.add(csvRow.getField("nconst"));
            }
            allNames.put(csvRow.getField("nconst"), csvRow.getField("primaryName"));
        }
        fis.close();
        System.out.println("end loading name.basic and live person. it took " + (new Date().getTime() - t1) + " milliseconds");
    }

    private void p3() throws IOException {
        System.out.println("start loading title.basics ...");
        var t1 = new Date().getTime();

        FileReader fis = new FileReader(new ClassPathResource("title.basics.tsv").getFile());
        titles = new HashMap<>();
        NamedCsvReader.builder().fieldSeparator('\t').quoteCharacter('|').build(fis).stream()
                .forEach(
                        csvRow ->
                                titles.put(csvRow.getField("tconst"), csvRow.getField("primaryTitle"))
                );
        fis.close();
        System.out.println("end loading title.basics. it took " + (new Date().getTime() - t1) + " milliseconds");
    }

    private void p4() throws IOException {
        System.out.println("start loading title.principals ...");
        var t1 = new Date().getTime();

        FileReader fis = new FileReader(new ClassPathResource("title.principals.tsv").getFile());
        for (NamedCsvRow csvRow : NamedCsvReader.builder().fieldSeparator('\t').quoteCharacter('|').build(fis)) {
            if (csvRow.getField("category").equals("actor")) {

                String nconst = csvRow.getField("nconst");
                String tconst = csvRow.getField("tconst");

                if (actors.containsKey(nconst)) {
                    actors.put(nconst, actors.get(nconst) + "," + tconst);
                } else {
                    actors.put(nconst, tconst);
                }
            }
        }
        fis.close();
        System.out.println("end loading title.principals. it took " + (new Date().getTime() - t1) + " milliseconds");

    }

    public void loadData() throws ExecutionException, InterruptedException {

        CompletableFuture<Void> cf1 = CompletableFuture.runAsync(() -> {
            try {
                 p1();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        CompletableFuture<Void> cf2 = CompletableFuture.runAsync(() -> {
            try {
                 p2();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture<Void> cf3 = CompletableFuture.runAsync(() -> {
            try {
                 p3();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        CompletableFuture<Void> cf4 = CompletableFuture.runAsync(() -> {
            try {
                p4();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        cf1.get();
        cf2.get();
        cf3.get();
        cf4.get();

        Object[] live = livePerson.toArray();
        titlesForSameLiveDirectorWriter = new HashMap<>();
        for (Map.Entry<String, String> dw : titlesForSameDirectorWriter.entrySet()) {
            if (Arrays.binarySearch(live, dw.getKey()) >= 0) {
                Arrays.stream(dw.getValue().split(","))
                        .forEach (
                            movieTitle -> {
                                if(titles.containsKey(movieTitle)) {
                                    titlesForSameLiveDirectorWriter.put(titles.get(movieTitle), allNames.get(dw.getKey()));
                                }
                            }
                        );
            }
        }
    }

}
