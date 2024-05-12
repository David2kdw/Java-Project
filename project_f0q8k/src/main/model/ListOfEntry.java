package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// represent a list contains multiple entries
public class ListOfEntry {
    protected List<Entry> listOfEntry;

    // EFFECTS: create an empty list of entries
    public ListOfEntry() {
        listOfEntry = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: if the entry is not present in the list, add the entry
    public void addEntry(Entry e) {
        if (!this.listOfEntry.contains(e)) {
            this.listOfEntry.add(e);
        }
    }

    // REQUIRES: list size > 0
    // MODIFIES: this
    // EFFECTS: remove the entry at the given index
    public void removeAt(int index) {
        this.listOfEntry.remove(index);
    }

    // REQUIRES: list size > 0
    // EFFECTS: return the entries in the reverse order they were added
    public List<Entry> sortByDate() {
        List<Entry> reversed = new ArrayList<>();
        for (int i = listOfEntry.size() - 1; i >= 0; i--) {
            reversed.add(listOfEntry.get(i));
        }
        return reversed;
    }

    // REQUIRES: list size > 0
    // EFFECTS: return the entries with the given tag
    public List<Entry> filterByTag(String tag) {
        List<Entry> filtered = new ArrayList<>();
        for (Entry entry: listOfEntry) {
            if (entry.getTags().contains(tag)) {
                filtered.add(entry);
            }
        }
        return filtered;
    }

    // EFFECTS: return json object representing the list
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("listOfEntry", entriesToJson());
        return json;
    }

    // EFFECTS: return the json array representing the entries
    private JSONArray entriesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Entry e: listOfEntry) {
            jsonArray.put(e.toJson());
        }
        return jsonArray;
    }


    // EFFECTS: return the list
    public List<Entry> getListOfEntry() {
        return listOfEntry;
    }

    // EFFECTS: copy all the entries from the given ListOfEntry to the current ListOfEntry
    public void copy(ListOfEntry listOfEntry) {
        this.listOfEntry = new ArrayList<>();
        for (Entry e: listOfEntry.getListOfEntry()) {
            this.addEntry(e);
        }
    }

    // EFFECTS: set all entries isLoading to false
    public void loadingComplete() {
        for (Entry e: listOfEntry) {
            e.setLoading(false);
        }
    }
}
