package Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DAO {
    private Database database = new Database();
    private List<QuestionAndAnswers> questionAndAnswersList;
    private List<QuestionAndAnswers> alreadyUsedQuestionsList;
    PropertiesClass propertiesClass = new PropertiesClass();
    private int counter = 0;

    public DAO(){
        this.alreadyUsedQuestionsList = new ArrayList<>();
    }

    public QuestionAndAnswers getRandomQuestionAndAnswers(String category) {
        propertiesClass.loadProperties();
        int amountOfQ = propertiesClass.getAmountOfQuestions();

        if (questionAndAnswersList == null || questionAndAnswersList.isEmpty() || counter == amountOfQ) {
            questionAndAnswersList = database.readQuestionsAndAnswersFromFile(category);

            if (questionAndAnswersList == null || questionAndAnswersList.isEmpty()) {
                return new QuestionAndAnswers("No questions available. Call readQuestionsAndAnswersFromFile() first.", "");
            }

            counter = 0;
        }

        Random random = new Random();
        int randomIndex;
        QuestionAndAnswers randomQuestion;

        if (counter < amountOfQ) {
            do {
                randomIndex = random.nextInt(questionAndAnswersList.size());
                randomQuestion = questionAndAnswersList.get(randomIndex);
            } while (alreadyUsedQuestionsList.contains(randomQuestion));

            alreadyUsedQuestionsList.add(randomQuestion);
            questionAndAnswersList.remove(randomQuestion);


            counter++;

            return randomQuestion;
        } else {

            questionAndAnswersList.clear();

            counter = 0;

            return new QuestionAndAnswers("No questions available. List cleared.", "");
        }
    }
}