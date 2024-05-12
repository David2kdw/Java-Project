package model;

import java.util.*;

// A date object to record a moment in time, also a static method implementing a timeline
public class Time {
    private final String year;
    private final String month;
    private final String day;
    private final String dateInString;
    private static List<String> timeLine = new ArrayList<>();

    // EFFECTS: construct a date, with the current time
    public Time() {
        Calendar cal = Calendar.getInstance();
        dateInString = String.valueOf(cal.getTime());
        year = dateInString.substring(24, 28);
        month = dateInString.substring(4,7);
        day = dateInString.substring(8, 10);
    }

    // EFFECTS: return the date in string
    public String getDateInString() {
        return dateInString;
    }

    // EFFECTS: return the day in string
    public String getDay() {
        return day;
    }

    // EFFECTS: return the month in string
    public String getMonth() {
        return month;
    }

    // EFFECTS: return the year in string
    public String getYear() {
        return year;
    }


    // EFFECTS: return the timeline
    public static List<String> getTimeLine() {
        return timeLine;
    }

    // REQUIRES: type should be chosen from:
    //           1: entry created
    //           2: started watching
    //           3: finished watching
    // MODIFIES: this
    // EFFECT: update the timeline using the given entry
    public static void addTimeLine(Entry entry, int type) {
        if (!entry.isLoading()) {
            EventLog eventLog = EventLog.getInstance();
            String now = new Time().dateInString;
            switch (type) {
                case 1:
                    timeLine.add(entry.getTitle() + " created at " + now);
                    eventLog.logEvent(new Event(entry.getTitle() + " created"));
                    break;
                case 2:
                    timeLine.add("Started watching " + entry.getTitle() + " at " + now);
                    eventLog.logEvent(new Event("Started watching " + entry.getTitle()));
                    break;
                case 3:
                    timeLine.add("Finished watching " + entry.getTitle() + " at " + now);
                    eventLog.logEvent(new Event("Finished watching " + entry.getTitle()));
                    break;
            }
        }
    }

    // EFFECTS: clean the timeline
    public static void cleanTimeLine() {
        timeLine = new ArrayList<>();
    }

    // EFFECTS: set the timeline
    public static void setTimeLine(List<String> timeLine1) {
        timeLine = timeLine1;
    }


}
