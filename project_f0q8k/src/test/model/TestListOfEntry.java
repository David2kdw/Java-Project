package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class TestListOfEntry {
    ListOfEntry list1;
    Entry e1;
    Entry e2;
    Entry e3;

    @BeforeEach
    public void runBefore() {
        e1 = new Entry("Entry 1", "abc", 1, 10, false);
        e2 = new Entry("Entry 2", "qwe", 2, 3, false);
        e3 = new Entry("Entry 3", "123", 3, 13, false);
        list1 = new ListOfEntry();
    }

    @Test
    public void testAdd() {
        assertEquals(0, list1.getListOfEntry().size());
        list1.addEntry(e1);
        List<Entry> entries = list1.getListOfEntry();
        assertEquals(1, entries.size());
        assertEquals(e1, entries.get(0));
        list1.addEntry(e2);
        List<Entry> entries1 = list1.getListOfEntry();
        assertEquals(2, entries.size());
        assertEquals(e2, entries.get(1));
        list1.addEntry(e2);
        assertEquals(2, entries.size());
        assertEquals(e2, entries.get(1));
    }

    @Test
    public void testRemoveAt() {
        list1.addEntry(e1);
        list1.addEntry(e2);
        list1.removeAt(1);
        List<Entry> entries = list1.getListOfEntry();
        assertEquals(1, entries.size());
        assertEquals(e1, entries.get(0));
    }

    @Test
    public void testSortByDate() {
        list1.addEntry(e1);
        list1.addEntry(e2);
        list1.addEntry(e3);
        List<Entry> entries = new ArrayList<>();
        entries.add(e3);
        entries.add(e2);
        entries.add(e1);
        assertEquals(entries, list1.sortByDate());
    }

    @Test
    public void testFilterByTag() {
        e1.addTag("tag1");
        e2.addTag("tag2");
        e3.addTag("tag2");
        list1.addEntry(e1);
        list1.addEntry(e2);
        list1.addEntry(e3);
        List<Entry> filtered1 = list1.filterByTag("tag1");
        List<Entry> filtered2 = list1.filterByTag("tag2");
        assertTrue(filtered1.contains(e1));
        assertEquals(1, filtered1.size());
        assertTrue(filtered2.contains(e2));
        assertTrue(filtered2.contains(e3));
        assertEquals(2, filtered2.size());
    }

    @Test
    void testToJson() {
        list1.addEntry(e1);
        list1.addEntry(e2);
        list1.addEntry(e3);
        e3.setComments("test");
        JSONObject jsonObject = list1.toJson();
        JsonReader reader = new JsonReader("./data/testWriterGeneralListOfEntry.json");
        ListOfEntry resultList = reader.parseListOfEntry(jsonObject);
        assertTrue(isSame(e1, resultList.getListOfEntry().get(0)));
        assertTrue(isSame(e2, resultList.getListOfEntry().get(1)));
        assertTrue(isSame(e3, resultList.getListOfEntry().get(2)));
    }

    @Test
    void testCopy() {
        ListOfEntry list2 = new ListOfEntry();
        list2.addEntry(e1);
        list2.addEntry(e2);
        list2.addEntry(e3);
        list1.copy(list2);
        assertEquals(list2.getListOfEntry(), list1.getListOfEntry());
    }

    private boolean isSame(Entry e1, Entry e2) {
        return (Objects.equals(e1.getTitle(), e2.getTitle()) &&
                e1.getStatus() == e2.getStatus() &&
                e1.getTags().size() == e2.getTags().size() &&
                e1.getTotalEpisodes() == e2.getTotalEpisodes() &&
                Objects.equals(e1.getDateCreated(), e2.getDateCreated()) &&
                Objects.equals(e1.getDescription(), e2.getDescription()));
    }

}
