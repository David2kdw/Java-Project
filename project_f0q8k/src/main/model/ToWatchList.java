package model;

import java.util.ArrayList;
import java.util.List;

// represent the to-watch list, the items should all be entries that are in status 1
public class ToWatchList extends ListOfEntry {

    // REQUIRES: list size > 0
    // MODIFIES: this, entry
    // EFFECT: remove the started entry
    public void startAtIndex(int index) {
        listOfEntry.get(index).beginWatching();
        listOfEntry.remove(index);
    }
}
