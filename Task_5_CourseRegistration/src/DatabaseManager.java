import java.sql.*;

public class DatabaseManager
{
    private static final String URL = "jdbc:sqlite:courses.db";

    private DatabaseManager() {}

    public static Connection getConnection() throws SQLException
    {
        Connection conn = DriverManager.getConnection(URL);
        try(Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }

    public static void initialize()
    {
        try (Connection conn = getConnection())
        {
            createTables(conn);
            seedData(conn);
        } catch (SQLException e)
        {
            System.out.println("Database setup failed: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void createTables(Connection conn) throws SQLException
    {
        try(Statement st = conn.createStatement())
        {
            st.execute("""
                            CREATE TABLE IF NOT EXISTS courses(
                                code        TEXT PRIMARY KEY,
                                title       TEXT NOT NULL,
                                description TEXT,
                                capacity    INTEGER NOT NULL,
                                schedule    TEXT
                            )""");

            st.execute("""
                           CREATE TABLE IF NOT EXISTS students(
                               id TEXT PRIMARY KEY,
                               name TEXT NOT NULL
                           )""");

            st.execute("""
                        CREATE TABLE IF NOT EXISTS registrations(
                            student_id TEXT NOT NULL,
                            course_code TEXT NOT NULL,
                            PRIMARY KEY (student_id, course_code),
                            FOREIGN KEY (student_id) REFERENCES students(id),
                            FOREIGN KEY (course_code) REFERENCES courses(code)
                        )""");
        }
    }

    private static void seedData(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT (*) FROM courses")) {
            if (rs.next() && rs.getInt(1) > 0)
                return;
        }


        String courseSql = "INSERT INTO courses VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(courseSql)) {
            addCourse(ps, "CS101", "Intro to Programming", "Java basics, control flow, OOP", 3, "Mon/Wed 10:00");
            addCourse(ps, "CS201", "Data Structures", "Lists, trees, graphs, hashing", 2, "Tue/Thu 11:00");
            addCourse(ps, "CS301", "Database Systems", "SQL, normalization, transactions", 4, "Mon/Wed 14:00");
            addCourse(ps, "CS305", "Operating Systems", "Processes, memory, scheduling", 2, "Fri 09:00");
            addCourse(ps, "CS401", "Software Engineering", "SDLC, testing, design patterns", 5, "Tue/Thu 15:00");
            ps.executeBatch();
        }

        String studentSql = "INSERT INTO students VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(studentSql)) {
            String[][] students = {{"S001", "Koustav Karmakar"}, {"S002", "Kousik Karmakar"}, {"S003", "Manasi Karmakar"}};
            for (String[] s : students) {
                ps.setString(1, s[0]);
                ps.setString(2, s[1]);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void addCourse(PreparedStatement ps, String code, String title,
                                  String desc, int capacity, String schedule) throws SQLException {
        ps.setString(1, code);
        ps.setString(2, title);
        ps.setString(3, desc);
        ps.setInt(4, capacity);
        ps.setString(5, schedule);
        ps.addBatch();
    }
}

