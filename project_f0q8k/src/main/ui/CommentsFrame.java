package ui;

import model.Entry;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// represent the frame which allows editing the comments
public class CommentsFrame extends JFrame implements ActionListener {
    private JPanel commentsPanel;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JButton saveButton;
    private Entry entry;

    // construct the frame, with a scrollPane and a button
    public CommentsFrame(String name, Entry entry) {
        super(name);
        this.entry = entry;
        this.saveButton = new JButton("SAVE");
        this.saveButton.addActionListener(this);
        this.textArea = new JTextArea(300, 300);
        this.textArea.setText(entry.getComments());
        this.scrollPane = new JScrollPane(textArea);
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.scrollPane.setBounds(0, 0, 300, 300);
        this.saveButton.setBounds(0, 300, 300, 50);
        this.commentsPanel = new JPanel();
        this.commentsPanel.add(saveButton);
        this.commentsPanel.setLayout(null);
        this.commentsPanel.add(this.scrollPane);
        this.setContentPane(this.commentsPanel);
        this.setSize(350, 400);
    }

    // MODIFIES: entry
    // EFFECTS: set the comments of the entry to the text in the textArea, if nothing input then do nothing
    @Override
    public void actionPerformed(ActionEvent e) {
        if (textArea.toString().length() != 0) {
            entry.setComments(textArea.getText());
        }
    }
}
