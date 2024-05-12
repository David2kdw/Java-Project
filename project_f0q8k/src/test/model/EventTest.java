package model;

import model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Event class
 */
public class EventTest {
    private Event e;
    private Event e2;
    private Date d;

    //NOTE: these tests might fail if time at which line (2) below is executed
    //is different from time that line (1) is executed.  Lines (1) and (2) must
    //run in same millisecond for this test to make sense and pass.

    @BeforeEach
    public void runBefore() {
        e = new Event("Sensor open at door");   // (1)
        e2 = new Event("Sensor open at door");
        d = Calendar.getInstance().getTime();   // (2)
    }

    @Test
    public void testEvent() {
        assertEquals("Sensor open at door", e.getDescription());
        assertEquals(d.toString(), e.getDate().toString());
    }

    @Test
    public void testToString() {
        assertEquals(d.toString() + "\n" + "Sensor open at door", e.toString());
    }

    @Test
    public void testEquals() {
        Event nullEvent = null;
        assertFalse(e.equals(null));
        Time time = new Time();
        assertFalse(e.equals(time));

        assertEquals(e2.hashCode(), e2.hashCode());
    }
}
