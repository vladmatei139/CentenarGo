package echipa_8.centenargo_core.wrappers;

/**
 * Created by Ioan-Emanuel Popescu on 24-Mar-18.
 */

public class Answer {
    private Integer uuid;
    private String text;
    private Integer questionID;
    private Boolean isCorrect;

    public Answer() {
    }
    public Answer(Integer uuid, String text, Integer questionID, Boolean isCorrect) {
        this.uuid = uuid;
        this.text = text;
        this.questionID = questionID;
        this.isCorrect = isCorrect;
    }

    public Integer getUuid() {
        return uuid;
    }
    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public Integer getQuestionID() {
        return questionID;
    }
    public void setQuestionID(Integer questionID) {
        this.questionID = questionID;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }
    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }
}
