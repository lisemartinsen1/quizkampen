public class Protocol {
    final protected int FIRST_QNA = 0;
    final protected int NEXT_QNA = 1;

    protected int state = FIRST_QNA;
    DAO dao = new DAO();

    public String getOutput() {   //Uppdaterat getOutput metod
        QuestionAndAnswers qa;
        if (state == FIRST_QNA) {
            state = NEXT_QNA;

            qa = dao.getRandomQuestionAndAnswers();

        } else if (state == NEXT_QNA) {
            qa = dao.getRandomQuestionAndAnswers();
        }
        else {
            return "Unexpected state error";
        }
        String question = qa.getQuestion();
        String answers = qa.getAnswers();
        return question + "|" + answers;
    }
}