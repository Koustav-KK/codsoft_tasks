import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final RegistrationService service = new RegistrationService();
    private static String studentId;

    public static void main(String[] args) {
        DatabaseManager.initialize();
        System.out.println("===== COURSE REGISTRATION SYSTEM =====");

        if (!login()) {
            System.out.println("Goodbye!");
            return;
        }

        while (true) {
            System.out.println("\n===== MENU =====");
            System.out.println("1. List Courses");
            System.out.println("2. Register for a Course");
            System.out.println("3. Drop a Course");
            System.out.println("4. My Courses");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> printCourses(service.getAllCourses());
                    case "2" -> registerCourse();
                    case "3" -> dropCourse();
                    case "4" -> {
                        List<Course> mine = service.getStudentCourses(studentId);
                        if (mine.isEmpty()) {
                            System.out.println("You are not registered in any course.");
                        } else {
                            printCourses(mine);
                        }
                    }
                    case "5" -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid option. Choose 1-5");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }

    private static boolean login() {
        while (true) {
            System.out.print("Enter Student ID (or q to quit): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Q")) {
                return false;
            }

            try {
                String name = service.findStudentName(input);
                if (name != null) {
                    studentId = input;
                    System.out.println("Welcome, " + name + "!");
                    return true;
                }
                System.out.println("Student ID not found. Try again.");
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
                return false;
            }
        }
    }

    private static void printCourses(List<Course> courses) {
        System.out.println("Code | Title | Schedule | Cap | Slots");
        System.out.println("-".repeat(56));

        for (Course c : courses) {
            String slots = c.isFull()
                    ? "FULL"
                    : String.valueOf(c.getAvailableSlots());

            System.out.println(
                    c.getCode() + " | " +
                            c.getTitle() + " | " +
                            c.getSchedule() + " | " +
                            c.getCapacity() + " | " +
                            slots
            );

            System.out.println("Description: " + c.getDescription());
        }
    }

    private static void registerCourse() throws SQLException {
        printCourses(service.getAllCourses());
        System.out.print("\nEnter course code to register: ");
        String code = scanner.nextLine().trim().toUpperCase();

        switch (service.register(studentId, code)) {
            case SUCCESS -> System.out.println("Registered for " + code + " successfully.");
            case COURSE_NOT_FOUND -> System.out.println("Course " + code + " not found.");
            case ALREADY_REGISTERED -> System.out.println("You are already registered for " + code + ".");
            case COURSE_FULL -> System.out.println("Sorry, " + code + " is full.");
        }
    }


    private static void dropCourse() throws SQLException {
        List<Course> mine = service.getStudentCourses(studentId);
        if (mine.isEmpty()) {
            System.out.println("You have no courses to drop.");
            return;
        }
        printCourses(mine);
        System.out.print("\nEnter course code to drop: ");
        String code = scanner.nextLine().trim().toUpperCase();

        if (service.drop(studentId, code)) {
            System.out.println("Dropped " + code + " successfully.");
        } else {
            System.out.println("You are not registered for " + code + ".");
        }
    }
}