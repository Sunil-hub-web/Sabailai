package in.co.sabailai.customer.models;

public class FAQGetSet {

    String question, answer;

    public FAQGetSet(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public FAQGetSet setQuestion(String question) {
        this.question = question;
        return this;
    }

    public String getAnswer() {
        return answer;
    }

    public FAQGetSet setAnswer(String answer) {
        this.answer = answer;
        return this;
    }
}
