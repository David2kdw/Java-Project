package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class TestSortByRating {
    Entry e1;
    Entry e2;
    Entry e3;

    @BeforeEach
    public void runBefore() {
        e1 = new Entry("Entry 1", "abc", 1, 10, false);
        e2 = new Entry("Entry 2", "qwe", 2, 3, false);
        e3 = new Entry("Entry 3", "123", 3, 13, false);
    }

    @Test
    public void testCompare() {
        e1.setRating(10);
        e2.setRating(6);
        e3.setRating(1);
        SortByRating c = new SortByRating();
        int zero = c.compare(e1, e1);
        int positive = c.compare(e3, e1);
        int negative = c.compare(e1, e3);
        assertEquals(0, zero);
        assertEquals(9, positive);
        assertEquals(-9, negative);
    }
}
