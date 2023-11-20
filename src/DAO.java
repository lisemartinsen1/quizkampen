import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DAO {
    private Database database = new Database();
    private List<QuestionAndAnswers> questionAndAnswersList;
    private List<QuestionAndAnswers> alreadyUsedQuestionsList;

    public DAO(){
        this.alreadyUsedQuestionsList = new ArrayList<>();
    }

    public QuestionAndAnswers getRandomQuestionAndAnswers() {
        questionAndAnswersList = database.readQuestionsAndAnswersFromFile();

        if (questionAndAnswersList == null || questionAndAnswersList.isEmpty()) {
            return new QuestionAndAnswers("No questions available. Call readQuestionsAndAnswersFromFile() first.", "");
        }



        Random random = new Random();
        int randomIndex;
        QuestionAndAnswers randomQuestion;

        do {
            randomIndex = random.nextInt(questionAndAnswersList.size());
            randomQuestion = questionAndAnswersList.get(randomIndex);
        } while (alreadyUsedQuestionsList.contains(randomQuestion));

        alreadyUsedQuestionsList.add(randomQuestion);

        return randomQuestion;
    }
}