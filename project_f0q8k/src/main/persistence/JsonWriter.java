package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.List;

// Represents a writer that writes JSON representation of ListOfEntry or List<String> to file
// This class is modeled from the example JsonSerializationDemo
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String location) {
        this.destination = location;
    }

    // EFFECTS: set the destination
    public void setDestination(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // opens writer; throws FileNotFoundException if destination file cannot
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of ListOfEntry to file
    public void write(ListOfEntry listOfEntry) {
        JSONObject json = listOfEntry.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: write JSON representation of List<String> to file
    public void write(List<String> stringList) {
        JSONArray jsonArray = new JSONArray();
        for (String s: stringList) {
            jsonArray.put(s);
        }
        saveToFile(jsonArray.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
