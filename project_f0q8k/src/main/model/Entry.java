package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

// represents an entry for an anime
public class Entry {
    private static List<String> allTags = new ArrayList<>();
    private final String title;
    private String description;
    private int status; // 1: want to watch  2: currently watching 3: finished watching
    private final int totalEpisodes;
    private int currentEpisode;
    private String dateCreated;
    private String dateStarted;
    private String dateFinished;
    private List<String> tags;
    private int rating; // ranged in [1,10]
    private boolean isLoading;
    private String comments;
    private String imagePath;

    // REQUIRES: status choose from 1, 2, or 3; totalEpisodes > 0
    // EFFECTS: construct a new entry, with its title, description, status, total episodes, the date it is created.
    //          tags and rating are to be added, current episode set to 0 or have already watched.
    public Entry(String title, String description, int status, int totalEpisodes, boolean isLoading) {
        this.isLoading = isLoading;
        this.title = title;
        this.description = description;
        this.status = status;
        this.totalEpisodes = totalEpisodes;
        this.tags = new ArrayList<>();
        this.dateCreated = new Time().getDateInString();
        setNull();
        this.rating = -1;
        Time.addTimeLine(this, 1); // timeline: created entry
        if (this.status == 3) { // entry already finished
            Time.addTimeLine(this, 3); // timeline: finished entry
            this.currentEpisode = this.totalEpisodes;
            this.dateFinished = this.dateCreated;
            this.dateStarted = this.dateCreated;
        } else {
            this.currentEpisode = 0; // not started
            if (this.status == 2) { // start watching
                this.dateStarted = this.dateCreated;
                Time.addTimeLine(this, 2); // timeline: started entry
            }
        }
    }

    // EFFECTS: set some fields null
    private void setNull() {
        this.dateStarted = null;
        this.dateFinished = null;
        this.comments = null;
        this.imagePath = "";
    }

    // EFFECT: return the details of the entry in string
    public String toString() {
        switch (this.status) {
            case 1: return case1();
            case 2: return case2();
            case 3: return case3();
        }
        return null;
    }

    // EFFECTS: returns the string of the entry with status 1
    private String case1() {
        return "Title: " + title + ", Status: to be watched, Tags:" + tags + "\n    "
                +
                "Description: " + description + "\n    "
                +
                "Date created: " + dateCreated;
    }

    // EFFECTS: returns the string of the entry with status 2
    private String case2() {
        return "Title: " + title + ", Status: watching, Episodes: " + currentEpisode + "/"
                +
                totalEpisodes + ", Tags:" + tags + "\n    "
                +
                "Description: " + description + "\n    "
                +
                "Date created: " + dateCreated + "\n    "
                +
                "Date started: " + dateStarted;
    }

    // EFFECTS: returns the string of the entry with status 3
    private String case3() {
        return  "Title: " + title + ", Status: finished, Episodes: " + currentEpisode + "/"
                +
                totalEpisodes + ", Tags:" + tags + "\n    "
                +
                "Description: " + description + "\n    "
                +
                "Rating: " + rating + "\n    "
                +
                "Date created: " + dateCreated + "\n    "
                +
                "Date started: " + dateStarted + "\n    "
                +
                "Date finished: " + dateFinished + "\n    "
                +
                "Comments: " + comments;
    }

    // EFFECTS: returns true if all episodes are watched
    public boolean isFinished() {
        return this.currentEpisode >= this.totalEpisodes;
    }

    // REQUIRES: status == 1
    // MODIFIES: this
    // EFFECTS: set status from 1 to 2
    public void beginWatching() {
        advanceStatus();
        Time.addTimeLine(this, 2);
        this.dateStarted = new Time().getDateInString();
    }

    // REQUIRES: status == 2
    // MODIFIES: this
    // EFFECTS: increments the current episode by 1, returns true if all finished, false otherwise
    public boolean watch() {
        this.currentEpisode++;
        EventLog log = EventLog.getInstance();
        log.logEvent(new Event("Watched " + title + " " + currentEpisode + "/" + totalEpisodes));
        if (isFinished()) {
            advanceStatus();
            Time.addTimeLine(this, 3);
            this.dateFinished = new Time().getDateInString();
            return true;
        } else {
            return false;
        }
    }

    // REQUIRES: status != 3
    // MODIFIES: this
    // EFFECTS: progress the status
    private void advanceStatus() {
        status++;
    }

    // REQUIRES: same tag was not added before
    // MODIFIES: this
    // EFFECT: add tag to the entry and the allTags contains all tags created
    public void addTag(String tag) {
        this.tags.add(tag);
        if (!allTags.contains(tag)) {
            Entry.allTags.add(tag);
        }
    }

    // EFFECTS: return the Json object representing this entry
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray tags = new JSONArray();
        for (String s: this.tags) {
            tags.put(s);
        }
        json.put("tags", tags);
        json.put("title", this.title);
        json.put("description", this.description);
        json.put("status", this.status);
        json.put("totalEpisodes", this.totalEpisodes);
        json.put("currentEpisode", this.currentEpisode);
        json.put("dateCreated", this.dateCreated);
        json.put("comments", this.comments);
        json.put("path", this.imagePath);
        if (status == 2) {
            json.put("dateStarted", this.dateStarted);
        } else if (status == 3) {
            json.put("dateStarted", this.dateStarted);
            json.put("dateFinished", this.dateFinished);
        }
        json.put("rating", this.rating);
        return json;
    }

    // EFFECTS: add a list of tags
    public void addTags(List<String> tags) {
        for (String s: tags) {
            this.addTag(s);
        }
    }

    // REQUIRES: rating in [1, 10], and status == 3
    // MODIFIES: this
    // EFFECTS: set the rating
    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(String started) {
        this.dateStarted = started;
    }

    public String getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(String finished) {
        this.dateFinished = finished;
    }

    public int getCurrentEpisode() {
        return currentEpisode;
    }

    public void setCurrentEpisode(int currentEpisode) {
        this.currentEpisode = currentEpisode;
    }

    public int getTotalEpisodes() {
        return totalEpisodes;
    }

    public int getRating() {
        return rating;
    }

    public int getStatus() {
        return status;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public static List<String> getAllTags() {
        return allTags;
    }

    public static void setAllTags(List<String> tags) {
        allTags = tags;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void setComments(String s) {
        this.comments = s;
    }

    public String getComments() {
        return comments;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
