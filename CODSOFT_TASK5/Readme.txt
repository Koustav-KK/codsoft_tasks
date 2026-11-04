Description:
A course registration system built in Java with an SQLite database accessed through JDBC. Students log in with their ID, view available courses with schedules and remaining slots, and register for or drop courses. The system enforces course capacity, prevents duplicate registrations, and stores all data persistently between runs. Enrollment counts are calculated with SQL joins, so they always stay accurate.

Key features:

Student login and course listing with live slot availability
Register/drop with validation (course exists, not already registered, not full)
Persistent storage with a relational schema (courses, students, registrations)

Tech: Java, JDBC, SQLite, SQL (JOIN, GROUP BY, foreign keys), PreparedStatement
