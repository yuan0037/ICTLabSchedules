ICTLabSchedules
===============
A little calendar app to load lab name list remotely (via JSON), and tap on each lab will show that lab's schedule in a calendar view similar to Android's native calendar app. 


About this project:
This project loads a list of labs from a JSON string provided by a remote service;
then when user taps on any lab, it will check if it already has that lab's schedule 
cached; if not, it will go to an URL to retrive that lab's schedule in JSON format; 
The it displays the schedule using a WeekView object. Other implmentations (display schedule with gridLayout and tableLayout) are also included in this project. 

The app can detect any class cross multiple hours and only show one "cell" for it;


Controls used in this app:
WeekView (from https://github.com/alamkanak/Android-Week-View)
TextView, gridLayout, tableLayout;

Other stuffs used in this app:
Passing object from one activity to another, and get result from another activity;

Concepts used: 
MVC

Screenshots:
![ScreenShot](/screenshots/mainactivity.png)