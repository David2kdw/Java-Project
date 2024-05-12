# My Personal Project

## Project Proposal
This application will act as a journal to keep track of the animes the user *finished*, *currently watching*, 
and the *want to watch* animes. The user is able to create the entry
of each of the anime, including its name, thumbnail image, description, 
the date finished watching, and the user's personal rating and tags. The user can 
sort the entries by date finished and rating. Entries can be filtered by 
different tags.

This app is for those who are  interested in anime or TV series to record their 
feelings after watching each show. It can also show how the preferred type of shows varies
from time to time.

I am interested in this project because I personally like to keep track of the animes
I've watched, and writing down something after watching each one of them.
I think it will be very cool to have such an app to do these things in a simple way, 
and present the work visually.

## User Stories
- I want to be able to create an entry, including the anime's name,
  description, total episodes, and an image, then categorize it into the *finished* list, *currently watching* list, or
  the *want to watch* list.
- I want to add tags either from existing tags or new tags to an entry when creating it.
- I want to rate the entry and give it a comment when finishing it.
- I want to be able to update one entry in the *currently watching* in able to increment the *episodes
  have watched* by one.
- I want to be able to update one entry in the *currently watching* in able to finish the
  whole show, and move it into the *finished* list.
- I want to be able to edit the comments of the finished entries.
- I want to be able to view the *currently watching* list.
- I want to be able to find all entries with the tag "sci-fi" in the *finished* list.
- I want to be able to sort entries in *finished* list by rating, from high to low.
- I want to see a timeline that shows that what time I finished what show.
- I want to have the option to save my lists and timeline to file.
- When I start the application, I want to be given the option to load my lists from file.

# Instructions for Grader

- You can generate the first required action related to the user story "adding multiple Xs to a Y" by clicking "Create
  New Entry". The last step of creating an entry requires selecting an image.
- You can view the list of Xs by clicking one of the Display list buttons. To see more detailed
  information about an entry, first select the entry in the list at top left, then click "Display Entry Info".
- You can view the timeline at the bottom left.
- You can view the finished entries sorted by rating from high to low by first clicking "Display finished list"
  then click "Sort By Rating".
- You can enter a tag below the "Sort By Tag" button then filter the lists by the tag.
- You can locate my visual component at the bottom centre.
- You can save the state of my application by clicking "SAVE".
- You can reload the state of my application by clicking "LOAD".

# Phase 4: Task 2

- Tue Nov 28 17:40:39 PST 2023 \
  Started watching MEGALO BOX
- Tue Nov 28 17:41:03 PST 2023 \
  Finished watching MEGALO BOX
- Tue Nov 28 17:41:45 PST 2023 \
  Steins Gate created
- Fri Dec 01 15:37:02 PST 2023 \
  Watched MEGALO BOX 1/13

# Phase 4: Task 3

An improvement might be creating a new class AllLists which has a list as its field which contains FinishedList, WatchingList,
ToWatchList, so both the entries and the three lists can change state within the scope of the class 
AllList, rather than in a method in Gui. It would also be easier to assign an entry to the corresponding list,
because the status of the entry can be used as an index to find the list in the ArrayList of AllLists class.