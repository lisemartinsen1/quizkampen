import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String FILE_PATH = "src/questions.txt";
    private List<QuestionAndAnswers> questionAndAnswersList;

    public List<QuestionAndAnswers> readQuestionsAndAnswersFromFile() {
        questionAndAnswersList = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = in.readLine()) != null) {
                String question = line;
                String answersLine = in.readLine();
                questionAndAnswersList.add(new QuestionAndAnswers(question, answersLine));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return questionAndAnswersList;
    }
    public String getAnswersForQuestion(String question) {
        for (QuestionAndAnswers qa : questionAndAnswersList) {
            if (qa.getQuestion().equals(question)) {
                return qa.getAnswers();
            }
        }
        return "Answers not found for the given question.";
    }

}

class QuestionAndAnswers {
    private String question;
    private String answers;

    public QuestionAndAnswers(String question, String answers) {
        this.question = question;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswers() {
        return answers;
    }
}