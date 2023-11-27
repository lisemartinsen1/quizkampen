package Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DAO {
    protected Database database = new Database();
    private List<QuestionAndAnswers> questionAndAnswersList = new ArrayList<>();
    private List<QuestionAndAnswers> alreadyUsedQuestionsList;
    PropertiesClass propertiesClass = new PropertiesClass();


    public DAO() {
        this.alreadyUsedQuestionsList = new ArrayList<>();
    }

    public QuestionAndAnswers getRandomQuestionAndAnswers(String category) {
        propertiesClass.loadProperties();
        int amountOfQ = propertiesClass.getAmountOfQuestions();
        int counter = 0;

        if (questionAndAnswersList.isEmpty()) {

            if (counter < amountOfQ) {
                questionAndAnswersList = database.readQuestionsAndAnswersFromFile(category);
                counter++;
            } else {
                // Clear the list when amountOfQ is reached
                questionAndAnswersList.clear();
            }
        } else {
            // If the list is not empty, clear it
            questionAndAnswersList.clear();
        }

        // Continue with the rest of your code to get a random question
        Random random = new Random();
        int randomIndex;
        QuestionAndAnswers randomQuestion;

        do {
            randomIndex = random.nextInt(questionAndAnswersList.size());
            randomQuestion = questionAndAnswersList.get(randomIndex);
        } while (alreadyUsedQuestionsList.contains(randomQuestion));

        alreadyUsedQuestionsList.add(randomQuestion);
        questionAndAnswersList.remove(randomQuestion);

        return randomQuestion;
    }
}