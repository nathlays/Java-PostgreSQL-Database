# Java-PostgreSQL-Database

This application allows for initialization and CRUD manipulation of a PostgreSQL Database in Java from the command-line, using the PostgreSQL JDBC Driver.

Setup:
* Compile the Java code by using:\
  `javac -cp ".;lib/postgresql-42.7.3.jar" SQLApplication.java`
  
* (Optional) Initialize the PostgreSQL table and its associated values within psql using:\
  `\i /path/to/scripts/dbInitializer.sql`
  
* Execute the application with:\
  `java -cp ".;lib/postgresql-42.7.3.jar" SQLApplication`

Make sure to edit the PostgreSQL server variables (URL, username, password) to ensure a proper connection.

Demonstration video: https://youtu.be/Syy_yesq7og
