🪙 Java Expense Tracker

Hi! This is my first full desktop application built using Java. I created this project to learn how to build real-world software with a Graphical User Interface (GUI) and how to save data into files.

📖 About the Project
I wanted to build a tool that helps people manage their daily spending. This app lets you type in your expenses, choose a category, and save them. It even shows you a "Dashboard" at the top so you can see your total spending at a glance!

What I learned while building this:
Java Swing: How to make windows, buttons, and tables.

Object-Oriented Programming (OOP): Using different classes for different jobs.

File Handling: Saving and loading data from a .csv file so the data stays there even after closing the app.

Layouts: Making the app look organized and neat.

✨ Features
✅ Add Expense: Enter the amount, category, date, and what you bought.

✅ Edit/Update: Click on any row in the table to change the details.

✅ Delete: Remove an expense if you made a mistake.

✅ Dashboard: Automatically calculates your total money spent and your most used category.

✅ Search: A search bar to find specific expenses by their description.

✅ Auto-Save: Everything is saved into a file called expenses.csv automatically.

📂 My Code Structure
I split the code into 4 files to keep it organized:

Expense.java: Holds the data for a single expense (ID, Amount, etc.).

ExpenseManager.java: Does all the math and logic for the list.

FileHandler.java: The part that talks to the computer's hard drive to save the file.

ExpenseTrackerGUI.java: The main file that builds the window and buttons you see.

🚀 How to Run It on Your Computer

1. Get the files
Download all the .java files into one folder.

2. Compile the code
Open your terminal or command prompt inside that folder and type:
javac *.java

4. Run the app
After compiling, type:
java ExpenseTrackerGUI

Note: You need to have Java (JDK) installed on your computer to run this!

📊 Where is my data?
When you add your first expense, the app creates a file called expenses.csv in the same folder. You can even open this file in Microsoft Excel or Notepad to see your data!



Thanks for checking out my project!
