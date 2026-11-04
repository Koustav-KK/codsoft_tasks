import java.util.List;

public class QuestionBank
{
    private QuestionBank() {}

    public static List<Question> getQuestions()
    {
        return List.of(
                new Question("Which keyword prevents a class from being inherited?",
                        List.of("static", "final", "private", "abstract"), 1),
                new Question("What is the default value of an int field?",
                        List.of("0", "null", "1", "undefined"), 0),
                new Question("Which collection does not allow duplicates?",
                        List.of("ArrayList", "LinkedList", "HashSet", "Vector"), 2),
                new Question("What is the size of an int in Java?",
                        List.of("16 bits", "32 bits", "64 bits", "8 bits"), 1),
                new Question("Which method is the JVM entry point?",
                        List.of("start()", "run()", "main()", "init()"), 2),
                new Question("Which is NOT a pillar of OOP?",
                        List.of("Encapsulation", "Inheritance", "Compilation", "Polymorphism"), 2),
                new Question("A String object in Java is:",
                        List.of("Mutable", "Immutable", "A primitive", "An interface"), 1),
                new Question("Accessing arr[10] on a 5-element array throws:",
                        List.of("NullPointerException", "ArrayIndexOutOfBoundsException",
                                "ClassCastException", "ArithmeticException"), 1),
                new Question("For objects, == compares:",
                        List.of("Values", "References", "Hash codes", "Types"), 1),
                new Question("Which package is imported by default?",
                        List.of("java.util", "java.io", "java.lang", "java.net"), 2)
        );
    }
}
