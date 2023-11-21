public class Protocol {
    final protected int FIRST_QNA = 0;
    final protected int ANSWERED_FIRST_Q = 1;

    protected int state = FIRST_QNA;
    DAO dao = new DAO();

    public String getOutput(String category) { //getOutput tar kategori som inparameter
        if (state == FIRST_QNA) {
            state = ANSWERED_FIRST_Q;

            QuestionAndAnswers qa = dao.getRandomQuestionAndAnswers(category);
            String question = qa.getQuestion();
            String answers = qa.getAnswers();

            return question + "|" + answers;
        } else if (state == ANSWERED_FIRST_Q) {
            return "";
        }
        return "Unexpected state error";
    }
}