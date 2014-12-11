ICTLabSchedules
===============
Self assessment: I finished A grade; 
For A+ grade, I finished cell merging feature using gridLayout; 

About this project:
This project loads a list of labs from a JSON string provided by a remote service;
then when user taps on any lab, it will check if it already has that lab's schedule 
cached; if not, it will go to an URL to retrive that lab's schedule in JSON format; 
The it displays the schedule using a gridLayout. 
The schedule is displayed by multi-column grid. Each column stands for a day of the week. 
The app can detect any class cross multiple hours and only show one "cell" for it;
The app also has a fixed time-ruler that does not scoll with the content. 

Controls used in this app:
TextView, gridLayout, tableLayout;

Other things used in this app:
Parsing object from one activity to another, and get result from another activity;

Concepts used: 
MVC

Screenshots:
![ScreenShot](/screenshots/mainactivity.png)
![ScreenShot](/screenshots/detailactivity.png)