import javax.swing.*;
import java.awt.*;
import java.util.List;

public class QuizFrame extends JFrame
{
    private static final int TIME_PER_QUESTION = 15;
    private final List<Question> questions;
    private final int[] selectedAnswers;
    private int currentIndex = 0;
    private int score = 0;
    private int timeLeft;
    private Timer timer;

    private final JLabel progressLabel = new JLabel();
    private final JLabel timerLabel = new JLabel();
    private final JLabel questionLabel = new JLabel();
    private final JRadioButton[] optionButtons = new JRadioButton[4];
    private final ButtonGroup optionGroup = new ButtonGroup();
    private final JButton submitButton = new JButton("Submit");

    public QuizFrame(List<Question> questions)
    {
        this.questions = questions;
        this.selectedAnswers = new int[questions.size()];

        setTitle("Java Quiz");
        setSize(600, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);
        loadQuestion();
    }

    private JPanel buildTopPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));
        panel.add(progressLabel, BorderLayout.WEST);
        panel.add(timerLabel, BorderLayout.EAST);
        return panel;
    }

    private JPanel buildCenterPanel()
    {
        JPanel panel = new JPanel(new GridLayout(5, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        questionLabel.setFont(questionLabel.getFont().deriveFont(Font.BOLD, 16f));
        panel.add(questionLabel);

        for (int i = 0; i < optionButtons.length; i++)
        {
            optionButtons[i] = new JRadioButton();
            optionGroup.add(optionButtons[i]);
            panel.add(optionButtons[i]);
        }
        return panel;
    }

    private JPanel buildBottomPanel()
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        submitButton.addActionListener(e -> onSubmitClicked());
        panel.add(submitButton);
        return panel;
    }

    private void loadQuestion()
    {
        Question q = questions.get(currentIndex);

        progressLabel.setText("Question " + (currentIndex + 1) + " of " + questions.size());
        questionLabel.setText("<html>" + q.getText() + "</html>");

        List<String> options = q.getOptions();

        for(int i = 0; i < optionButtons.length; i++)
        {
            optionButtons[i].setText(options.get(i));
        }

        optionGroup.clearSelection();

        startTimer();
    }

    private void startTimer()
    {
        if(timer == null)
        {
            timer = new Timer(1000, e -> tick());
        }
        timeLeft = TIME_PER_QUESTION;
        updateTimerLabel();
        timer.restart();
    }

    private void tick()
    {
        timeLeft--;
        updateTimerLabel();
        if (timeLeft <= 0)
        {
            submitAnswer();
        }
    }

    private void updateTimerLabel()
    {
        timerLabel.setText("Time left: " + timeLeft + "s");
        timerLabel.setForeground(timeLeft <= 5
                ? Color.RED
                : UIManager.getColor("Label.foreground"));
    }

    private int getSelectedIndex()
    {
        for (int i = 0; i < optionButtons.length; i++)
        {
            if (optionButtons[i].isSelected())
            {
                return i;
            }
        }
        return -1;
    }

    private void onSubmitClicked()
    {
        if(getSelectedIndex() == -1)
        {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Please select an option.",
                    "No answer", JOptionPane.WARNING_MESSAGE);
            timer.start();
            return;
        }
        submitAnswer();
    }

    private void submitAnswer()
    {
        timer.stop();

        int selected = getSelectedIndex();
        selectedAnswers[currentIndex] = selected;
        if(questions.get(currentIndex).isCorrect(selected))
        {
            score++;
        }
        currentIndex++;
        if(currentIndex < questions.size())
        {
            loadQuestion();
        }
        else
        {
            finishQuiz();
        }
    }

    private void finishQuiz()
    {
        submitButton.setEnabled(false);
        for (JRadioButton b : optionButtons)
        {
            b.setEnabled(false);
        }
        JOptionPane.showMessageDialog(this, buildResultText(),
                "Results", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private String buildResultText()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Score: %d / %d (%.0f%%)%n%n",
                score, questions.size(), score * 100.0 / questions.size()));

        for (int i = 0; i < questions.size(); i++)
        {
            Question q = questions.get(i);
            int picked = selectedAnswers[i];
            String correct = q.getOptions().get(q.getCorrectIndex());

            if (picked == -1)
                sb.append("Q").append(i + 1).append(": Unanswered | Correct: ").append(correct);
            else if (q.isCorrect(picked))
                sb.append("Q").append(i + 1).append(": Correct");
            else
                sb.append("Q").append(i + 1).append(": Wrong | You: ")
                        .append(q.getOptions().get(picked)).append(" | Correct: ").append(correct);
            sb.append("\n");
        }
        return sb.toString();
    }

}