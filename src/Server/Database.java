package Server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String FILE_PATH_CATEGORY1 = "src/questions.txt";
    private static final String FILE_PATH_CATEGORY2 = "src/questions2.txt";
    private static final String FILE_PATH_CATEGORY3 = "src/questions3.txt";
    private static final String FILE_PATH_CATEGORY4 = "src/questions4.txt";
    private List<QuestionAndAnswers> questionAndAnswersList;

    public List<QuestionAndAnswers> readQuestionsAndAnswersFromFile(String category) {
        questionAndAnswersList = new ArrayList<>();
        String FILE_PATH = "";
        if (category.equals("CATEGORY1")) {
            FILE_PATH = FILE_PATH_CATEGORY1;
        } else if (category.equals("CATEGORY2")) {
            FILE_PATH = FILE_PATH_CATEGORY2;
        }else if (category.equals("CATEGORY3")) {
            FILE_PATH = FILE_PATH_CATEGORY3;
        }else if (category.equals("CATEGORY4")) {
            FILE_PATH = FILE_PATH_CATEGORY4;
        }

        try (BufferedReader in = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = in.readLine()) != null) {
                String answersLine = in.readLine();
                questionAndAnswersList.add(new QuestionAndAnswers(line, answersLine));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return questionAndAnswersList;
    }
}

class QuestionAndAnswers {
    private final String question;
    private final String answers;

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