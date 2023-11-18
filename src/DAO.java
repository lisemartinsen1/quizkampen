import java.util.List;
import java.util.Random;

public class DAO {
    private Database database = new Database();
    private List<QuestionAndAnswers> questionAndAnswersList;

    public QuestionAndAnswers getRandomQuestionAndAnswers() {
        questionAndAnswersList = database.readQuestionsAndAnswersFromFile();

        if (questionAndAnswersList == null || questionAndAnswersList.isEmpty()) {
            return new QuestionAndAnswers("No questions available. Call readQuestionsAndAnswersFromFile() first.", "");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(questionAndAnswersList.size());

        return questionAndAnswersList.get(randomIndex);
    }

}