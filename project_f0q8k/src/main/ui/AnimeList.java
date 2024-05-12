package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

// represents the anime list application
public class AnimeList {
    private ToWatchList toWatchList;
    private WatchingList watchingList;
    private FinishedList finishedList;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private Scanner scanner1;
    private boolean isQuit;

    // EFFECTS: construct an animeList, with 3 empty lists
    public AnimeList() {
        toWatchList = new ToWatchList();
        watchingList = new WatchingList();
        finishedList = new FinishedList();
        jsonWriter = new JsonWriter("");
        jsonReader = new JsonReader("");
        scanner1 = new Scanner(System.in);
        isQuit = false;
    }

    // MODIFIES: this
    // EFFECTS: present the home page, can create entry, view lists, update entries, and view timeline
    public void homePage() throws IOException {
        // boolean isQuit = false;
        while (!isQuit) {
            int choice;
            System.out.println("This is the home page.\nSelect from 1 to 7:");
            System.out.print("1: Create entry,\n2: View one of the list,\n3: Update an entry,\n"
                    + "4: View timeline,\n5: Save,\n6: Load,\n7: Quit\nChoice:");
            choice = scanner1.nextInt();
            switch (choice) {
                case 1: createEntry();
                    break;
                case 2: viewList();
                    break;
                case 3: updateEntry();
                    break;
                case 4: viewTimeLine();
                    break;
                case 5: saveAll();
                    break;
                case 6: loadALl();
                    break;
                case 7: isQuit = true;
                    break;
            }
        }
    }

    // EFFECTS: save all data (3 lists, timeline, all tags).
    private void saveAll() throws FileNotFoundException {
        longLine();
        System.out.println("SAVED");
        saveToWatch();
        saveWatching();
        saveFinished();
        saveTimeLine();
        saveAllTags();
        longLine();
    }

    // EFFECTS: save ToWatch list to data folder
    private void saveToWatch() throws FileNotFoundException {
        jsonWriter.setDestination("./data/toWatch.json");
        jsonWriter.open();
        jsonWriter.write(toWatchList);
        jsonWriter.close();
    }

    // EFFECTS: save Watching list
    private void saveWatching() throws FileNotFoundException {
        jsonWriter.setDestination("./data/watching.json");
        jsonWriter.open();
        jsonWriter.write(watchingList);
        jsonWriter.close();
    }

    // EFFECTS: save Finished list
    private void saveFinished() throws FileNotFoundException {
        jsonWriter.setDestination("./data/finished.json");
        jsonWriter.open();
        jsonWriter.write(finishedList);
        jsonWriter.close();
    }

    // EFFECTS: save timeline
    private void saveTimeLine() throws FileNotFoundException {
        jsonWriter.setDestination("./data/timeLine.json");
        jsonWriter.open();
        jsonWriter.write(Time.getTimeLine());
        jsonWriter.close();
    }

    // EFFECTS: save all tags
    private void saveAllTags() throws FileNotFoundException {
        jsonWriter.setDestination("./data/allTags.json");
        jsonWriter.open();
        jsonWriter.write(Entry.getAllTags());
        jsonWriter.close();
    }

    // EFFECTS: load all data from data folder
    private void loadALl() throws IOException {
        longLine();
        System.out.println("LOADED");
        loadTimeLine();
        loadAllTags();
        loadToWatch();
        loadWatching();
        loadFinished();
        longLine();
        toWatchList.loadingComplete();
        watchingList.loadingComplete();
        finishedList.loadingComplete();
    }

    // EFFECTS: load timeline
    private void loadTimeLine() throws IOException {
        jsonReader.setSource("./data/timeLine.json");
        Time.setTimeLine(jsonReader.readStringList());
    }

    // EFFECTS: load all tags
    private void loadAllTags() throws IOException {
        jsonReader.setSource("./data/allTags.json");
        Entry.setAllTags(jsonReader.readStringList());
    }

    // EFFECTS: load toWatch list
    private void loadToWatch() throws IOException {
        jsonReader.setSource("./data/toWatch.json");
        this.toWatchList.copy(jsonReader.read());
    }

    // EFFECTS: load watching list
    private void loadWatching() throws IOException {
        jsonReader.setSource("./data/watching.json");
        this.watchingList.copy(jsonReader.read());
    }

    // EFFECTS: load finished list
    private void loadFinished() throws IOException {
        jsonReader.setSource("./data/finished.json");
        this.finishedList.copy(jsonReader.read());
    }


