package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestReader {
    @Test
    void testNoSuchFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ListOfEntry listOfEntry = reader.read();
            fail();
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testSetSource() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        reader.setSource("./data/testWriterEmptyList.json");
        try {
            ListOfEntry listOfEntry = reader.read();
        } catch (IOException e) {
            fail();
        }
    }
}
