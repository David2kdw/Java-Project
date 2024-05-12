package model;

import java.util.ArrayList;
import java.util.List;

// represents a list contains entries at status 2 (currently watching)
public class WatchingList extends ListOfEntry {

    // REQUIRES: list size > 0
    // MODIFIES: this, entry
    // EFFECT: watch one episode which the entry is given by the index, remove the entry if finished watching
    public boolean watchAtIndex(int index) {
        Entry entry = listOfEntry.get(index);
        boolean isFinished = entry.watch();
        if (isFinished) {
            listOfEntry.remove(index);
        }
        return isFinished;
    }

    // REQUIRES: list size > 0
    // MODIFIES: this
    // EFFECT: remove all the finished entries and return them
    public List<Entry> removeFinished() {
        List<Entry> original = new ArrayList<>(listOfEntry);
        List<Entry> removed = new ArrayList<>();
        for (Entry entry: original) {
            if (entry.isFinished()) {
                removed.add(entry);
                listOfEntry.remove(entry);
            }
        }
        return removed;
    }
}
