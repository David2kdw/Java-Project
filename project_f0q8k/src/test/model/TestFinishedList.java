package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestFinishedList {
    FinishedList list1;
    Entry e1;
    Entry e2;
    Entry e3;

    @BeforeEach
    public void runBefore() {
        e1 = new Entry("Entry 1", "abc", 1, 10, false);
        e2 = new Entry("Entry 2", "qwe", 2, 3, false);
        e3 = new Entry("Entry 3", "123", 3, 13, false);
        list1 = new FinishedList();
        list1.addEntry(e1);
        list1.addEntry(e2);
        list1.addEntry(e3);
    }

    @Test
    public void testRateAtIndex() {
        list1.rateAtIndex(0, 1);
        list1.rateAtIndex(1, 6);
        list1.rateAtIndex(2, 10);
        List<Entry> entries = list1.getListOfEntry();
        assertEquals(1, e1.getRating());
        assertEquals(6, e2.getRating());
        assertEquals(10, e3.getRating());
    }

    @Test
    public void testSortByRating() {
        list1.rateAtIndex(0, 1);
        list1.rateAtIndex(1, 6);
        list1.rateAtIndex(2, 10);
        List<Entry> sorted = list1.sortByRating();
        assertEquals(3, sorted.size());
        assertEquals(e3, sorted.get(0));
        assertEquals(e2, sorted.get(1));
        assertEquals(e1, sorted.get(2));
    }

}
