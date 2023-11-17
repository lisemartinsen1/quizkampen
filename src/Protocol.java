public class Protocol {
    final protected int FIRST_QNA = 0;
    final protected int ANSWERED_FIRST_Q = 1;

    protected int state = FIRST_QNA;
    DAO dao = new DAO();

    public QuestionAndAnswers getOutput() {
        if (state == FIRST_QNA) {
            state = ANSWERED_FIRST_Q;

            return dao.getRandomQuestionAndAnswers();
        } else if (state == ANSWERED_FIRST_Q) {
            return null;
        }
        throw new IllegalStateException("Unexpected state error");
    }
}