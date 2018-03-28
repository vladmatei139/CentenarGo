package echipa_8.centenargo_core.wrappers;

/**
 * Created by Ioan-Emanuel Popescu on 24-Mar-18.
 */

public class Question{
    private Integer uuid;
    private String text;
    private Integer landmarkID;

    public Question() {
    }
    public Question(Integer uuid, String text, Integer landmarkID) {
        this.uuid = uuid;
        this.text = text;
        this.landmarkID = landmarkID;
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

    public Integer getLandmarkID() {
        return landmarkID;
    }
    public void setLandmarkID(Integer landmarkID) {
        this.landmarkID = landmarkID;
    }
}
