package model;

import java.util.ArrayList;
import java.util.List;

// represents a list only contains entries with status 3 (finished watching)
public class FinishedList extends ListOfEntry {

    // REQUIRES: list size > 0
    // MODIFIES: this, entry
    // EFFECT: rate one episode which the entry is given by the index
    public void rateAtIndex(int index, int rating) {
        Entry entry = listOfEntry.get(index);
        entry.setRating(rating);
    }

    // EFFECT: return the list of entries in descending order of ratings
    public List<Entry> sortByRating() {
        List<Entry> original = new ArrayList<>(listOfEntry);
        original.sort(new SortByRating());
        return original;
    }


}
