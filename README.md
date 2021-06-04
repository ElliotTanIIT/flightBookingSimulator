# flightBookingSimulator

### Live Demo
https://youtu.be/GivWDZUF2hs

### Objective
To create a flight booking application in Java and PostgreSQL.

### Application Functionality
A user can create an account registering with an email

A user can add/modify/delete their payment or address information within the account that they are logged into.

A user can search for flights and specify certain constraints for the flights that are displayed, and eventually book a flight if so desired.

### Design
The GUI was designed using Java's JFrames. First the layout was set and all the buttons, labels, and text boxes were created and positioned. One of the buttons would also open up a flight search window where users could browse the flights. Within the Java code, pre-written SQL functions are executed to a PostgreSQL server, which is connected via JBDC. The server then returns the query result which results in some action on the GUI. If a user changed their account information or booked/cancelled a flight, the database would be updated.

### Tools Used
Java, Jframe, JBDC
SQL, PostgreSQL
