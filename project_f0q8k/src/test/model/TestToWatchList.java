package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestToWatchList {
    ToWatchList list1;
    Entry e1;
    Entry e2;
    Entry e3;

    @BeforeEach
    public void runBefore() {
        e1 = new Entry("Entry 1", "abc", 1, 10, false);
        e2 = new Entry("Entry 2", "qwe", 1, 3, false);
        e3 = new Entry("Entry 3", "123", 1, 13, false);
        list1 = new ToWatchList();
        list1.addEntry(e1);
        list1.addEntry(e2);
        list1.addEntry(e3);
    }

    @Test
    public void testStartAtIndex() {
        list1.startAtIndex(0);
        List<Entry> remain = list1.getListOfEntry();
        assertEquals(2, remain.size());
        assertEquals(e2, remain.get(0));
        assertEquals(e3, remain.get(1));
    }
}
