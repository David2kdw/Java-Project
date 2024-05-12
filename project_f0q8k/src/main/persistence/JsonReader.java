package persistence;

import model.Entry;
import model.ListOfEntry;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.List;

// Represents a reader that reads ListOfEntry from JSON data stored in file
// This class is modeled from the example JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS: constructs the JsonReader with the source
    public JsonReader(String source) {
        this.source = source;
    }

    // MODIFIES: this
    // EFFECTS: change the source to the given one
    public void setSource(String source) {
        this.source = source;
    }

    // REQUIRES: source file should be json file with list of String
    // EFFECTS: read the json file of string lists and returns it
    public List<String> readStringList() throws IOException {
        String jsonData = readFile(source);
        JSONArray jsonArray = new JSONArray(jsonData);
        return parseListOfString(jsonArray);
    }

    // REQUIRES: source file should be json file with ListOfEntry
    // EFFECTS: read json file
    public ListOfEntry read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseListOfEntry(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses ListOfEntry from JSON object and returns it
    public ListOfEntry parseListOfEntry(JSONObject jsonObject) {
        // String name = jsonObject.getString("name");
        ListOfEntry listOfEntry = new ListOfEntry();
        addEntries(listOfEntry, jsonObject);
        listOfEntry.loadingComplete();
        return listOfEntry;
    }

    // MODIFIES: listOfEntry
    // EFFECTS: parses Entries from JSON object and adds them to ListOfEntry
    private void addEntries(ListOfEntry listOfEntry, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("listOfEntry");
        for (Object json : jsonArray) {
            JSONObject nextEntry = (JSONObject) json;
            addEntry(listOfEntry, nextEntry);
        }
    }

    // MODIFIES: listOfEntry
    // EFFECTS: parses Entry from JSON object and adds it to ListOfEntry
    private void addEntry(ListOfEntry listOfEntry, JSONObject jsonObject) {
        String title = jsonObject.getString("title");
        String created = jsonObject.getString("dateCreated");
        int status = jsonObject.getInt("status");
        String des = jsonObject.getString("description");
        int current = jsonObject.getInt("currentEpisode");
        int total = jsonObject.getInt("totalEpisodes");
        List<String> tags = parseListOfString(jsonObject.getJSONArray("tags"));
        int rating = jsonObject.getInt("rating");
        Entry entry = new Entry(title, des, status, total, true);
        entrySetData(jsonObject, created, current, tags, rating, entry);
        if (status == 2) {
            //String start = jsonObject.getString("dateStarted");
            entry.setDateStarted(jsonObject.getString("dateStarted"));
        } else if (status == 3) {
            //String comments = jsonObject.getString("comments");
            entry.setComments(jsonObject.getString("comments"));
            String start = jsonObject.getString("dateStarted");
            entry.setDateStarted(start);
            String finish = jsonObject.getString("dateFinished");
            entry.setDateFinished(finish);
        }
        listOfEntry.addEntry(entry);
    }

    private void entrySetData(JSONObject jsonObject, String created,
                              int current, List<String> tags, int rating, Entry entry) {
        entry.setRating(rating);
        entry.addTags(tags);
        entry.setCurrentEpisode(current);
        entry.setDateCreated(created);
        entry.setImagePath(jsonObject.getString("path"));
    }

    // EFFECTS: parses List<String> from JSON object and adds them to ListOfEntry
    public List<String> parseListOfString(JSONArray jsonArray) {
        List<String> tags = new ArrayList<>();
        for (Object json: jsonArray) {
            tags.add((String) json);
        }
        return tags;
    }

}
