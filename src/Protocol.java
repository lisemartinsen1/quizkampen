import jdk.jfr.Category;

import java.util.ArrayList;

public class Protocol {
    final protected int FIRST_QNA = 0;
    final protected int NEXT_QNA = 1;
    final protected int ALL_Q_ASKED = 2;

    protected int state = FIRST_QNA;
    DAO dao = new DAO();
    ArrayList<QuestionAndAnswers> poolOfQuestions = new ArrayList<>();
    int counter = 1;

    public String getOutput(String category) { //getOutput tar kategori som inparameter
        QuestionAndAnswers qa = null;
        if (state == FIRST_QNA) {
            state = NEXT_QNA;

            PropertiesClass propertiesClass = new PropertiesClass();
            propertiesClass.loadProperties();
            int questionsEachRound = propertiesClass.getAmountOfQuestions();

            for (int i = 0; i < questionsEachRound; i++) {
                poolOfQuestions.add(dao.getRandomQuestionAndAnswers(category));
            }

            System.out.println(poolOfQuestions.size());

            qa = poolOfQuestions.get(0);

        } else if (state == NEXT_QNA) {

            if (counter == poolOfQuestions.size()) {
                state = ALL_Q_ASKED;
                return "ALL_Q_ASKED";
            }
            qa = poolOfQuestions.get(counter);
            counter++;

        } else {
            System.err.println("Unexpected state error");
            return null;
        }

        String question = qa.getQuestion();
        String answers = qa.getAnswers();
        return question + "|" + answers;
    }
}