    // MODIFIES: this
    // EFFECTS: create a new entry, add to the specified list
    private void createEntry() {
        longLine();
        Scanner scanner = new Scanner(System.in);
        String title;
        String description;
        int status;
        int totalEpisodes;
        System.out.println("CREATING ENTRY");
        System.out.print("Enter the title:");
        title = scanner.nextLine();
        System.out.print("Enter the description:");
        description = scanner.nextLine();
        System.out.print("Enter the number of total episodes:");
        totalEpisodes = scanner.nextInt();
        System.out.print("Enter the status (1: to be watched 2: watching 3: finished):");
        status = scanner.nextInt();
        Entry newEntry = new Entry(title, description, status, totalEpisodes, false);
        chooseTags(newEntry);
        addToList(newEntry);
    }

    // MODIFIES: this
    // EFFECTS: helper function of createEntry(), add the entry to the list
    private void addToList(Entry entry) {
        Scanner scanner = new Scanner(System.in);
        int status = entry.getStatus();
        switch (status) {
            case 1:
                toWatchList.addEntry(entry);
                break;
            case 2:
                watchingList.addEntry(entry);
                break;
            case 3:
                System.out.print("Enter the rating of the finished anime [0, 10]:");
                int rating = scanner.nextInt();
                entry.setRating(rating);
                System.out.print("Post the comments to this finished anime:");
                Scanner scanner2 = new Scanner(System.in);
                String comments = scanner2.nextLine();
                entry.setComments(comments);
                finishedList.addEntry(entry);
                break;
        }
        longLine();
    }

    // MODIFIES: this, entry
    // EFFECT: display all the tags, and add the tags input to the entry
    private void chooseTags(Entry entry) {
        int tagNum;
        Scanner scanner = new Scanner(System.in);
        shortLine();
        System.out.println("ADDING TAGS");
        System.out.println("EXISTING TAGS ARE:" + Entry.getAllTags());
        System.out.print("Enter number of tags:");
        tagNum = scanner.nextInt();
        String tag;
        for (int i = 1; i <= tagNum; i++) {
            System.out.print("Enter tag:");
            tag = scanner.next();
            entry.addTag(tag);
        }
    }

    // EFFECTS: display the selected list, both to-watch and watching list can only sort by time and filter by tag,
    //          while finished list can also sort by rating
    private void viewList() {
        longLine();
        Scanner scanner = new Scanner(System.in);
        int choice;
        int choice1 = 0; // 1 for time, 3 for filter
        int choice2 = 0; // 1 for time sort, 2 for rating sort, 3 for filter
        System.out.println("VIEW LIST");
        System.out.print("Select list (1: to-watch List  2: watching List  3: finished List):");
        choice = scanner.nextInt();
        if (choice == 3) {
            System.out.print("Select view type (1: sorted by time  2: sorted by rating  3: filter by tag):");
            choice2 = scanner.nextInt();
        } else {
            System.out.print("Select view type (1: sorted by time  3: filter by tag):");
            choice1 = scanner.nextInt();
        }
        chooseListDisplay(choice, choice1, choice2);
        longLine();
    }

    // REQUIRES: choice==1,2,3  choice1==1,3  choice2==1,2,3
    // EFFECTS: display the list with the given choice,
    //          choice 1 determines which list,
    //          choice 2 determines sort by time or filter, for to-watch and watching
    //          choice 3 determines sort by time or by rating or filter , for finished list
    private void chooseListDisplay(int choice, int choice1, int choice2) {
        switch (choice) {
            case 1:
                if (toWatchList.getListOfEntry().size() == 0) {
                    System.out.println("EMPTY LIST");
                } else {
                    display1or2(1, choice1);
                }
                break;
            case 2:
                if (watchingList.getListOfEntry().size() == 0) {
                    System.out.println("EMPTY LIST");
                } else {
                    display1or2(2, choice1);
                }
                break;
            case 3:
                if (finishedList.getListOfEntry().size() == 0) {
                    System.out.println("EMPTY LIST");
                } else {
                    display3(finishedList, choice2);
                }
                break;
        }
    }

    // REQUIRES: choice==1,2  choice1==1,3
    // EFFECTS: display to-watch or watching list with the given choice
    private void display1or2(int choice, int choice1) {
        switch (choice) {
            case 1:
                if (choice1 == 1) {
                    displayListByDate(toWatchList);
                } else {
                    displayByFilter(toWatchList);
                }
                break;
            case 2:
                if (choice1 == 1) {
                    displayListByDate(watchingList);
                } else {
                    displayByFilter(watchingList);
                }
                break;
        }
    }

