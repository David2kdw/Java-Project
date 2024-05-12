package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestWatchingList {
    WatchingList list1;
    Entry e1;
    Entry e2;
    Entry e3;

    @BeforeEach
    public void runBefore() {
        e1 = new Entry("Entry 1", "abc", 2, 10, false);
        e2 = new Entry("Entry 2", "qwe", 2, 3, false);
        e3 = new Entry("Entry 3", "123", 3, 13, false);
        list1 = new WatchingList();
        list1.addEntry(e1);
        list1.addEntry(e2);
        list1.addEntry(e3);
    }

    @Test
    public void testWatchAtIndex() {
        assertFalse(list1.watchAtIndex(1));
        assertEquals(1, e2.getCurrentEpisode());
        assertFalse(list1.watchAtIndex(1));
        assertEquals(2, e2.getCurrentEpisode());
        assertTrue(list1.watchAtIndex(1));
        assertEquals(2, list1.getListOfEntry().size());
        assertEquals(3, e2.getCurrentEpisode());
        assertEquals(3, e2.getStatus());
    }

    @Test
    public void testRemoveFinished() {
        List<Entry> removed = list1.removeFinished();
        assertEquals(1, removed.size());
        assertEquals(e3, removed.get(0));
        assertEquals(2, list1.getListOfEntry().size());
    }


}
