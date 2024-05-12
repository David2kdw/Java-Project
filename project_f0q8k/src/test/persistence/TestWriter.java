package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestWriter {
    Time time1;

    @Test
    void testSetDestination() {
        JsonWriter writer = new JsonWriter("a");
        writer.setDestination("./data/testWriterEmptyList.json");
        try {
            writer.open();
        } catch (IOException e) {
            fail();
        }


    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/\0invalid.json");
            writer.open();
            fail("Exception not caught");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriteEmptyList() {
        try {
            ToWatchList toWatchList = new ToWatchList();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyList.json");
            writer.open();
            writer.write(toWatchList);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyList.json");
            toWatchList.copy(reader.read());
            assertEquals(0, toWatchList.getListOfEntry().size());
        } catch (IOException e) {
            fail("No exception expected");
        }
    }

    @Test
    void testWriteEmptyListOfString() {
        try {
            List<String> testTimeLine = new ArrayList<>();
            List<String> emptyList = new ArrayList<>();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyStringList.json");
            writer.open();
            writer.write(testTimeLine);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyStringList.json");
            testTimeLine = reader.readStringList();
            assertEquals(emptyList, testTimeLine);
        } catch (IOException e) {
            fail("No exception expected");
        }
    }

    @Test
    void testWriteGeneralListOfEntry() {
        try {
            ListOfEntry listOfEntry = new ListOfEntry();
            Entry e1 = new Entry("no1", "abc", 1, 18, false);
            Entry e2 = new Entry("no2", "abcde", 2, 2, false);
            Entry e3 = new Entry("no3", "finished", 3, 10, false);
            e3.setComments("test");
            e3.setImagePath("imagePath");
            e1.addTag("tag1");
            e1.addTag("tag2");
            e2.addTag("tag2");
            e3.setRating(9);
            e3.addTag("tag3");
            time1 = new Time();
            listOfEntry.addEntry(e1);
            listOfEntry.addEntry(e2);
            listOfEntry.addEntry(e3);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralListOfEntry.json");
            writer.open();
            writer.write(listOfEntry);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralListOfEntry.json");
            ListOfEntry listOfEntry1 = new ListOfEntry();
            listOfEntry1.copy(reader.read());
            assertEquals(3, listOfEntry1.getListOfEntry().size());
            assertEquals("no1", listOfEntry1.getListOfEntry().get(0).getTitle());
            assertEquals("abc", listOfEntry1.getListOfEntry().get(0).getDescription());
            assertEquals(1, listOfEntry1.getListOfEntry().get(0).getStatus());
            assertEquals(18, listOfEntry1.getListOfEntry().get(0).getTotalEpisodes());
            assertEquals(time1.getDateInString(), listOfEntry1.getListOfEntry().get(0).getDateCreated());
            assertFalse(listOfEntry1.getListOfEntry().get(0).isLoading());

            assertEquals("no2", listOfEntry1.getListOfEntry().get(1).getTitle());
            assertEquals("abcde", listOfEntry1.getListOfEntry().get(1).getDescription());
            assertEquals(2, listOfEntry1.getListOfEntry().get(1).getStatus());
            assertEquals(2, listOfEntry1.getListOfEntry().get(1).getTotalEpisodes());
            assertEquals(time1.getDateInString(), listOfEntry1.getListOfEntry().get(1).getDateCreated());
            assertEquals(time1.getDateInString(), listOfEntry1.getListOfEntry().get(1).getDateStarted());
            assertFalse(listOfEntry1.getListOfEntry().get(1).isLoading());

            assertEquals("no3", listOfEntry1.getListOfEntry().get(2).getTitle());
            assertEquals("finished", listOfEntry1.getListOfEntry().get(2).getDescription());
            assertEquals(3, listOfEntry1.getListOfEntry().get(2).getStatus());
            assertEquals(10, listOfEntry1.getListOfEntry().get(2).getTotalEpisodes());
            assertEquals(10, listOfEntry1.getListOfEntry().get(2).getCurrentEpisode());
            assertEquals(time1.getDateInString(), listOfEntry1.getListOfEntry().get(2).getDateCreated());
            assertEquals(time1.getDateInString(), listOfEntry1.getListOfEntry().get(2).getDateStarted());
            assertEquals(time1.getDateInString(), listOfEntry1.getListOfEntry().get(2).getDateFinished());
            assertEquals(9, listOfEntry1.getListOfEntry().get(2).getRating());
            assertEquals("test", listOfEntry1.getListOfEntry().get(2).getComments());
            assertEquals("imagePath", listOfEntry1.getListOfEntry().get(2).getImagePath());
            assertFalse(listOfEntry1.getListOfEntry().get(2).isLoading());

        } catch (IOException e) {
            fail("Not expecting");
        }
    }

    @Test
    void testWriteListOfString() {
        try {
            List<String> stringList = new ArrayList<>();
            stringList.add("one");
            stringList.add("two");
            JsonWriter jsonWriter = new JsonWriter("./data/testWriterGeneralListOfString.json");
            jsonWriter.open();
            jsonWriter.write(stringList);
            jsonWriter.close();

            JsonReader jsonReader = new JsonReader("./data/testWriterGeneralListOfString.json");
            List<String> result = jsonReader.readStringList();
            assertEquals(stringList, result);
        } catch (IOException e) {
            fail("Not expecting");
        }

    }
}