    // REQUIRES: list not empty
    // EFFECTS: display the list filtered by tag provided
    private void displayByFilter(ListOfEntry listOfEntry) {
        String tag;
        Scanner scanner = new Scanner(System.in);
        System.out.println("EXISTING TAGS ARE:" + Entry.getAllTags());
        System.out.print("Enter the tag used to filter:");
        tag = scanner.next();
        List<Entry> displayed = listOfEntry.filterByTag(tag);
        printList(displayed);
    }

    // REQUIRES: list not empty
    // EFFECTS: display the list by time, from new to old
    private void displayListByDate(ListOfEntry listOfEntry) {
        List<Entry> displayed = listOfEntry.sortByDate();
        printList(displayed);
    }

    // REQUIRES: choice2==1,2,3,  list not empty
    // EFFECTS: display the finished list with the choice provided
    private void display3(FinishedList finishedList, int choice2) {
        List<Entry> displayed;
        if (choice2 == 2) { // sort by rating
            displayed = finishedList.sortByRating();
            printList(displayed);
        } else if (choice2 == 1) {
            displayed = finishedList.sortByDate();
            printList(displayed);
        } else {
            displayByFilter(finishedList);
        }
    }

    // list not empty
    // EFFECTS: print out the list, with an index starts at 1
    private void printList(List<Entry> displayed) {
        shortLine();
        int index = 1;
        for (Entry e : displayed) {
            System.out.println("#" + index);
            System.out.println(e);
            System.out.println();
            System.out.println();
            index++;
        }
    }

    // MODIFIES: this, entry
    // EFFECTS: update one entry either in to-watch or watching list
    private void updateEntry() {
        Scanner scanner = new Scanner(System.in);
        longLine();
        System.out.print("OPERATING ON ENTRY\nEnter which list (1: to-watch  2: watching):");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                if (toWatchList.getListOfEntry().size() == 0) {
                    System.out.println("EMPTY");
                } else {
                    printList(toWatchList.getListOfEntry());
                    startAt();
                }
                break;
            case 2:
                if (watchingList.getListOfEntry().size() == 0) {
                    System.out.println("EMPTY");
                } else {
                    printList(watchingList.getListOfEntry());
                    watchAt();
                }
                break;
        }
        longLine();
    }

    // MODIFIES: this, entry
    // EFFECTS: update one entry in to-watch list
    private void startAt() {
        Scanner scanner = new Scanner(System.in);
        shortLine();
        System.out.print("Choose the index of entry to START:");
        int index = scanner.nextInt() - 1;
        watchingList.addEntry(toWatchList.getListOfEntry().get(index));
        toWatchList.startAtIndex(index);
    }

    // MODIFIES: this, entry
    // EFFECTS: update one entry in watching list, if finishes, ask for a rating
    private void watchAt() {
        Scanner scanner = new Scanner(System.in);
        shortLine();
        System.out.print("Choose the index of entry to WATCH:");
        int index = scanner.nextInt() - 1;
        Entry entry = watchingList.getListOfEntry().get(index);
        int before = entry.getCurrentEpisode();
        boolean isFinished = watchingList.watchAtIndex(index);
        int current = entry.getCurrentEpisode();
        if (isFinished) {
            finishedList.addEntry(entry);
            System.out.print("You have finished watching this anime, give it a rating in [0, 10]:");
            int rating = scanner.nextInt();
            System.out.println("Post the comments to this finished anime:");
            Scanner scanner2 = new Scanner(System.in);
            String comments = scanner2.nextLine();
            entry.setComments(comments);
            entry.setRating(rating);
        } else {
            System.out.println("Current episodes: " + before + "--->" + current);
        }
    }

    // EFFECTS: display the timeline
    private void viewTimeLine() {
        longLine();
        System.out.println("VIEWING TIMELINE");
        System.out.println();
        for (String line: Time.getTimeLine()) {
            System.out.println(line);
            System.out.println();
        }
        longLine();
    }

    // EFFECTS: display a long line
    private void longLine() {
        System.out.println("__________________________________________________________________");
    }

    // EFFECTS: display a short line
    private void shortLine() {
        System.out.println("_________________________________");
    }
}
