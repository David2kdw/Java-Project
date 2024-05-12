package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// represents the graphical user interface of the Anime List
public class Gui extends JFrame implements ActionListener, WindowListener {
    private JPanel panel = new JPanel();
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private ToWatchList toWatchList;
    private FinishedList finishedList;
    private WatchingList watchingList;
    private List<ListOfEntry> allLists = new ArrayList<>();
    private JList<Entry> entryJList;
    private DefaultListModel<Entry> listModel;
    private int currentDis;
    private JButton loadButton = new JButton("LOAD");
    private JButton saveButton = new JButton("SAVE");
    private JButton toWatchButton = new JButton("Display to-watch list");
    private JButton watchingButton = new JButton("Display watching list");
    private JButton finishedButton = new JButton("Display finished list");
    private JButton displayInfoButton = new JButton("Display Entry Info");
    private JTextArea info;
    private JTextArea allTags = new JTextArea();
    private JTextArea timeLine;
    private JScrollPane scrollPane;
    private JButton sortByRatingButton = new JButton("Sort By Rating");
    private JButton sortByDateButton = new JButton("Sort By Date");
    private JButton sortByTagButton = new JButton("Sort By Tag");
    private JTextField tagsInput = new JTextField();
    private boolean isRatingSorted;
    private boolean isDateSorted;
    private ImagePainter imagePainter = new ImagePainter("./data/imgs/Sat Nov 18 185909 PST 2023.jpg");
    private JButton createButton = new JButton("Create New Entry");
    private JButton watchButton = new JButton("Watch");
    private JLabel progress = new JLabel("0/0");
    private JButton editCommentsButton = new JButton("Edit Comment");

    // EFFECTS: constructs the gui with the given title
    public Gui(String s) {
        super(s);
        addWindowListener(this);
        initializeFeilds();

        init2();

        setButtons();

        displayInfoButton.setActionCommand("INFO");
        JScrollPane scrollText = getjScrollPaneInfo();
        JScrollPane scrollTimeline = getjScrollPaneTimeline(scrollText);

        allTags.setEditable(false);
        JScrollPane scrollTags = new JScrollPane(allTags);
        scrollTags.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        addComps1(scrollText, scrollTimeline);
        addComp2(scrollTags);
    }

    // MODIFIES: this
    // EFFECTS: set layout, content pane, size
    private void init2() {
        panel.setOpaque(true);
        //panel.setLayout(new BorderLayout());
        panel.setLayout(null);
        // panel.setBounds(0, 0, 700, 700);
        JScrollPane main = new JScrollPane(panel);
        main.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        main.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.setContentPane(main);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,800);
        this.setVisible(true);

