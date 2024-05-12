package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class TestTime {
    Time d1;
    String dateInString;
    Calendar cal;

    @BeforeEach
    public void runBefore() {
        d1 = new Time();
        cal = Calendar.getInstance();
        dateInString = String.valueOf(cal.getTime());
    }

    @Test
    public void testDate() {
        assertEquals(dateInString, d1.getDateInString());
        assertEquals(dateInString.substring(24, 28), d1.getYear());
        assertEquals(dateInString.substring(4,7), d1.getMonth());
        assertEquals(dateInString.substring(8,10), d1.getDay());
    }

    @Test
    public void testClear() {
        Time.cleanTimeLine();
        List<String> empty = new ArrayList<>();
        assertEquals(Time.getTimeLine(), empty);
    }

    @Test
    public void testSet() {
        List<String> stringList = new ArrayList<>();
        stringList.add("A");
        stringList.add("B");
        Time.setTimeLine(stringList);
        assertEquals(2, Time.getTimeLine().size());
        assertEquals("A", Time.getTimeLine().get(0));
        assertEquals("B", Time.getTimeLine().get(1));
    }

}
