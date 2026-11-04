import java.util.List;

public class Quiz
{
    private static final int TIME_PER_QUESTION = 15;

    private final List<Question> questions;
    private final int[] selectedAnswers;
    private int score = 0;
    private final TimedInput input = new TimedInput();

    public Quiz(List<Question> questions)
    {
        this.questions = questions;
        this.selectedAnswers = new int[questions.size()];
    }

    public void start()
    {
        System.out.println("===== JAVA QUIZ =====");
        System.out.println(questions.size() + " questions, " + TIME_PER_QUESTION
                + " seconds each. Type 1-4 and press Enter.\n");

        for (int i = 0; i < questions.size(); i++)
        {
            displayQuestion(i);
            int answer = askAnswer();
            selectedAnswers[i] = answer;

            if (answer == -1)
            {
                System.out.println("Time's up!\n");
            } else if (questions.get(i).isCorrect(answer))
            {
                score++;
                System.out.println("Correct!\n");
            } else
            {
                System.out.println("Incorrect.\n");
            }
        }
        showResults();

    }

    private void displayQuestion(int index)
    {
        Question q = questions.get(index);
        System.out.println("Question " + (index + 1) + " of " + questions.size());
        System.out.println(q.getText());

        List<String> options = q.getOptions();
        for (int i = 0; i < options.size(); i++)
        {
            System.out.println("  " + (i + 1) + ". " + options.get(i));
        }
    }

    private int askAnswer()
    {
        long deadline = System.currentTimeMillis() + TIME_PER_QUESTION * 1000L;

        while (true)
        {
            long remaining = deadline - System.currentTimeMillis();
            if (remaining <= 0)
            {
                return -1;
            }

            System.out.print("Your answer (" + (remaining + 999) / 1000 + "s left): ");
            String line = input.readLine(remaining);
            if (line == null)
            {
                System.out.println();
                return -1;
            }

            int answer = parseAnswer(line);
            if (answer != -1)
            {
                return answer;
            }
            System.out.println("Invalid input. Enter a number 1-4.");
        }
    }

    private int parseAnswer(String line)
    {
        try {
            int n = Integer.parseInt(line.trim());
            return (n >= 1 && n <= 4) ? n - 1 : -1;
        } catch (NumberFormatException e)
        {
            return -1;
        }
    }

    private void showResults()
    {
        System.out.println("===== RESULTS =====");
        System.out.printf("Score: %d / %d (%.0f%%)%n%n",
                score, questions.size(), score * 100.0 / questions.size());

        for (int i = 0; i < questions.size(); i++)
        {
            Question q = questions.get(i);
            int picked = selectedAnswers[i];
            String correct = q.getOptions().get(q.getCorrectIndex());

            if (picked == -1)
            {
                System.out.println("Q" + (i + 1) + ": Unanswered | Correct: " + correct);
            }
            else if (q.isCorrect(picked))
            {
                System.out.println("Q" + (i + 1) + ": Correct    | " + correct);
            }
            else
            {
                System.out.println("Q" + (i + 1) + ": Incorrect  | You: "
                        + q.getOptions().get(picked) + " | Correct: " + correct);
            }
        }
    }
}
