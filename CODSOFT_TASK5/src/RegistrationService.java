import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegistrationService
{
    public enum RegisterResult {
        SUCCESS, COURSE_NOT_FOUND, ALREADY_REGISTERED, COURSE_FULL
    }

    private static final String COURSE_WITH_COUNT = """
            SELECT c.code, c.title, c.description, c.capacity, c.schedule,
                   COUNT(r.student_id) AS enrolled
                   FROM courses c
                   LEFT JOIN registrations r ON c.code = r.course_code
                   """;

    public String findStudentName(String studentId) throws SQLException {
        String sql = "SELECT name FROM students WHERE id = ?";
        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try(ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("name") : null;
            }
        }
    }

    public List<Course> getAllCourses() throws SQLException
    {
        String sql = COURSE_WITH_COUNT + "GROUP BY c.code ORDER BY c.code";
        List<Course> courses = new ArrayList<>();
        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next())
            {
                courses.add(mapCourse(rs));
            }
        }
        return courses;
    }

    public List<Course> getStudentCourses(String studentID) throws SQLException
    {
        String sql = COURSE_WITH_COUNT
                + "WHERE c.code IN (SELECT course_code FROM registrations WHERE student_id = ?) "
                + "GROUP BY c.code ORDER BY c.code";

        List<Course> courses = new ArrayList<>();
        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentID);

            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    courses.add(mapCourse(rs));
                }
            }
        }
        return courses;
    }

    public RegisterResult register(String studentId, String courseCode) throws SQLException
    {
        Course course = findCourse(courseCode);
        if(course == null)
        {
            return RegisterResult.COURSE_NOT_FOUND;
        }
        if(isRegistered(studentId, courseCode))
        {
            return RegisterResult.ALREADY_REGISTERED;
        }
        if(course.isFull())
        {
            return RegisterResult.COURSE_FULL;
        }

        String sql = "INSERT INTO registrations(student_id, course_code) VALUES (?, ?)";
        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, studentId);
            ps.setString(2, course.getCode());
            ps.executeUpdate();
        }
        return RegisterResult.SUCCESS;
    }

    public boolean drop(String studentId, String courseCode) throws SQLException
    {
        String sql = "DELETE FROM registrations WHERE student_id = ? AND course_code = ?";
        try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, studentId);
            ps.setString(2, courseCode);
            return ps.executeUpdate() > 0;
        }
    }

    private Course findCourse(String code) throws SQLException
    {
        String sql = COURSE_WITH_COUNT + "WHERE c.code = ? GROUP BY c.code";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery())
            {
                return rs.next() ? mapCourse(rs) : null;
            }
        }
    }

    private boolean isRegistered(String studentId, String courseCode) throws SQLException
    {
        String sql = "SELECT 1 FROM registrations WHERE student_id = ? AND course_code = ?";
        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, studentId);
            ps.setString(2, courseCode);

            try (ResultSet rs = ps.executeQuery())
            {
                return rs.next();
            }
        }
    }

    private Course mapCourse(ResultSet rs) throws SQLException
    {
        return new Course(
                rs.getString("code"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("schedule"),
                rs.getInt("capacity"),
                rs.getInt("enrolled"));
    }

}