        currentDis = 1;
        listModel = new DefaultListModel<>();
        //listModel.addElement(new Entry("no1", "des", 1, 10, false));
        entryJList = new JList<>();
        entryJList.setModel(listModel);
        entryJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //entryJList.setVisibleRowCount(15);
        entryJList.setSelectedIndex(0);
        // selected = listModel.getElementAt(entryJList.getSelectedIndex());
    }

    // MODIFIES: this
    // EFFECTS: add the corresponding listeners to the buttons
    private void setButtons() {
        SaveLoadListener saveLoadListener = new SaveLoadListener();
        loadButton.addActionListener(saveLoadListener);
        loadButton.setActionCommand("load");
        saveButton.addActionListener(saveLoadListener);
        saveButton.setActionCommand("save");

        toWatchButton.addActionListener(this);
        toWatchButton.setActionCommand("1");
        watchingButton.addActionListener(this);
        watchingButton.setActionCommand("2");
        finishedButton.addActionListener(this);
        finishedButton.setActionCommand("3");

        sortByRatingButton.addActionListener(new RatingSortListener());
        sortByRatingButton.setEnabled(false);
        isDateSorted = false;
        sortByDateButton.addActionListener(new DateSortListener());
        sortByTagButton.addActionListener(new TagsSortListener());
        createButton.addActionListener(new CreateNewEntryListener());
        watchButton.addActionListener(new WatchListener());

        editCommentsButton.addActionListener(new EditCommentListener());
    }

    // MODIFIES: this
    // EFFECTS: add components to the panel
    private void addComp2(JScrollPane scrollTags) {
        panel.add(sortByDateButton);
        sortByDateButton.setBounds(500, 250, 200, 50);
        panel.add(sortByTagButton);
        sortByTagButton.setBounds(500, 100, 200, 50);
        panel.add(tagsInput);
        tagsInput.setBounds(500, 150, 200, 50);
        JLabel tagsLabel = new JLabel("All Tags:");
        panel.add(tagsLabel);
        tagsLabel.setBounds(500, 0, 200, 50);
        panel.add(scrollTags);
        scrollTags.setBounds(500, 50, 200, 50);
        panel.add(imagePainter);
        imagePainter.setBounds(300, 400, 200, 300);
        panel.add(createButton);
        createButton.setBounds(550, 400, 200, 50);
        panel.add(watchButton);
        watchButton.setBounds(400, 300, 200, 50);
        panel.add(progress);
        progress.setBounds(500, 360, 40, 10);

        panel.add(editCommentsButton);
        // displayInfoButton.setBounds(200, 300, 200,50);
        editCommentsButton.setBounds(200, 350, 200, 50);
    }

    // MODIFIES: this
    // EFFECTS: add components to the panel
    private void addComps1(JScrollPane scrollText, JScrollPane scrollTimeline) {
        scrollPane = new JScrollPane(entryJList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane);
        scrollPane.setBounds(0, 0, 300, 300);
        panel.add(loadButton);
        loadButton.setBounds(0, 300, 100,100);
        panel.add(saveButton);
        saveButton.setBounds(100, 300, 100,100);
        panel.add(toWatchButton);
        toWatchButton.setBounds(300, 0, 200,100);
        panel.add(watchingButton);
        watchingButton.setBounds(300, 100, 200,100);
        panel.add(finishedButton);
        finishedButton.setBounds(300, 200, 200,100);
        panel.add(displayInfoButton);
        displayInfoButton.setBounds(200, 300, 200,50);
        // info
        panel.add(scrollText);
        scrollText.setBounds(0, 400, 300, 150);
        // timeline
        panel.add(scrollTimeline);
        scrollTimeline.setBounds(0, 550, 300, 150);
        panel.add(sortByRatingButton);
        sortByRatingButton.setBounds(500, 200, 200, 50);
    }

    // MODIFIES: this
    // EFFECTS: initialize 4 lists, and readers
    private void initializeFeilds() {
        toWatchList = new ToWatchList();
        watchingList = new WatchingList();
        finishedList = new FinishedList();
        allLists.add(toWatchList);
        allLists.add(watchingList);
        allLists.add(finishedList);
        jsonWriter = new JsonWriter("");
        jsonReader = new JsonReader("");
        imagePainter.setImageLocation("./data/testImage.png");
    }

    // MODIFIES: this
    // EFFECTS: return the entry info scrollPane
    private JScrollPane getjScrollPaneInfo() {
        info = new JTextArea(30, 30);
        info.setEditable(false);
        DisplayInfoListener displayInfoListener = new DisplayInfoListener();
        displayInfoButton.addActionListener(displayInfoListener);
        JScrollPane scrollText = new JScrollPane(info);
        scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        return scrollText;
    }

    // MODIFIES: this
    // EFFECTS: return the timeline scrollPane
    private JScrollPane getjScrollPaneTimeline(JScrollPane scrollText) {
        timeLine = new JTextArea(30, 30);
        timeLine.setEditable(false);
        JScrollPane scrollTimeline = new JScrollPane(timeLine);
        scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        return scrollTimeline;
    }

    // REQUIRES: int is one of (1, 2, 3)
    // MODIFIES: this
    // EFFECTS: set the current displaying list
    private void setDisplayed(int type) {
        listModel.clear();
        List<Entry> entryArrayList = allLists.get(type - 1).getListOfEntry();
        for (Entry e: entryArrayList) {
            this.listModel.addElement(e);
        }
        entryJList.setModel(listModel);
        isDateSorted = false;
        isRatingSorted = false;
    }

    // MODIFIES: this
    // EFFECTS: use the signal from buttons to change the current displaying list
    @Override
    public void actionPerformed(ActionEvent e) { // includes displaying 3 different lists
        String actionCommand = e.getActionCommand();
        updateTimelineAllTags();
        /*switch (actionCommand) {
            case "1":
                setDisplayed(1);
                this.currentDis = 1;
                sortByRatingButton.setEnabled(false);
                break;
            case "2":
                setDisplayed(2);
                this.currentDis = 2;
                sortByRatingButton.setEnabled(false);
                break;
            case "3":
                setDisplayed(3);
                this.currentDis = 3;
                isRatingSorted = false;
                sortByRatingButton.setEnabled(true);
                break;
        }*/
        setDisplayed(Integer.parseInt(actionCommand));
        currentDis = Integer.parseInt(actionCommand);
        if (Integer.parseInt(actionCommand) <= 2) { // to watch, watching
            sortByRatingButton.setEnabled(false);
            watchButton.setEnabled(true);
            editCommentsButton.setEnabled(false);
        } else { // finished
            isRatingSorted = false;
            sortByRatingButton.setEnabled(true);
            watchButton.setEnabled(false);
            editCommentsButton.setEnabled(true);
        }
        isDateSorted = false;
        System.out.println("currentDis: " + currentDis);
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    // EFFECTS: print the eventLog when the window is closing
    @Override
    public void windowClosing(WindowEvent e) {
        EventLog eventLog = EventLog.getInstance();
        System.out.println("_________________________________________________");
        System.out.println("EventLog:");
        for (Event event : eventLog) {
            System.out.println(event);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    // represents the ActionListener which handles displayEntryInfo Button
    private class DisplayInfoListener implements ActionListener {
        // EFFECTS: set the text of the info scrollPane and the image
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = entryJList.getSelectedIndex();
            // System.out.println(index);
            if (index >= 0) {
                Entry entry = listModel.getElementAt(entryJList.getSelectedIndex());
                // System.out.println(entry);
                Gui.this.info.setText(entry.toString());
                // System.out.println(entry.getImagePath());
                imagePainter.setImageLocation(entry.getImagePath());
                imagePainter.updateUI();
                progress.setText((entry.getCurrentEpisode())
                        + "/" + (entry.getTotalEpisodes()));
            }
        }
    }

    // represents the ActionListener which handles sortByRating Button
    private class RatingSortListener implements ActionListener {
        // change the displayed list, sort by rating, if already sorted then returns to original
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Is rating sorted:" + isRatingSorted);
            if (!isRatingSorted) {
                listModel.clear();
                List<Entry> entryArrayList = finishedList.sortByRating(); // get finished list
                for (Entry entry: entryArrayList) {
                    listModel.addElement(entry);
                }
                entryJList.setModel(listModel);
                isRatingSorted = true;
            } else {
                setDisplayed(3);
                isRatingSorted = false;
            }
        }
    }

    // represents the ActionListener which handles Save and Load Button
    private class SaveLoadListener implements ActionListener {
        // EFFECTS: save or load all the data
        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            // updateTimelineAllTags();
            int select = JOptionPane.showConfirmDialog(Gui.this,
                    "Confirm Operation?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (select == 0) {
                switch (actionCommand) {
                    case "load":
                        try {
                            loadALl();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    case "save":
                        try {
                            saveAll();
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                }
            }
            setDisplayed(currentDis);
            updateTimelineAllTags();
        }
    }

    // represents the ActionListener which handles sortByDate Button
    private class DateSortListener implements ActionListener {
        // EFFECTS: sort the displayed list by date created
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Is date sorted:" + isDateSorted);
            if (!isDateSorted) {
                listModel.clear();
                List<Entry> entryArrayList = allLists.get(currentDis - 1).sortByDate(); // get finished list
                for (Entry entry: entryArrayList) {
                    listModel.addElement(entry);
                }
                entryJList.setModel(listModel);
                isDateSorted = true;
            } else {
                setDisplayed(currentDis);
                isDateSorted = false;
            }
        }
    }

    // represents the ActionListener which handles filterByTag Button
    private class TagsSortListener implements ActionListener {
        // EFFECTS: filter the displayed list by the input tag
        @Override
        public void actionPerformed(ActionEvent e) {
            String tag = tagsInput.getText();
            if (tag.length() != 0) {
                List<Entry> filtered = allLists.get(currentDis - 1).filterByTag(tag);
                listModel.clear();
                for (Entry entry: filtered) {
                    listModel.addElement(entry);
                }
                entryJList.setModel(listModel);
                tagsInput.setText(null);
            }
        }
    }

    // represents the ActionListener which handles CreateNewEntry Button
    private class CreateNewEntryListener implements ActionListener {
        // EFFECTS: use input dialogs to input the data of new entry
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = JOptionPane.showInputDialog("Enter title:");
            String description = JOptionPane.showInputDialog("Enter description:");
            int total = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of total episodes:"));
            int status = Integer.parseInt(JOptionPane.showInputDialog(
                    "Enter the status (1: to be watched 2: watching 3: finished):"));
            Entry newEntry = new Entry(title, description, status, total, false);
            int tagNum = Integer.parseInt(JOptionPane.showInputDialog("Enter the Number of tags:"));
            for (int i = 1; i <= tagNum; i++) {
                String tag = JOptionPane.showInputDialog("Enter tag:");
                newEntry.addTag(tag);
            }
            allLists.get(status - 1).addEntry(newEntry);
            if (status >= 3) {
                int rating = Integer.parseInt(JOptionPane.showInputDialog(
                        "Enter the rating of the finished anime [0, 10]:"));
                newEntry.setRating(rating);
                String comments = JOptionPane.showInputDialog("Add the comments to this finished anime:");
                newEntry.setComments(comments);
            }
            chooseFile(newEntry);
            updateTimelineAllTags();
        }
    }

    // EFFECTS: add the entry to the corresponding list
    private void chooseFile(Entry newEntry) {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("imgs",
                "jpg", "jpeg", "png");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        int choice = fileChooser.showOpenDialog(Gui.this);
        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            File newFile = new File("./data/imgs/" + getFileName(newEntry.getDateCreated()) + ".jpg");
            newEntry.setImagePath("./data/imgs/" + getFileName(newEntry.getDateCreated()) + ".jpg");
            System.out.println(newFile.getPath());
            try {
                copyFileUsingChannel(file, newFile);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    // EFFECTS: remove all the ":" in the file name
    private String getFileName(String s) {
        return s.replace(":", "");
    }

    // EFFECTS: copy the file in source, to the file in dest
    // https://www.digitalocean.com/community/tutorials/java-copy-file
    private void copyFileUsingChannel(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            sourceChannel.close();
            destChannel.close();
        }
    }

    // represents the ActionListener which handles Watch Button
    private class WatchListener implements ActionListener {
        // EFFECTS: watch the entry, replace it to different lists if needed
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = entryJList.getSelectedIndex();
            if (index >= 0) {
                Entry entry = listModel.getElementAt(entryJList.getSelectedIndex());
                int status = entry.getStatus();
                if (status == 1) {
                    entry.beginWatching();
                    toWatchList.getListOfEntry().remove(entry);
                    watchingList.addEntry(entry);
                } else if (status == 2) {
                    boolean finished = entry.watch();
                    if (finished) {
                        finishes(entry);
                    }
                }
                progress.setText((entry.getCurrentEpisode())
                        + "/" + (entry.getTotalEpisodes()));
            }
            setDisplayed(currentDis);
            updateTimelineAllTags();

        }
    }

    // represents the ActionListener which handles Edit Comment Button
    private class EditCommentListener implements ActionListener {
        // EFFECTS: open a new CommentsFrame and pass the selected entry to it
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = entryJList.getSelectedIndex();
            // System.out.println(index);
            if (index >= 0) {
                Entry entry = listModel.getElementAt(entryJList.getSelectedIndex());
                CommentsFrame commentsFrame = new CommentsFrame("Edit Comments", entry);
                commentsFrame.setVisible(true);
            }
        }
    }

    // EFFECTS: finishes the entry, adding its rating and comments
    private void finishes(Entry entry) {
        watchingList.getListOfEntry().remove(entry);
        finishedList.addEntry(entry);
        int rating = Integer.parseInt(JOptionPane.showInputDialog(
                "Enter the rating of the finished anime [0, 10]:"));
        entry.setRating(rating);
        String comments = JOptionPane.showInputDialog("Add the comments to this finished anime:");
        entry.setComments(comments);
    }

    // EFFECTS: update the displayed timeline and allTags
    private void updateTimelineAllTags() {
        String timeline = "";
        String allTags = "";
        for (String s: Time.getTimeLine()) {
            timeline += s + "\n";
        }
        for (String s: Entry.getAllTags()) {
            allTags += s + "\n";
        }
        timeLine.setText(timeline);
        this.allTags.setText(allTags);
    }

    // EFFECTS: load all data
    private void loadALl() throws IOException {
        loadTimeLine();
        loadAllTags();
        loadToWatch();
        loadWatching();
        loadFinished();
        toWatchList.loadingComplete();
        watchingList.loadingComplete();
        finishedList.loadingComplete();
        System.out.println("LOADED");
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

    // EFFECTS: save all files
    private void saveAll() throws FileNotFoundException {
        saveToWatch();
        saveWatching();
        saveFinished();
        saveTimeLine();
        saveAllTags();
        System.out.println("SAVED");
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

    public static void main(String[] args){
        Gui gui = new Gui("Anime List");
        gui.setVisible(true);
    }
}
