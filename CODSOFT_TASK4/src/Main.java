public class Main
{
    public static void main(String[] args)
    {
        Quiz quiz = new Quiz(QuestionBank.getQuestions());
        quiz.start();
    }
}