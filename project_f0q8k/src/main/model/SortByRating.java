package model;

import java.util.Comparator;

// A comparator that returns a positive number if the second rating is greater
public class SortByRating implements Comparator<Entry> {

    // EFFECTS: sort in descending order of ratings, return positive integer if second rating is higher
    @Override
    public int compare(Entry o1, Entry o2) {
        return o2.getRating() - o1.getRating();
    }
}
