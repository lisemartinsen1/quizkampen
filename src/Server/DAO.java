package Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DAO {
    protected Database database = new Database();
    private List<QuestionAndAnswers> questionAndAnswersList;
    private List<QuestionAndAnswers> alreadyUsedQuestionsList;
    int count = 0;

    public DAO(){
        this.alreadyUsedQuestionsList = new ArrayList<>();
    }

    public QuestionAndAnswers getRandomQuestionAndAnswers(String category) {

        while (true) {
            if (count == 0 || questionAndAnswersList == null) {
                System.out.println("Cat from DAO: " + category);
                questionAndAnswersList = database.readQuestionsAndAnswersFromFile(category);
                count += 3;

                 if (questionAndAnswersList == null || questionAndAnswersList.isEmpty()) {
                    return new QuestionAndAnswers("No questions available. Call readQuestionsAndAnswersFromFile() first.", "");
                }
            } else if (count > 0) {
                Random random = new Random();
                int randomIndex;
                QuestionAndAnswers randomQuestion;
                System.out.println("Q&A size: " + questionAndAnswersList.size());
                do {
                    randomIndex = random.nextInt(questionAndAnswersList.size());
                    randomQuestion = questionAndAnswersList.get(randomIndex);
                } while (alreadyUsedQuestionsList.contains(randomQuestion));

                alreadyUsedQuestionsList.add(randomQuestion);
                questionAndAnswersList.remove(randomQuestion);
                count--;
                return randomQuestion;
            }
        }
    }

}