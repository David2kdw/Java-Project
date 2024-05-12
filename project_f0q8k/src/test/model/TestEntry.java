package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class TestEntry {
    Entry e1;
    Entry e2;
    Entry e3;
    Time time1;

    @BeforeEach
    public void runBefore() {
        Time.cleanTimeLine();
        e1 = new Entry("Entry 1", "abc", 1, 10, false);
        e2 = new Entry("Entry 2", "qwe", 2, 3, false);
        e3 = new Entry("Entry 3", "123", 3, 13, false);
        time1 = new Time();
    }

    @Test
    public void testToString() {
        String comments = e3.getComments();
        assertEquals(e1.toString(), "Title: Entry 1, Status: to be watched, Tags:[]\n" +
                "    Description: abc\n" +
                "    Date created: " + time1.getDateInString());
        assertEquals(e2.toString(), "Title: Entry 2, Status: watching, Episodes: 0/3, Tags:[]\n" +
                "    Description: qwe\n" +
                "    Date created: " + time1.getDateInString() + "\n" +
                "    Date started: " + time1.getDateInString());
        assertEquals(e3.toString(), "Title: Entry 3, Status: finished, Episodes: 13/13, Tags:[]\n" +
                "    Description: 123\n" +
                "    Rating: -1\n" +
                "    Date created: " + time1.getDateInString() + "\n" +
                "    Date started: " + time1.getDateInString() + "\n" +
                "    Date finished: " + time1.getDateInString() + "\n" +
                "    Comments: " + comments);
        Entry e4 = new Entry("Entry 4", "123", 100, 13, false);
        assertNull(e4.toString());
    }

    @Test
    public void testTimeline() {
        List<String> timeline = Time.getTimeLine();
        assertEquals(5, timeline.size());
        assertEquals(e1.getTitle() + " created at " + time1.getDateInString(), timeline.get(0));
        assertEquals(e2.getTitle() + " created at " + time1.getDateInString(), timeline.get(1));
        assertEquals("Started watching " + e2.getTitle() + " at " + time1.getDateInString(), timeline.get(2));
        assertEquals(e3.getTitle() + " created at " + time1.getDateInString(), timeline.get(3));
        assertEquals("Finished watching " + e3.getTitle() + " at " + time1.getDateInString(), timeline.get(4));
    }
    @Test
    public void testConstructor() {
        assertEquals("Entry 1", e1.getTitle());
        assertEquals("abc", e1.getDescription());
        assertEquals(1, e1.getStatus());
        assertEquals(10, e1.getTotalEpisodes());
        assertEquals(0, e1.getCurrentEpisode());
        assertEquals(0, e2.getCurrentEpisode());
        assertEquals(13, e3.getCurrentEpisode());
        assertEquals(time1.getDateInString(), e1.getDateCreated());
        assertEquals(time1.getDateInString(), e2.getDateStarted());
        assertEquals(time1.getDateInString(), e3.getDateStarted());
        assertEquals(time1.getDateInString(), e3.getDateFinished());
        assertEquals(time1.getDateInString(), e3.getDateCreated());
        List<String> tagList = e1.getTags();
        assertEquals(0, tagList.size());
        assertEquals(-1, e1.getRating());
    }


    @Test
    public void testStatus1to2() {
        assertFalse(e1.isFinished());
        assertEquals(1, e1.getStatus());
        e1.beginWatching();
        Time d = new Time();
        assertEquals(d.getDateInString(), e1.getDateStarted());
        assertEquals(6, Time.getTimeLine().size());
        assertEquals("Started watching " + e1.getTitle() + " at " + d.getDateInString(),
                Time.getTimeLine().get(5));
        assertEquals(2, e1.getStatus());
        assertEquals(0, e1.getCurrentEpisode());
    }

    @Test
    public void testStatus2to3() {
        assertFalse(e2.isFinished());
        assertEquals(2, e2.getStatus());

        assertFalse(e2.watch());
        assertEquals(1, e2.getCurrentEpisode());
        assertEquals(2, e2.getStatus());
        assertFalse(e2.isFinished());

        assertFalse(e2.watch());
        assertEquals(2, e2.getCurrentEpisode());
        assertEquals(2, e2.getStatus());
        assertFalse(e2.isFinished());

        assertTrue(e2.watch());
        Time finish = new Time();
        assertEquals(6, Time.getTimeLine().size());
        assertEquals("Finished watching " + e2.getTitle() + " at " + finish.getDateInString(),
                Time.getTimeLine().get(5));
        assertEquals(3, e2.getCurrentEpisode());
        assertEquals(3, e2.getStatus());
        assertEquals(finish.getDateInString(), e2.getDateFinished());
        assertTrue(e2.isFinished());
    }

    @Test
    public void testSetRating() {
        assertEquals(-1, e3.getRating());
        e3.setRating(8);
        assertEquals(8, e3.getRating());
    }

    @Test
    public void testAddTags() {
        List<String> tagList = e1.getTags();
        List<String> allTags = Entry.getAllTags();
        assertEquals(0, tagList.size());
        assertEquals(0, allTags.size());

        e1.addTag("Sci-Fi");
        tagList = e1.getTags();
        allTags = Entry.getAllTags();
        assertEquals(1, tagList.size());
        assertEquals(1, allTags.size());
        assertEquals("Sci-Fi", tagList.get(0));
        assertEquals("Sci-Fi", allTags.get(0));

        e2.addTag("TV");
        tagList = e2.getTags();
        allTags = Entry.getAllTags();
        assertEquals(1, tagList.size());
        assertEquals(2, allTags.size());
        assertEquals("TV", tagList.get(0));
        assertEquals("Sci-Fi", allTags.get(0));
        assertEquals("TV", allTags.get(1));

        e2.addTag("Sci-Fi");
        tagList = e2.getTags();
        allTags = Entry.getAllTags();
        assertEquals(2, tagList.size());
        assertEquals(2, allTags.size());
        assertEquals("Sci-Fi", tagList.get(1));

        Entry e4 = new Entry("Entry 4", "1", 1, 1, false);
        List<String> tags = new ArrayList<>();
        tags.add("Sci-Fi");
        tags.add("TV");
        e4.addTags(tags);
        assertEquals(2, e4.getTags().size());
        Entry.setAllTags(tags);
        assertEquals(2, Entry.getAllTags().size());
    }

    @Test
    public void testSetDescription() {
        e1.setDescription("Description!");
        assertEquals("Description!", e1.getDescription());
    }

    @Test
    void testSetLoading() {
        e1.setLoading(true);
        assertTrue(e1.isLoading());
    }

    @Test
    void testDateCreated() {
        Time now = new Time();
        e1.setDateCreated(now.getDateInString());
        assertEquals(e1.getDateCreated(), now.getDateInString());
        e1.setDateStarted((now.getDateInString()));
        assertEquals(e1.getDateStarted(), now.getDateInString());
        e1.setDateFinished(now.getDateInString());
        assertEquals(e1.getDateFinished(), now.getDateInString());
    }

    @Test
    void testSetEpisode() {
        e1.setCurrentEpisode(5);
        assertEquals(5, e1.getCurrentEpisode());
    }

    @Test
    void testToJson() {
        Entry e4 = new Entry("no1", "abc", 1, 18, false);
        Time now = new Time();
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        e4.addTags(tags);
        e4.setComments("test comment");
        JSONObject jsonObject = e4.toJson();
        JsonReader reader = new JsonReader("./data/testWriterGeneralListOfEntry.json");
        assertEquals("no1", jsonObject.getString("title"));
        assertEquals(tags, reader.parseListOfString(jsonObject.getJSONArray("tags")));
        assertEquals("abc", jsonObject.getString("description"));
        assertEquals("test comment", jsonObject.getString("comments"));
        assertEquals(1, jsonObject.getInt("status"));
        assertEquals(18, jsonObject.getInt("totalEpisodes"));
        assertEquals(0, jsonObject.getInt("currentEpisode"));
        assertEquals(now.getDateInString(), jsonObject.getString("dateCreated"));
        JSONObject jsonObject2 = e2.toJson();
        assertEquals(time1.getDateInString(), jsonObject2.getString("dateCreated"));
        JSONObject jsonObject3 = e3.toJson();
        assertEquals(time1.getDateInString(), jsonObject3.getString("dateFinished"));
        assertEquals(-1, jsonObject.getInt("rating"));
    }

    @Test
    void testImagePath() {
        e1.setImagePath("./data/testImage.jpg");
        assertEquals("./data/testImage.jpg", e1.getImagePath());
    }
}