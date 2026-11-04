public class Course
{
    private final String code;
    private final String title;
    private final String description;
    private final String schedule;
    private final int capacity;
    private final int enrolled;

    public Course(String code, String title, String description, String schedule, int capacity, int enrolled) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.schedule = schedule;
        this.capacity = capacity;
        this.enrolled = enrolled;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSchedule() {
        return schedule;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getEnrolled() {
        return enrolled;
    }

    public int getAvailableSlots()
    {
        return capacity - enrolled;
    }

    public boolean isFull()
    {
        return getAvailableSlots() <= 0;
    }
}
